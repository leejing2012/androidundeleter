package com.reneelab.androidundeleter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.reneelab.thirdapi.AccessTokenKeeper;
import com.reneelab.thirdapi.WConstants;
import com.reneelab.thirdapi.qqLogin;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;

import java.text.SimpleDateFormat;

//import com.tencent.mm.sdk.openapi.SendAuth;

/**
 * Created by Administrator on 2016/5/19.
 */
public class MCsThirdLogin extends Fragment {
    private ImageView qq;
    private ImageView weixin;
    private ImageView weibo;
    private MCsEntry main;
    private qqLogin ql;
    private Tencent mt;
    private LinearLayout back;
    private Fragment base;
    public static IWXAPI api;
    private String WX_APP_ID ="wxb9c336e5158dea0d";


    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private AuthInfo mAuthInfo;
    private UsersAPI mUsersAPI;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.thirtylogin, null);
        back = (LinearLayout)view.findViewById(R.id.backarea);
        main = new MCsEntry();
        qq = (ImageView)view.findViewById(R.id.qq);
        weixin = (ImageView)view.findViewById(R.id.weixin);
        weibo = (ImageView)view.findViewById(R.id.weibo);
        ql=new qqLogin(getContext(),getActivity());
       //mt = Tencent.createInstance("1105381100",getContext());
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ql.onClickLogin();
            }
        });

        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 wxLoginApi();
            }
        });

        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthInfo = new AuthInfo(getContext(), WConstants.APP_KEY, WConstants.REDIRECT_URL, WConstants.SCOPE);
                mSsoHandler = new SsoHandler(getActivity(), mAuthInfo);
                mUsersAPI = new UsersAPI(getContext(), WConstants.APP_KEY, mAccessToken);
                mSsoHandler.authorize(new AuthListener());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base = new MCsContentFragment();
                 switchFragment(getFragmentManager().findFragmentById(getId()),base);
            }
        });

        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void wxLoginApi(){
      /*  api = WXAPIFactory.createWXAPI(getContext(), WX_APP_ID, true);
        api.registerApp(WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);*/
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void switchFragment(Fragment from,Fragment to) {
        if (getActivity() == null) {
            return;
        }
        if (getActivity() instanceof MCsEntry) {
            main = (MCsEntry) getActivity();
            main.switchConent(from,to);
        }
    }

    private class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String  phoneNum =  mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(getContext(), mAccessToken);
                Toast.makeText(getContext(),
                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
                long uid = Long.parseLong(mAccessToken.getUid());
                Toast.makeText(getContext(), "uid--"+uid, Toast.LENGTH_LONG).show();
                mUsersAPI.show(uid, mListener);
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(getContext(),
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getContext(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
        //mText.setText(String.format(format, mAccessToken.getToken(), date));
        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
      //  mText.setText(message);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        // ++++++++++++++++++
        //如果是sso授权登陆，必须先执行下面的回调，然后才会执行监听器里的 回调
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    Toast.makeText(getContext(),
                            "获取User信息成功，用户昵称：" + user.screen_name,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "2"+response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(getContext(), "1"+info.toString(), Toast.LENGTH_LONG).show();
        }
    };

}
