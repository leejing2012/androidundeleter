package com.reneelab.DataModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.reneelab.androidundeleter.MCsCommData;
import com.reneelab.androidundeleter.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/8.
 */
public class MCsCommListView extends BaseAdapter {
    private Context context;
    private ArrayList<CommModel> comm;
    private static HashMap<Integer, Boolean> isSelected;
    private int allchoose;
    private MCsCommData comdata;

    class ViewHolder {
        TextView tvName;
        CheckBox cb;
        TextView phone_comm_time;
        TextView comm_lastTime,recover_text;
    }

    public MCsCommListView(Context context, ArrayList<CommModel> cm, MCsCommData com,int all) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.comm = cm;
        this.allchoose = all;
        this.comdata = com;
        isSelected = new HashMap<Integer, Boolean>();
        initDate(all);
    }

    // 初始化isSelected的数据
    private void initDate(int a) {
        if(a == 0){
            for (int i = 0; i < comm.size(); i++) {
                getIsSelected().put(i, false);
            }
        }else if (a==1){
            for (int i = 0; i < comm.size(); i++) {
                getIsSelected().put(i, true);
            }
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return comm.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return comm.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // 页面
        ViewHolder holder;
        String comm_name = Long.toString(comm.get(position).getPhone());
        final String comm_data = comm.get(position).getDate();
        String comm_dur = Integer.toString(comm.get(position).getDuration());
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.comm_item, null);
            holder = new ViewHolder();
            holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.tvName = (TextView) convertView.findViewById(R.id.phone_name);
            holder.phone_comm_time = (TextView)convertView.findViewById(R.id.phone_comm_time);
            holder.comm_lastTime = (TextView)convertView.findViewById(R.id.comm_lastTime);
            holder.recover_text = (TextView)convertView.findViewById(R.id.recoverFlag);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(comm_name);
        holder.phone_comm_time.setText(comm_data);
        holder.comm_lastTime.setText(comm_dur+"秒钟");

        if(comm.get(position).isRcoverFlag()){
            holder.cb.setVisibility(View.GONE);
            holder.comm_lastTime.setVisibility(View.GONE);
            holder.recover_text.setVisibility(View.VISIBLE);
        }else {
            holder.cb.setVisibility(View.VISIBLE);
            holder.comm_lastTime.setVisibility(View.VISIBLE);
            holder.recover_text.setVisibility(View.GONE);
        }
        // 监听checkBox并根据原来的状态来设置新的状态
        holder.cb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                    comdata.deleteComm(comm.get(position).getPhone());
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                    comdata.addComm(comm.get(position));
                }

            }
        });
        // 根据isSelected来设置checkbox的选中状况
        holder.cb.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        MCsCommListView.isSelected = isSelected;
    }
}
