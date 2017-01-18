package com.reneelab.androidundeleter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reneelab.DataModel.ContacterModel;
import com.reneelab.DataModel.MCsContactListView;
import com.reneelab.DataModel.ScanProgressPar;
import com.reneelab.jni.CallBack;
import com.reneelab.jni.initJni;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/5/12.
 */
public class MCsContacterData extends Fragment implements CallBack {

    private ListView listView;
    private MCsContactListView adapter;
    private Button recoverBtn;
    private TextView title,contacter_num,scan_percent,t_down;
    private CheckBox RightBox,ChooseCheckBox;
    private LinearLayout backarea,contacterhead;
    public MCsEntry main;
    private LinearLayout  back;
    private Fragment ToRecover;
    public SharedPreferences mySharedPreferences;
    private ProgressBar scan_progress;
    private ScanProgressPar asyncTask;

    private ArrayList<ContacterModel> contacter,recover_contacter;

    public View view1;
    public MCsContacterData k;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main =(MCsEntry)getActivity();
        ToRecover = new MCsContentFragment();
        contacter = new ArrayList<ContacterModel>();
        recover_contacter = new ArrayList<ContacterModel>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_result, null);
        k = this;
        view1= view;
        RightBox = (CheckBox)view.findViewById(R.id.Rightcheck);
        ChooseCheckBox = (CheckBox)view.findViewById(R.id.Leftcheck);
        recoverBtn = (Button)view.findViewById(R.id.recover);
        contacter_num = (TextView)view.findViewById(R.id.contacter_num);

        t_down = (TextView)view.findViewById(R.id.t_down);
        scan_progress = (ProgressBar)view.findViewById(R.id.pb_progressbarf);
        scan_percent = (TextView)view.findViewById(R.id.scan_pro_num);
        contacterhead = (LinearLayout)view.findViewById(R.id.contacterhead);

        title =(TextView)view.findViewById(R.id.title);
        title.setText(getString(R.string.recoverContact));
        recoverBtn.setBackgroundColor(getResources().getColor(R.color.contacts));
        recoverBtn.setText(getString(R.string.recoverContact));
        recoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* mySharedPreferences= getActivity().getSharedPreferences("test", Activity.MODE_PRIVATE);
                String uname = mySharedPreferences.getString("username","null");
                if(uname.equalsIgnoreCase("null")){
                    Fragment to = new MCsThirdLogin();
                    switchFragment(getFragmentManager().findFragmentById(getId()),to);
                }else{
                    Fragment to  = new MCsPay();
                    switchFragment(getFragmentManager().findFragmentById(getId()),to);
                }*/
                if (recover_contacter.isEmpty()){
                   Toast.makeText(getActivity(),"至少选择一项。",Toast.LENGTH_SHORT).show();
                }else {
                    for (int i= 0;i<recover_contacter.size();i++){
                        ContentValues values = new ContentValues();
                        //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
                        Uri rawContactUri = getActivity().getContentResolver().insert(RawContacts.CONTENT_URI, values);
                        long rawContactId = ContentUris.parseId(rawContactUri);
                        //往data表入姓名数据
                        values.clear();
                        values.put(Data.RAW_CONTACT_ID, rawContactId);
                        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
                        values.put(StructuredName.GIVEN_NAME, recover_contacter.get(i).getName());
                        getActivity().getContentResolver().insert(
                                android.provider.ContactsContract.Data.CONTENT_URI, values);
                        //往data表入电话数据
                        values.clear();
                        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                        values.put(Phone.NUMBER, recover_contacter.get(i).getPhone());
                        values.put(Phone.TYPE, Phone.TYPE_MOBILE);
                        getActivity().getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
                        for(int j = 0;j<contacter.size();j++){
                            if(contacter.get(j).getPhone()==recover_contacter.get(i).getPhone()){
                                contacter.get(j).setFlag(1);
                            }
                        }
                    }
                    Toast.makeText(getActivity(),"联系人恢复成功！",Toast.LENGTH_SHORT).show();
                    recover_contacter.clear();
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });


        RightBox.setVisibility(View.INVISIBLE);
        backarea = (LinearLayout)view.findViewById(R.id.backarea);
        backarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initJni.getInstance().stopScan();
                main.openSliding();
                getFragmentManager().popBackStackImmediate("com.reneelab.androidundeleter.MCsContentFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        asyncTask = new ScanProgressPar(scan_percent, scan_progress,contacterhead,t_down);
        asyncTask.execute(100);

        ChooseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                  addAllContacter();
                }else {
                    delAllContacter();
                }
            }
        });

        Thread mThread = new Thread(runnable_logic);
        mThread.start();
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getActivity().getSupportFragmentManager().putFragment(outState, "mContent", this);
    }

    Runnable runnable_logic = new Runnable() {
        @Override
        public void run() {
            initJni.getInstance().recover_type(getContext(),k,4,0);
            handle.sendEmptyMessage(1);
        }
    };

    Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                initView(view1);
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view) {
        // TODO Auto-generated method stub
        contacter_num.setText(String.valueOf(contacter.size()));
        listView = (ListView)view.findViewById(R.id.listView1);
        adapter = new MCsContactListView((MCsEntry)getActivity(), contacter,this,0);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public void addContacter(ContacterModel cm){
        recover_contacter.add(cm);
    }

    public  void deleteContacter(String delname){
        for(int i=0;i<recover_contacter.size();i++){
            if(recover_contacter.get(i).getName()==delname){
                recover_contacter.remove(i);
            }
        }
    }

    private void addAllContacter(){
        recover_contacter.clear();
        if(contacter.size()==0){
            Toast.makeText(getContext(),"扫描记录为空,暂时无法选择.",Toast.LENGTH_SHORT).show();
            ChooseCheckBox.setChecked(false);
        }else{
            try{
                for(int i = 0;i<contacter.size();i++){
                    recover_contacter.add(contacter.get(i));
                }
                adapter = new MCsContactListView((MCsEntry)getActivity(), contacter,this,1);
                listView.setAdapter(adapter);
            }catch (Exception e){
                ChooseCheckBox.setChecked(false);
            }

        }

    }

    private void delAllContacter(){
         recover_contacter.clear();
        adapter = new MCsContactListView((MCsEntry)getActivity(), contacter,this,0);
        listView.setAdapter(adapter);
    }

    @Override
    public void doSomething(String str) {
        // TODO Auto-generated method stub
    }

    public void show_sms(long phone,String body,String date){
    }

    @Override
    public void ex_file(String out, int no, long size,String format) {
        // TODO Auto-generated method stub
    }

    @Override
    public void callback_getAccess(String path) {
        // TODO Auto-generated method stub
    }

    public void show_comm(long phone,String date,int duration){
    }

    public void callback_percent(int percent){}

    public void show_contacter(String phone,String name){
        ContacterModel con = new ContacterModel();
        con.setName(name);
        con.setPhone(phone);
        contacter.add(con);
    }
}
