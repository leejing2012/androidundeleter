package com.reneelab.DataModel;

import java.util.List;

/**
 * Created by Administrator on 2016/5/13.
 */
public class GroupItem {
    private String id;
    private String name;
    private List<ChildrenItem> childrenItems;


    public GroupItem() {
    }


    public GroupItem(String id,String name,List<ChildrenItem> childrenItems) {
        this.id = id;
        this.name = name;
        this.childrenItems = childrenItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<ChildrenItem> getChildrenItems() {
        return childrenItems;
    }


    public void setChildrenItems(List<ChildrenItem> childrenItems) {
        this.childrenItems = childrenItems;
    }
}
