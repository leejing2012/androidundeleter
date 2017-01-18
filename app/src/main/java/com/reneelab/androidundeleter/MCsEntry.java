package com.reneelab.androidundeleter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.reneelab.jni.initJni;
import com.reneelab.thirdapi.qqLogin;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;

public class MCsEntry extends SlidingFragmentActivity {
    private Fragment mContent;
    public SlidingMenu sm;
   private qqLogin ql;
    public static IWXAPI api;
    private String WX_APP_ID ="wxb9c336e5158dea0d";
    public SharedPreferences mySharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ql = new qqLogin(getApplicationContext(),this);
        setContentView(R.layout.main_framework);
        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(R.layout.left_menu_framework);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu()
                    .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }

        if (mContent == null) {
            mContent = new MCsContentFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent).commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new MCsMenuFragment()).commit();


        sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeEnabled(false);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);

        sm.setBackgroundImage(R.drawable.menu_background);
        sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, -canvas.getWidth() / 2,
                        canvas.getHeight() / 2);
            }
        });

        sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (1 - percentOpen * 0.25);
                canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
            }
        });
        //setWlanMac();
        initJni.getInstance().initPermission(getApplicationContext());

    }

    public void changeMenu(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new MCsMenuFragment()).commit();
        sm.toggle();
    }

    public  void  closeSliding(){
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    public  void  openSliding(){
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }


    public void switchConent(Fragment from,Fragment to) {
        mContent = to;
        if(mContent.isAdded()){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.silde_in_left, R.anim.silde_out_right, R.anim.silde_in_left, R.anim.silde_out_right)
                    .hide(from)
                    .show( mContent)
                    .addToBackStack(null)
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.silde_in_left, R.anim.silde_out_right, R.anim.pop_enter, R.anim.pop_out_left)
                    .hide(from)
                    .add(R.id.content_frame, mContent)
                    .addToBackStack(from.getClass().getName())
                    .commit();
        }
        getSlidingMenu().showContent();
    }

    public void test(Fragment from,Fragment to,String menu){
        changeMenu();
        mContent = to;
        if(mContent.isAdded()){
            getSupportFragmentManager().beginTransaction().remove(mContent).commit();
        }
        /*switch (menu){
            case "about":
                mContent = new MCsAbout();
                break;
            case "login":
                mContent = new MCsThirdLogin();
                break;
        }*/

        getSupportFragmentManager().beginTransaction()
                .hide(from)
                .add(R.id.content_frame, mContent).commit();
        getSlidingMenu().showContent();
    }

     public void switchFunction(Fragment from,Fragment to) {
        mContent = to;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.silde_in_left, R.anim.silde_out_right, R.anim.pop_enter, R.anim.pop_out_left)
                        // .replace(R.id.content_frame, fragment)
                .hide(from)
                .add(R.id.content_frame, mContent)
                .addToBackStack(null).commit();
        getSlidingMenu().showContent();
    }

    public void saveuser(String name,String uid,Activity activity){
        mySharedPreferences= activity.getSharedPreferences("test", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("username", name);
        editor.putString("uid", uid);
        editor.commit();
    }

    public void testuser(String name,Activity activity){
        mySharedPreferences= activity.getSharedPreferences("test", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("lee", name);
       // editor.putString("uid", uid);
        editor.commit();
    }

    /*public void wxLoginApi(){
        api = WXAPIFactory.createWXAPI(getBaseContext(), WX_APP_ID, true);
        System.err.println("weixin----api---"+api);
        api.registerApp(WX_APP_ID);
        //SendAuth.Req req = new SendAuth.Req();
        //req.scope = "snsapi_userinfo";
        //req.state = "wechat_sdk_demo_test";
        //api.sendReq(req);
    }*/


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mContent.isAdded()){
            getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, ql.loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setWlanMac(){
        WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        String wlan_mac = wm.getConnectionInfo().getMacAddress();
        wlan_mac = wlan_mac.replaceAll(":","");
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String NativePhoneNumber=telephonyManager.getLine1Number();
        SharedPreferences mySharedPreferences= this.getSharedPreferences("UndeleteUser", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("PHONE", NativePhoneNumber);
        editor.putString("WLANMAC", wlan_mac);
        editor.commit();
    }

   public void onBackPressed() {
// TODO Auto-generated method stub
       // getFragmentManager().popBackStackImmediate("com.reneelab.androidundeleter.MCsContentFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//return ;
    }


}
