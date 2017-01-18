package com.reneelab.DataModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.reneelab.androidundeleter.MCsContacterData;
import com.reneelab.androidundeleter.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/11.
 */
public class MCsContactListView extends BaseAdapter {
    private Context context;
    private ArrayList<ContacterModel> beans;
    private MCsContacterData cdata;

    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;
    private int all_choose;

    class ViewHolder {

        TextView tvName;
        TextView phonenumber;
        CheckBox cb;
        TextView recoverFlag;
    }

    public MCsContactListView(Context context, ArrayList<ContacterModel> c, MCsContacterData a,int all) {
        // TODO Auto-generated constructor stub
        this.beans = c;
        this.context = context;
        isSelected = new HashMap<Integer, Boolean>();
        this.cdata =a;
        this.all_choose = all;
        // 初始化数据
        initDate(all_choose);
    }

    // 初始化isSelected的数据
    private void initDate(int b) {
        if(b==0){
            for (int i = 0; i < beans.size(); i++) {
                getIsSelected().put(i, false);
            }
        }else if(b==1){
            for (int i = 0; i < beans.size(); i++) {
                getIsSelected().put(i, true);
            }
        }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return beans.get(position).getName();
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
        final ViewHolder holder;
        String cname = beans.get(position).getName();
        String cnumber = beans.get(position).getPhone();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_item, null);
            holder = new ViewHolder();
            holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
            holder.phonenumber = (TextView)convertView.findViewById(R.id.phone_number);
            holder.tvName = (TextView) convertView.findViewById(R.id.phone_name);
            holder.recoverFlag =(TextView)convertView.findViewById(R.id.recoverFlag);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(cname);
        holder.phonenumber.setText(cnumber);
        if(beans.get(position).getFlag()==1){
            holder.recoverFlag.setVisibility(View.VISIBLE);
            holder.cb.setVisibility(View.INVISIBLE);
        }else {
            holder.recoverFlag.setVisibility(View.INVISIBLE);
            holder.cb.setVisibility(View.VISIBLE);
        }
        // 监听checkBox并根据原来的状态来设置新的状态
        holder.cb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                    beans.get(position).setFlag(0);
                    cdata.deleteContacter(beans.get(position).getName());
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                    beans.get(position).setFlag(1);
                    cdata.addContacter(beans.get(position));
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
        MCsContactListView.isSelected = isSelected;
    }
}
