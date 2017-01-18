package com.reneelab.DataModel;

/**
 * Created by Administrator on 2016/10/31.
 */
public class NetOperator {
    public void operator(int a){
        try {
            //休眠1秒
            Thread.sleep(a);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
