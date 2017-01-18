package com.reneelab.DataModel;

/**
 * Created by Administrator on 2016/10/20.
 */
public class documentModel {
    private String document_name;
    private long size;
    private int number;
    private boolean flag=false;

    public String getDocument_name() {
        return document_name;
    }

    public void setVideo_name(String video_name) {
        this.document_name = video_name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }
}
