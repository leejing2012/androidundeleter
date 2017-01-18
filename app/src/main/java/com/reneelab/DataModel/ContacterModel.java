package com.reneelab.DataModel;

/**
 * Created by Administrator on 2016/10/14.
 */
public class ContacterModel {
    private  String name;
    private String phone;
    private int flag = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
