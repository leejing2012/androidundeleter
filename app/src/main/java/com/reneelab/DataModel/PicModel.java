package com.reneelab.DataModel;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/8.
 */
public class PicModel implements Serializable {
    private String pic_name;
    private long pic_size;
    private int pic_id;
    private String format;
    private int flag=0;

    public String getPic_name() {
        return pic_name;
    }

    public void setPic_name(String pic_name) {
        this.pic_name = pic_name;
    }

    public long getPic_size() {
        return pic_size;
    }

    public void setPic_size(long pic_size) {
        this.pic_size = pic_size;
    }

    public int getPic_id() {
        return pic_id;
    }

    public void setPic_id(int pic_id) {
        this.pic_id = pic_id;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
