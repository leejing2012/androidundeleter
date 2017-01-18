package com.reneelab.jni;

public interface CallBack{   //相当于接口InA
    public void doSomething(String str);
    public void show_sms(long phone,String body,String date);
    public void show_comm(long phone,String date,int duration);
    public void ex_file(String out, int no, long size,String format);
    public void callback_getAccess(String path);
    public void callback_percent(int percent);
    public void show_contacter(String phone,String name);
}
