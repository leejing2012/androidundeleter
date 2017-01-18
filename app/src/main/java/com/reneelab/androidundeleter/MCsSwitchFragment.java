package com.reneelab.androidundeleter;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/9/8.
 */
public class MCsSwitchFragment {

    private Activity main_activity;
    private MCsEntry main;

    public  MCsSwitchFragment(Activity activity){
          this.main_activity = activity;
    }

    public void switchFragment(Fragment from, Fragment to) {
        if (main_activity == null) {
            return;
        }
        if (main_activity instanceof MCsEntry) {
            main = (MCsEntry) main_activity;
            main.switchConent(from,to);
        }
    }
}
