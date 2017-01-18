package com.reneelab.jni;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class undeleteJni {

    CallBack action = null;
    CallBackTask task = null;
    boolean b = false;
    public byte photo[];
    ArrayList<dir_file> dir_files;
    String m_applicationCachePath;
    String m_wechatDataPath;
    int siz = 0,s=0,p=0;

    static {
        System.loadLibrary("undelete");
    }

    public undeleteJni(CallBack action) {
        this.action = action;
        b = true;
        photo = new byte[4096];
    }

    public undeleteJni(CallBack action,String cachepath) {
        this.action = action;
        b = true;
        photo = new byte[4096];
        m_applicationCachePath = cachepath.concat("/cache/");
    }

    public void setApplicationCachePath(String cachepath){m_applicationCachePath=cachepath;}

//testdisk native
    public native void setDevice(String prompt);
    public native void getDir(int cluster);
    public native void exScan(int types);
    public native void LogicRecover(int types);
    public native int WeChatRecover(String data);
    public native void SetScanState(int state);
    public native int read(int no, long offset, int length, byte[] buf);
    public native void stopScan(int state);



    public void output_exScan(String name, String format, String path, String create_date, String modify_date, int type, int number, long size, int delete_flag) {
        System.out.println(name + "|" + size + "|" + format + "|" + path + "|" + create_date + "|" + modify_date + "|" + delete_flag + "|" + type);
        if (b)
            action.ex_file(name, number, size,format);
    }

    public void ScanPercentCallBack(int percent) {
        System.out.println("percent: " + percent);
        action.callback_percent(percent);
    }

    public void output_file(String name, int cluster, boolean isfolder) {
        dir_files.add(new dir_file(name, cluster, isfolder));
    }


    public void logicRec(CallBack action, int types) {
        this.action = action;
        b = true;
        LogicRecover(types);
    }

    public void scan(CallBack action, int types) {
        this.action = action;
        b = true;
        exScan(types);
    }

    public ArrayList<dir_file> get_dir(int cluster) {
        dir_files = new ArrayList<dir_file>();
        getDir(cluster);
        return dir_files;
    }


//sqlcipher native
    public native void recover_sms();
    public native void recover_calls();
    public native void recover_contacts();
    public native void recover_weChat(int uin,String imei);
    public native void recover_whatsapp();
    public native void recover_line();
    public native void recover_facebook();
    public native void recover_snapchat();

    public void RecoverContacts(CallBackTask task)
    {
        this.task=task;
        recover_contacts();
    }

    public void getAccess(String path){
        action.callback_getAccess(path);
    }

    public void callback_contacts(boolean deleteFlag,String name,String number,String  ret,byte[] photo){
        System.out.println("undeleteJni -- callback_contacts :" + number + ", deleteFlag = " + deleteFlag+"---name---"+name);
        action.show_contacter(number,name);
    }

    public void callback_calls(String name,long phone,long date,int type,int duration,int id)
    {
        System.out.println("JavaCallBackCalls:"+name+"|"+phone+"|"+date+"|"+type+"|"+duration +"|"+id);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(Long.parseLong(String.valueOf(date))));
        action.show_comm(phone,time,duration);
      /*  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(Long.parseLong(String.valueOf(date))));*/

    }

    public void callback_sms(long phone,long date,String body,int type,int id)  {
        System.out.println("JavaCallBackSMS:"+phone+"|"+date+"|"+body+"|"+type+"|"+id);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Long timeStamp = Long.parseLong(date);
        String time = sdf.format(new Date(Long.parseLong(String.valueOf(date))));   // 时间戳转换成时间
        action.show_sms(phone,body,time);
    }

    public void callback_whatsapp(int id,int type,String title,String sender,String data,long time) {
        //action.doSomething(data);
        System.out.println("JavaCallbackWhatsapp: "+id+" | "+type+" | "+title+" | "+sender+" | "+data+" | "+time);
    }
    public void callback_facebook(int id,int type,String title,String sender,String data,long time) {
        System.out.println("JavaCallbackFacebook: "+id+" | "+type+" | "+title+" | "+sender+" | "+data+" | "+time);
    }
    public void callback_line(int id,int type,String title,String sender,String data,long time) {
        action.doSomething(data);
        System.out.println("JavaCallbackFacebook: "+id+" | "+type+" | "+title+" | "+sender+" | "+data+" | "+time);
    }
}
