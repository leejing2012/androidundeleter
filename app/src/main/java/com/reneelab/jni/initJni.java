package com.reneelab.jni;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/8/26.
 */
public class initJni {
    private String strMountInfo = "/dev/block/mmcblk0p";
    private undeleteJni obj, sql;
    private String SdCardPath = null;
    private CallBack callback_action;
    private Context context_action;
    private int muti_file_choose = 0;

    private static class initJniInner {
        private static initJni instance = new initJni();
    }

    private initJni() {
    }

    public static initJni getInstance() {
        return initJniInner.instance;
    }

    public void initPermission(Context context){
        String datapath=null;
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;

        context_action = context;
        try {
            ai = pm.getApplicationInfo(context_action.getPackageName(), 0);
            datapath = ai.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        obj = new undeleteJni(callback_action,datapath);
        sql = obj;
        if(!getMountPath())
            getMountPathFromMount();
    }

    public void recover_type(Context context,CallBack action,int type,int mutiType){
        String datapath=null;
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        callback_action = action;
        context_action = context;
        try {
            ai = pm.getApplicationInfo(context_action.getPackageName(), 0);
            datapath = ai.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        obj = new undeleteJni(callback_action,datapath);
        sql = obj;
        if(!getMountPath())
            getMountPathFromMount();

        if(checkAccess(strMountInfo)) {
            obj.setDevice(strMountInfo);
            switch (type){
                case 1:
                    pic_recover(mutiType);
                    break;
                case 2:
                    sms_recover();
                    break;
                case 3:
                    calls_recover();
                    break;
                case 4:
                    contacter_recover();
                    break;

            }
        }
    }

    private void pic_recover(int a){
        System.err.println("----逻辑扫描参数----"+a);
        muti_file_choose = a;
        Thread mThread = new Thread(runnable_logic);
        mThread.start();
    }

    Runnable runnable_logic = new Runnable() {
        @Override
        public void run() {
            obj.LogicRecover(muti_file_choose);
            //obj.exScan(muti_file_choose);
        }
    };

    public void pic_deep_scan(int b){
        muti_file_choose = b;
        Thread mThread = new Thread(runnable_deep);
        mThread.start();
    }

    Runnable runnable_deep = new Runnable() {
        @Override
        public void run() {
            //obj.LogicRecover(muti_file_choose);
            obj.exScan(muti_file_choose);
        }
    };

    public int readbuffer(int no, long offset, int length, byte[] buf){
        System.err.println("-------read jni-----");
        int a = obj.read(no,offset,length, buf);
        return a;
    }



    private void sms_recover(){
        String dbPath="/data/data/com.android.providers.telephony/databases/mmssms.db";
        if(checkAccess(dbPath))
            sql.recover_sms();
    }

    private void calls_recover(){
        String callsPath="/data/data/com.android.providers.contacts/databases/contacts2.db";
        if(checkAccess(callsPath))
            obj.recover_calls();
    }

    private  void contacter_recover(){
        String callsPath="/data/data/com.android.providers.contacts/databases/contacts2.db";
        if(checkAccess(callsPath))
            obj.recover_contacts();
    }

    public void stopScan(){
        obj.stopScan(1);
    }

    public void suspendedScan(){
        obj.SetScanState(0);
    }

    public void ContinueScan(){
        obj.SetScanState(1);
    }

    /**
     * config the jni so
     * */
    private boolean getMountPath(){
        try
        {
            Runtime.getRuntime().exec("su");
        }catch(Throwable t)
        {
            t.printStackTrace();
        }
        File file=new File("/proc/partitions");
        try {
            if(!file.exists()||file.isDirectory())
                throw new FileNotFoundException();
            if(!file.canRead())
                return false;
            BufferedReader br=new BufferedReader(new FileReader(file));
            String temp=null;
            int blocks=0;
            while((temp=br.readLine())!=null){
                temp = temp.replaceAll("\\s{1,}", " ").trim();
                String[] buf = temp.split( "\\s" );
                if(buf.length<3){
                    continue;
                }
                if(buf[3].length()==7&&buf[3].startsWith("mmcblk"))
                    blocks = Integer.parseInt(buf[2]);
                else if(buf[3].length()>7&&buf[3].startsWith("mmcblk")) {
                    if(Integer.parseInt(buf[2])>blocks/2) {
                        strMountInfo = "/dev/block/" + buf[3];
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void  getMountPathFromMount() {
        try{
            Process process = null;
            DataOutputStream dos = null;
            InputStream dis = null;
            process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dis = process.getInputStream();
            BufferedReader buff = new BufferedReader(new InputStreamReader(dis));
            dos.writeBytes("mount" + " \n");
            dos.flush();
            String line = null;
            boolean found=false;
            while((line=buff.readLine())!=null) {
                String[] blocks = line.split( "\\s" );
                for ( int j = 0;
                      j < blocks.length;
                      j++ )
                {
                    if ( -1 != blocks[j].indexOf( "sdcard" )) {
                        if(j>0)
                            strMountInfo+=blocks[0].substring(blocks[0].lastIndexOf(':')+1);
                        found=true;
                        break;
                    }
                }
                if (found){
                    dos.writeBytes("chmod o+r "+strMountInfo + " \n");
                    dos.flush();
                    break;
                }
            }
            dis.close();
            dos.close();


            process.destroy();
            System.out.println("getMountPathFromMount success");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("getMountPathFromMount fail");
        }
    }

    private boolean checkAccess(String path){
        boolean ret = false;
        File file=new File(path);

        ret = CopyAssetsFile("setenforce0.sh", "/data/local");
        if(ret == false)
        {
           // Log.e(tag, "checkAccess : copy setenforce0.sh file fail");
        }
        ret = CopyAssetsFile("setenforce1.sh", "/data/local");
        if(ret == false)
        {
           // Log.e(tag, "checkAccess : copy setenforce1.sh file fail");
        }

        execShell("chmod 755 /data/local/setenforce*");
        execShell("sh /data/local/setenforce0.sh");

        if(!file.canRead())
            return getAccess(path);
        return file.canRead();
    }

    private void execShell(String cmd){
        try{
            Process p = Runtime.getRuntime().exec("su");
            OutputStream outputStream = p.getOutputStream();
            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        }
        catch(Throwable t)
        {
           // Log.e(tag, "execShell : exe cmd fail");
            t.printStackTrace();
        }
    }

    private Boolean CopyAssetsFile(String filename, String des) {
        Boolean isSuccess = true;
        AssetManager assetManager = context_action.getAssets();
        InputStream in = null;
        OutputStream out = null;

        File dir = new File(des);
        if (!dir.exists())
        {
            if (dir.mkdir())
            {
                return true;
            }
            else
                return false;
        }

        execShell("chmod -R 777 " + des);

        SdCardPath = getSDPath();
        try {
            in = assetManager.open(filename);
            String newFileName = "/sdcard" + "/" + filename;

            out = new FileOutputStream(newFileName);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            //Log.e(tag, "CopyAssetsFile : 2  ");
            String cmd = "cp " + newFileName + " " + des;
            execShell(cmd);
            //Log.e(tag, "CopyAssetsFile : 3  ");
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    private String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //ÅÐ¶Ïsd¿¨ÊÇ·ñ´æÔÚ
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//»ñÈ¡¸úÄ¿Â¼
        }
        return sdDir.toString();
    }

    private boolean getAccess(String path){
        try{
            Process process = null;
            DataOutputStream dos = null;
            InputStream dis = null;
            process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dis = process.getInputStream();
           // Log.w(tag, "getAccess: ---- 777 path is" + path);
            dos.writeBytes("chmod 777 "+path + " \n");
            dos.flush();
            dos.writeBytes("ls" + " \n");
            dos.flush();

            byte[] buf=new byte[1];
            dis.read(buf,0,1);

            process.destroy();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("getAccess fail");
            return false;
        }
        return true;
    }


}
