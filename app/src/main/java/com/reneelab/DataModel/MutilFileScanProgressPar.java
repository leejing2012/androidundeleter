package com.reneelab.DataModel;

import android.os.AsyncTask;

import com.reneelab.androidundeleter.MCsRecoverFile;

/**
 * Created by Administrator on 2016/10/31.
 */
public class MutilFileScanProgressPar extends AsyncTask <Integer, Integer, String> {
    private BarProgross progressBar;
    private MCsRecoverFile mrf;


    public MutilFileScanProgressPar(BarProgross progressBar, MCsRecoverFile mrf) {
        super();
        this.progressBar = progressBar;
        this.mrf = mrf;
    }


    /**
     * 这里的Integer参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */
    protected String doInBackground(Integer... params) {
        NetOperator netOperator = new NetOperator();
        int i = 0;

        /*for (i = 10; i <= 100; i+=1) {
            netOperator.operator();
            publishProgress(i);
        }*/
        System.err.println("-------progress-----"+i);
        while (mrf.scanFinish()){
                i+=1;
               if(i<15){
                   netOperator.operator(1000);
                   publishProgress(i);
               }else if(i>15||i<50){
                   netOperator.operator(10000);
                   publishProgress(i);
               }else if(i>50||i<80){
                   netOperator.operator(1000);
                   publishProgress(i);
               }else if(i>80||i>98){
                   netOperator.operator(10000);
                   publishProgress(i);
               }else {

               }

        }
        publishProgress(100);
        i=0;
        return /*i + params[0].intValue() + ""*/"100";
    }



    /**
     * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
     */
    protected void onPostExecute(String result) {
        //textView.setText("异步操作执行结束" + result);
    }


    //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
    protected void onPreExecute() {
        //textView.setText("开始执行异步线程");
    }


    /**
     * 这里的Intege参数对应AsyncTask中的第二个参数
     * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
     */
    protected void onProgressUpdate(Integer... values) {
        int vlaue = values[0];
        progressBar.setProgress(vlaue);
    }
    protected void onCancelled() {
        progressBar.setProgress(100);
    }

}
