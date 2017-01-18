package com.reneelab.DataModel;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reneelab.androidundeleter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MCsSmsList extends BaseExpandableListAdapter {
   private List<GroupItem> dataList;
    private LayoutInflater inflater;
    private  String sms_content;
    //以选中的子列表项
    private List<String> checkedChildren = new ArrayList<String>();
    //父列表项的选中状态：value值为1（选中）、2（部分选中）、3（未选中）
    private Map<String, Integer> groupCheckedStateMap = new HashMap<String, Integer>();

    public MCsSmsList(Context context, List<GroupItem> dataList){
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
        //默认设置所有的父列表项和子列表项都为选未中状态
        int groupCount = getGroupCount();
        for (int groupPosition = 0; groupPosition < groupCount; groupPosition++) {
            try {
                GroupItem groupItem = dataList.get(groupPosition);
                if (groupItem==null || groupItem.getChildrenItems() == null|| groupItem.getChildrenItems().isEmpty()) {
                    groupCheckedStateMap.put(groupItem.getId(), 3);
                    continue;
                }
                groupCheckedStateMap.put(groupItem.getId(), 3);
               // List<ChildrenItem> childrenItems = groupItem.getChildrenItems();
               /* for (ChildrenItem childrenItem : childrenItems) {
                    checkedChildren.add(childrenItem.getId());
                }*/
            } catch (Exception e) {

            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        System.err.println("---getChild----groupPosition----"+groupPosition+"----childPosition-----"+childPosition);
        final GroupItem groupItem = dataList.get(groupPosition);
        if (groupItem==null || groupItem.getChildrenItems()==null|| groupItem.getChildrenItems().isEmpty()) {
            return null;
        }
        return groupItem.getChildrenItems().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        ChildrenItem childrenItem = (ChildrenItem) getChild(groupPosition, childPosition);
        ChildViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.sms_children_item, null);
            viewHolder.SmsContentTime = (TextView) convertView.findViewById(R.id.sms_content_time);
            viewHolder.SmsContent = (TextView)convertView.findViewById(R.id.sms_content);
            viewHolder.SmsContentCheckbox = (CheckBox) convertView.findViewById(R.id.children_cb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        viewHolder.SmsContentTime.setText(childrenItem.getName());
        viewHolder.SmsContent.setText(childrenItem.getSms_body());
        final String childrenId = childrenItem.getId();
        viewHolder.SmsContentCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //@Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!checkedChildren.contains(childrenId)) {
                        checkedChildren.add(childrenId);
                    }
                }else {
                    checkedChildren.remove(childrenId);
                }
                setGroupItemCheckedState(dataList.get(groupPosition));
                MCsSmsList.this.notifyDataSetChanged();
            }
        });


        if (checkedChildren.contains(childrenId)) {
            viewHolder.SmsContentCheckbox.setChecked(true);
        }else {
            viewHolder.SmsContentCheckbox.setChecked(false);
        }
        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        final GroupItem groupItem = dataList.get(groupPosition);
        if (groupItem== null || groupItem.getChildrenItems()==null
                || groupItem.getChildrenItems().isEmpty()) {
            return 0;
        }
        return groupItem.getChildrenItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (dataList==null) {
            return null;
        }
        return dataList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        if (dataList==null) {
            return 0;
        }
        return dataList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
        try {
            GroupItem groupItem = dataList.get(groupPosition);
            GroupViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new GroupViewHolder();
                convertView = inflater.inflate(R.layout.sms_parent_item, null);
                viewHolder.SmsTitle = (TextView) convertView.findViewById(R.id.sms_title_content);
                viewHolder.SmsTitleExpandIcon = (ImageView) convertView.findViewById(R.id.expand_icon);
                viewHolder.SmsTitleCheckBox = (ImageView) convertView.findViewById(R.id.sms_title_checkbox);
                viewHolder.SmsTitleCheckboxSpace = (LinearLayout) convertView.findViewById(R.id.sms_checkbox_space);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GroupViewHolder) convertView.getTag();
            }
            if(isExpanded){
                viewHolder.SmsTitleExpandIcon.setBackgroundResource(R.drawable.file_indicator_expand);
            }else{
                viewHolder.SmsTitleExpandIcon.setBackgroundResource(R.drawable.file_indicator_collapse);
            }


            viewHolder.SmsTitleCheckboxSpace.setOnClickListener(new GroupCBLayoutOnClickListener(groupItem));
            viewHolder.SmsTitle.setText(groupItem.getName());
            int state = groupCheckedStateMap.get(groupItem.getId());
            switch (state) {
                case 1:
                    viewHolder.SmsTitleCheckBox.setImageResource(R.drawable.checkbox_selected);
                    break;
                case 2:
                    viewHolder.SmsTitleCheckBox.setImageResource(R.drawable.checkbox_normal);
                    break;
                case 3:
                    viewHolder.SmsTitleCheckBox.setImageResource(R.drawable.checkbox_normal);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private void setGroupItemCheckedState(GroupItem groupItem){
        List<ChildrenItem> childrenItems = groupItem.getChildrenItems();
        if (childrenItems==null || childrenItems.isEmpty()) {
            groupCheckedStateMap.put(groupItem.getId(), 3);
            return;
        }

        int  checkedCount = 0;
        for (ChildrenItem childrenItem : childrenItems) {
            if (checkedChildren.contains(childrenItem.getId())) {
                checkedCount ++;
            }
        }
        int state = 1;
        if (checkedCount==0) {
            state = 3;
        }else if (checkedCount==childrenItems.size()) {
            state = 1;
        }else {
            state = 2;
        }
        groupCheckedStateMap.put(groupItem.getId(), state);
    }

    final static class GroupViewHolder {
        TextView SmsTitle;
        ImageView SmsTitleCheckBox;
        ImageView SmsTitleExpandIcon;
        LinearLayout SmsTitleCheckboxSpace;
    }

    final static class ChildViewHolder {
        TextView SmsContentTime;
        CheckBox SmsContentCheckbox;
        TextView SmsContent;
    }

    public class GroupCBLayoutOnClickListener implements View.OnClickListener {

        private GroupItem groupItem;

        public GroupCBLayoutOnClickListener(GroupItem groupItem){
            this.groupItem = groupItem;
        }

        @Override
        public void onClick(View v) {
            List<ChildrenItem> childrenItems = groupItem.getChildrenItems();
            if (childrenItems==null || childrenItems.isEmpty()) {
                groupCheckedStateMap.put(groupItem.getId(), 3);
                return;
            }
            int  checkedCount = 0;
            for (ChildrenItem childrenItem : childrenItems) {
                if (checkedChildren.contains(childrenItem.getId())) {
                    checkedCount ++;
                }
            }

            boolean checked = false;

            if (checkedCount==childrenItems.size()) {
                checked = false;
                groupCheckedStateMap.put(groupItem.getId(), 3);
            }else{
                checked = true;
                groupCheckedStateMap.put(groupItem.getId(), 1);
            }

            for (ChildrenItem childrenItem : childrenItems) {
                String holderKey = childrenItem.getId();
                if (checked) {
                    if (!checkedChildren.contains(holderKey)) {
                        checkedChildren.add(holderKey);
                    }
                }else {
                    checkedChildren.remove(holderKey);
                }
            }

            MCsSmsList.this.notifyDataSetChanged();
        }
    }

    public List<String> getCheckedRecords() {
        return checkedChildren;
    }

    public List<String> getCheckedChildren() {
        return checkedChildren;
    }

}
