package com.reneelab.androidundeleter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reneelab.DataModel.ChildrenItem;
import com.reneelab.DataModel.GroupItem;
import com.reneelab.DataModel.MCsSmsList;
import com.reneelab.DataModel.ScanProgressPar;
import com.reneelab.jni.CallBack;
import com.reneelab.jni.initJni;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class MCsSms extends Fragment implements CallBack{

    private ExpandableListView expandableList;
    private MCsSmsList adapter;
    private List<GroupItem> dataList = new ArrayList<GroupItem>();
    private Button rb;
    private LinearLayout llt,smshead;
    private Fragment ToRecover = null;
    public MCsEntry main;
    private TextView title,sms_num,scan_percent,t_down;
    private CheckBox hiddenBox;
    public SharedPreferences mySharedPreferences;
    private MCsSms k;
    public  int scan_num;
    private ProgressBar scan_progress;
    private ScanProgressPar asyncTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main =(MCsEntry)getActivity();
        scan_num = 0;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sms_layout, null);
        k = this;
        initWidget(view);
        Thread mThread = new Thread(runnable_logic);
        mThread.start();
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    Runnable runnable_logic = new Runnable() {
        @Override
        public void run() {
            initJni.getInstance().recover_type(getContext(),k,2,0);
            handle.sendEmptyMessage(1);
        }
    };

    Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                initView();
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showCheckedItems(){
        String checkedItems = "";
        List<String> checkedChildren = adapter.getCheckedChildren();
        if (checkedChildren!=null && !checkedChildren.isEmpty()) {
            for (String child : checkedChildren) {
                if (checkedItems.length()>0) {
                    checkedItems += "\n";
                }

                checkedItems += child;
            }
        }
    }

    @Override
    public void doSomething(String str) {
        // TODO Auto-generated method stub
    }

    public void show_sms(long phone,String body,String date){
        scan_num++;
        List<ChildrenItem> list1 = new ArrayList<ChildrenItem>();
        list1.add(new ChildrenItem("childrenItem1",date,body));

        GroupItem groupItem1 = new GroupItem("groupItem1","1条短息("+phone+")",list1);
        dataList.add(groupItem1);

      /*  adapter = new MCsSmsList(getContext(),dataList);
        expandableList.setAdapter(adapter);*/
    }


    public void initView(){
        adapter = new MCsSmsList(getContext(),dataList);
        expandableList.setAdapter(adapter);
        sms_num.setText(String.valueOf(scan_num));
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

    public void show_contacter(String phone,String name){}

    private void initWidget(View view){
        sms_num = (TextView)view.findViewById(R.id.sms_num_get);
        expandableList = (ExpandableListView)view.findViewById(R.id.expandable_list);
        expandableList.setGroupIndicator(null);
        hiddenBox = (CheckBox)view.findViewById(R.id.Leftcheck);
        t_down = (TextView)view.findViewById(R.id.t_down);
        scan_progress = (ProgressBar)view.findViewById(R.id.pb_progressbarf);
        scan_percent = (TextView)view.findViewById(R.id.scan_pro_num);
        smshead = (LinearLayout)view.findViewById(R.id.smshead);

        rb = (Button)view.findViewById(R.id.recover);
        rb.setText(R.string.recoverSms);

        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mySharedPreferences= getActivity().getSharedPreferences("test", Activity.MODE_PRIVATE);
                String uname = mySharedPreferences.getString("username","null");
                if(uname.equalsIgnoreCase("null")){
                    Fragment to = new MCsThirdLogin();
                    switchFragment(getFragmentManager().findFragmentById(getId()),to);
                }else{
                }*/
                Fragment to  = new MCsPay();
                getFragmentManager().popBackStackImmediate("com.reneelab.androidundeleter.MCsContentFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        asyncTask = new ScanProgressPar(scan_percent, scan_progress,smshead,t_down);
        asyncTask.execute(100);
        ToRecover = new MCsContentFragment();

        title = (TextView)view.findViewById(R.id.title);
        title.setText(getString(R.string.recoverSms));

        llt = (LinearLayout)view.findViewById(R.id.backarea);
        llt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initJni.getInstance().stopScan();
                main.openSliding();
                getFragmentManager().popBackStackImmediate("com.reneelab.androidundeleter.MCsContentFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
               // switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()), ToRecover);
            }
        });
        hiddenBox.setVisibility(View.INVISIBLE);
    }
}
