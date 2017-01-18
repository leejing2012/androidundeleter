package com.reneelab.androidundeleter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.reneelab.DataModel.WifiAdapter;
import com.reneelab.DataModel.WifiInfo;
import com.reneelab.DataModel.WifiManage;

import java.util.List;

;

/**
 * Created by Administrator on 2016/5/17.
 */
public class MCsWifi extends Fragment {
    private MCsEntry main;
    private Fragment ToFragment;
    private WifiManage wifiManage;
    private int wifinum;
    private TextView Nwifi;
    private LinearLayout backarea;
    private MCsSwitchFragment switchFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifiManage = new WifiManage();
        main = new MCsEntry();
        ToFragment = new MCsContentFragment();
        switchFragment = new MCsSwitchFragment(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wifi_layout, null);
        Nwifi = (TextView)view.findViewById(R.id.wifiNum);
        backarea =(LinearLayout)view.findViewById(R.id.backarea);
        backarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // switchFragment.switchFragment(getFragmentManager().findFragmentById(getId()),ToFragment);
               // main.openSliding();
                getFragmentManager().popBackStack();
            }
        });
        initData(view);
        Nwifi.setText(wifinum + "个wifi信息");
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void initData(View view){
        try {
            List<WifiInfo> wifiInfos = wifiManage.Read();
            wifinum = wifiInfos.size();
            ListView wifiInfosView=(ListView)view.findViewById(R.id.WifiInfosView);
            WifiAdapter ad = new WifiAdapter(wifiInfos,this.getActivity());
            wifiInfosView.setAdapter(ad);
        }catch (Exception e){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
