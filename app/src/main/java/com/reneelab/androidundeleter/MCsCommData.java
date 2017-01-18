package com.reneelab.androidundeleter;

import android.Manifest;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
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

import com.reneelab.DataModel.CommModel;
import com.reneelab.DataModel.MCsCommListView;
import com.reneelab.DataModel.ScanProgressPar;
import com.reneelab.jni.CallBack;
import com.reneelab.jni.initJni;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/8.
 */
public class MCsCommData extends Fragment implements CallBack {
    private ListView listView;
    private MCsCommListView adapter;
    private Button recoverBtn;
    private TextView title,comm_num,scan_percent,t_down;
    private CheckBox hiddenBox,ChooseCheckBox;
    private LinearLayout backarea,commhead;
    public MCsEntry main;
    private Fragment ToRecover;
    public SharedPreferences mySharedPreferences;
    private ProgressBar scan_progress;
    private ScanProgressPar asyncTask;
    private ArrayList<CommModel> comm_list,recover_comm;
    public MCsCommData k;
    public View view1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main =(MCsEntry)getActivity();
        ToRecover = new MCsContentFragment();
        comm_list = new ArrayList<CommModel>();
        recover_comm = new ArrayList<CommModel>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        k = this;

        View view = inflater.inflate(R.layout.comm_result, null);
        view1 = view;
        hiddenBox = (CheckBox)view.findViewById(R.id.Leftcheck);
        ChooseCheckBox = (CheckBox)view.findViewById(R.id.Rightcheck);
        recoverBtn = (Button)view.findViewById(R.id.recover);
        backarea = (LinearLayout)view.findViewById(R.id.backarea);
        comm_num = (TextView)view.findViewById(R.id.comm_num);

        t_down = (TextView)view.findViewById(R.id.t_down);
        scan_progress = (ProgressBar)view.findViewById(R.id.pb_progressbarf);
        scan_percent = (TextView)view.findViewById(R.id.scan_pro_num);
        commhead = (LinearLayout)view.findViewById(R.id.commhead);

        backarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initJni.getInstance().stopScan();
                main.openSliding();
               // switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()), ToRecover);
                getFragmentManager().popBackStackImmediate("com.reneelab.androidundeleter.MCsContentFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        title =(TextView)view.findViewById(R.id.title);
        title.setText(getString(R.string.recoverCall));
        recoverBtn.setBackgroundColor(getResources().getColor(R.color.call));
        recoverBtn.setText(getString(R.string.recoverCall));

        recoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* mySharedPreferences= getActivity().getSharedPreferences("test", Activity.MODE_PRIVATE);
                String uname = mySharedPreferences.getString("username","null");
                if(uname.equalsIgnoreCase("null")){
                    Fragment to = new MCsThirdLogin();
                    switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()),to);
                }else{
                    Fragment to  = new MCsPay();
                    switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()),to);
                }*/
                for(int i=0;i<recover_comm.size();i++){
                    //insertCallLog(Long.toString(recover_comm.get(i).getPhone()),recover_comm.get(i).getDate(),String.valueOf(recover_comm.get(i).getDuration()));
                    for(int j = 0;j<comm_list.size();j++){
                        if(comm_list.get(j).getPhone()==comm_list.get(i).getPhone()){
                            comm_list.get(j).setRcoverFlag(true);
                        }
                    }
                }
                Toast.makeText(getActivity(),"通讯记录恢复成功！",Toast.LENGTH_SHORT).show();
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        hiddenBox.setVisibility(View.INVISIBLE);

        ChooseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    addAllComm();
                }else{
                    delAllComm();
                }
            }
        });

        asyncTask = new ScanProgressPar(scan_percent, scan_progress,commhead,t_down);
        asyncTask.execute(100);

        Thread mThread = new Thread(runnable_logic);
        mThread.start();

        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    Runnable runnable_logic = new Runnable() {
        @Override
        public void run() {
            initJni.getInstance().recover_type(getContext(),k,3,0);
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

    private void addAllComm(){
        recover_comm.clear();
        if(comm_list.size()==0){
            Toast.makeText(getContext(),"扫描记录为空,暂时无法选择.",Toast.LENGTH_SHORT).show();
            ChooseCheckBox.setChecked(false);
        }else{
            try{
                for(int i = 0;i<comm_list.size();i++){
                    recover_comm.add(comm_list.get(i));
                }
                adapter = new MCsCommListView((MCsEntry)getActivity(), comm_list,this,1);
                listView.setAdapter(adapter);
            }catch (Exception e){
                ChooseCheckBox.setChecked(false);
            }
        }
    }

    private void delAllComm(){
        recover_comm.clear();
        adapter = new MCsCommListView((MCsEntry)getActivity(), comm_list,this,0);
        listView.setAdapter(adapter);
    }

    public void addComm(CommModel cm){
        recover_comm.add(cm);
    }

    public  void deleteComm(long delphone){
        for(int i=0;i<recover_comm.size();i++){
            if(recover_comm.get(i).getPhone()==delphone){
                recover_comm.remove(i);
            }
        }
    }

    private void insertCallLog(String number,String date, String duration){
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, number);
        values.put(CallLog.Calls.DATE, date);
        values.put(CallLog.Calls.DURATION, duration);
        values.put(CallLog.Calls.TYPE,CallLog.Calls.INCOMING_TYPE);//未接
        /*values.put(CallLog.Calls.NEW, string4);//0已看1未看*/
        /*来电：CallLog.Calls.INCOMING_TYPE  (常量值：1)
        已拨：CallLog.Calls.OUTGOING_TYPE (常量值：2)
        未接：CallLog.Calls.MISSED_TYPE (常量值：3)*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
                getContext().getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
            }else{
                //
            }
        }else{
            getContext().getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
        }

    }

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
        comm_num.setText(String.valueOf(comm_list.size()));
        listView = (ListView)view.findViewById(R.id.listView1);
        adapter = new MCsCommListView((MCsEntry)getActivity(),comm_list,this,0);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
        CommModel cm = new CommModel();
        cm.setPhone(phone);
        cm.setDate(date);
        cm.setDuration(duration);
        comm_list.add(cm);
       /* s.add(phone);
        s1.add(date);
        s2.add(duration);*/
    }

    public void callback_percent(int percent){}

    public void show_contacter(String phone,String name){}

}
