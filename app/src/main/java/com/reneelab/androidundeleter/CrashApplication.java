package com.reneelab.androidundeleter;

import android.app.Application;
import android.util.Log;

import com.reneelab.androidundeleter.utils.CrashHandler;

/**
 * Created by Administrator on 2016/12/27.
 */
public class CrashApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("leejing","crash");
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
