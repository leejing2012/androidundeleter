package com.reneelab.DataModel;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.reneelab.androidundeleter.MCsRecoverFile;
import com.reneelab.androidundeleter.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/19.
 */
public class MCsVideoListView extends BaseAdapter{
    private Context context;
    private ArrayList<videoModel> beans;
    private MCsRecoverFile rf;
    private SparseBooleanArray mCheckStateArray;


    class ViewHolder {
        TextView mp3_name;
        CheckBox cb;
        TextView mp3_time;
        TextView video_recover;
    }

    public MCsVideoListView(Context context, ArrayList<videoModel> beans, MCsRecoverFile k) {
        // TODO Auto-generated constructor stub
        this.beans = beans;
        this.context = context;
        this.mCheckStateArray = new SparseBooleanArray();
        rf=k;
        // 初始化数据

    }

    // 初始化isSelected的数据


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return beans.get(position);
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
        String audio_name = beans.get(position).getVideo_name();
        float audio_time = (float)beans.get(position).getSize();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.avi_item, null);
            holder = new ViewHolder();
            holder.mp3_name = (TextView) convertView.findViewById(R.id.mp3_name);
            holder.mp3_time = (TextView)convertView.findViewById(R.id.mp3_size);
            holder.cb = (CheckBox)convertView.findViewById(R.id.checkBoxAvi);
            holder.video_recover = (TextView)convertView.findViewById(R.id.video_recover);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mp3_name.setText(audio_name);
        audio_time = audio_time/1024;
        audio_time = (Math.round(audio_time*10))/10;
        holder.mp3_time.setText("大小："+String.valueOf(audio_time)+"KB");
        // 监听checkBox并根据原来的状态来设置新的状态
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setChecked(position,isChecked);
               if(isChecked){
                rf.addVideoList(beans.get(position));
               }else{
                   rf.delVideoList(beans.get(position));
               }
            }
        });
        holder.cb.setChecked(isChecked(position));
        if(beans.get(position).isFlag()){
            holder.video_recover.setVisibility(View.VISIBLE);
            holder.cb.setVisibility(View.GONE);
        }else {
            holder.video_recover.setVisibility(View.GONE);
            holder.cb.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStateArray.put(position, isChecked);
    }

    public boolean isChecked(int position) {
        return mCheckStateArray.get(position);
    }

}
