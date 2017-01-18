package com.reneelab.DataModel;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/31.
 */
public class ScanProgressPar extends AsyncTask <Integer, Integer, String> {
    private TextView textView,down;
    private ProgressBar progressBar;
    private LinearLayout smshead;


    public ScanProgressPar(TextView textView, ProgressBar progressBar, LinearLayout smshead,TextView down) {
        super();
        this.progressBar = progressBar;
        this.textView = textView;
        this.smshead = smshead;
        this.down = down;
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
        System.err.println("---------p-----"+i);
        for (i = 10; i <= 100; i+=10) {
            System.err.println("---------p-in----"+i);
            netOperator.operator(100);
            publishProgress(i);
        }
        return i + params[0].intValue() + "";
    }



    /**
     * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
     */
    protected void onPostExecute(String result) {
        //textView.setText("异步操作执行结束" + result);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        down.setVisibility(View.GONE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)smshead.getLayoutParams();
        lp.weight = 6;
        smshead.setLayoutParams(lp);
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
        textView.setText(String.valueOf(values[0])+".00%");
    }
    protected void onCancelled() {
        textView.setText("100.00%");
        progressBar.setProgress(100);
    }

}
