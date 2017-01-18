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
public class MCsDocumentListView extends BaseAdapter{
    private Context context;
    private ArrayList<documentModel> beans;
    private SparseBooleanArray mCheckStateArray;
    private MCsRecoverFile rf;

    class ViewHolder {
        TextView document_name;
        CheckBox cb;
        TextView document_size;
        TextView document_recover;
    }

    public MCsDocumentListView(Context context, ArrayList<documentModel> beans, MCsRecoverFile k) {
        // TODO Auto-generated constructor stub
        this.beans = beans;
        this.context = context;
        this.mCheckStateArray = new SparseBooleanArray();
        rf=k;
    }

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
        String audio_name = beans.get(position).getDocument_name();
        float audio_time = (float)beans.get(position).getSize();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.document_item, null);
            holder = new ViewHolder();
            holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            holder.document_name = (TextView) convertView.findViewById(R.id.document_name);
            holder.document_size = (TextView)convertView.findViewById(R.id.document_size);
            holder.document_recover = (TextView)convertView.findViewById(R.id.document_recover);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        holder.document_name.setText(audio_name);
        audio_time = audio_time/1024;
        audio_time = (Math.round(audio_time*10))/10;
        holder.document_size.setText("大小："+String.valueOf(audio_time)+"KB");
        // 监听checkBox并根据原来的状态来设置新的状态
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setChecked(position,isChecked);
                if(isChecked){
                    rf.addDocumentList(beans.get(position));
                }else{
                    rf.delDocumentList(beans.get(position));
                }
            }
        });
        holder.cb.setChecked(isChecked(position));
        if(beans.get(position).isFlag()){
            holder.document_recover.setVisibility(View.VISIBLE);
            holder.cb.setVisibility(View.GONE);
        }else {
            holder.document_recover.setVisibility(View.GONE);
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
