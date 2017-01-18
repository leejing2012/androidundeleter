package com.reneelab.DataModel;

/**
 * Created by Administrator on 2016/5/13.
 */
public class ChildrenItem {
    private String id;
    private String name;
    private String sms_body;

    public ChildrenItem() {
    }


    public ChildrenItem(String id,String name,String sms_body) {
        this.id = id;
        this.name = name;
        this.sms_body = sms_body;
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

    public String getSms_body(){
        return sms_body;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSms_body(String sms_body) {
        this.sms_body = sms_body;
    }

}
