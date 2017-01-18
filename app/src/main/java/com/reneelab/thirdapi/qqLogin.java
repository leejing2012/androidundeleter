package com.reneelab.thirdapi;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.reneelab.androidundeleter.MCsEntry;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/25.
 */
public class qqLogin {
    public String mAppid = "1105381100";
    private static Tencent mTencent;
    private static boolean isServerSideLogin = false;
    private Context context;
    private Activity activity;
    private String lee;
    private MCsEntry main;
    public static String uid;
    private UserInfo mInfo;

    private IUiListener userInfoListener;
    private String scope;

    public qqLogin(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {}
                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
                @Override
                public void onCancel() {}
            };
            mInfo = new UserInfo(context, mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        } else {

        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                       // te.setText(response.getString("nickname"));
                        main.saveuser(response.getString("nickname"),uid,activity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what == 1){
            }
        }

    };

    public void onClickLogin() {

        mTencent = Tencent.createInstance(mAppid, context);
        main = new MCsEntry();
        if (!mTencent.isSessionValid()) {
            mTencent.login(activity, "all", loginListener);
            isServerSideLogin = false;
           // Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                mTencent.logout(context);
                mTencent.login(activity, "all", loginListener);
                isServerSideLogin = false;
                //Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            updateUserInfo();
            mTencent.logout(context);
        }
    }

    /*public void wxLogin(){
        main = new MCsEntry();
        main.wxLoginApi();
    }*/


    public static void initOpenidAndToken(JSONObject jsonObject) {
        System.err.println("initOpenidAndToken In");
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            System.err.println("openId In"+openId);
            uid = openId;
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    public  IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                qqUtil.showResultDialog(context, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
               qqUtil.showResultDialog(context, "返回为空", "登录失败");
                return;
            }
            try {
                lee= (String) jsonResponse.get("openid");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            qqUtil.showResultDialog(context, lee, "登录成功");

            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {

        }

        @Override
        public void onCancel() {

            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }


    }
}
