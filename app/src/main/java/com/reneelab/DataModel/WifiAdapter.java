package com.reneelab.DataModel;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.reneelab.androidundeleter.R;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class WifiAdapter extends BaseAdapter {
    List<WifiInfo> wifiInfos =null;
    Context con;

    public WifiAdapter(List<WifiInfo> wifiInfos,Context con){
        this.wifiInfos =wifiInfos;
        this.con = con;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return wifiInfos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return wifiInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = LayoutInflater.from(con).inflate(R.layout.wifi_view, null);
        TextView tv = (TextView)convertView.findViewById(R.id.wifi_name);
        TextView pas = (TextView)convertView.findViewById(R.id.wifi_password);
        Button btn_copy = (Button)convertView.findViewById(R.id.copy);
        tv.setText(wifiInfos.get(position).Ssid);
        pas.setText(wifiInfos.get(position).Password);

        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(wifiInfos.get(position).Password);
                Toast.makeText(con,"密码已经复制到粘贴板，请直接去到wifi登陆界面粘贴",Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
