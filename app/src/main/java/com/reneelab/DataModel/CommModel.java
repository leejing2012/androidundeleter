package com.reneelab.DataModel;

/**
 * Created by Administrator on 2016/10/20.
 */
public class CommModel {
    private long phone;
    private int duration;
    private String date;
    private boolean rcoverFlag;

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRcoverFlag() {
        return rcoverFlag;
    }

    public void setRcoverFlag(boolean rcoverFlag) {
        this.rcoverFlag = rcoverFlag;
    }
}
