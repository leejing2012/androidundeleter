package com.reneelab.androidundeleter.wxapi;

import android.app.Activity;

/**
 * Created by Administrator on 2016/5/25.
 */
public class WXEntryActivity extends Activity/* implements IWXAPIEventHandler*/ {
    /*private Bundle bundle;
    public String str;
    private Thread newThread;
    // IWXAPI 是第三方app和微信通信的openapi接口
    private String WX_APP_ID ="wx4c01ca1627088be8";
    public TextView te;
    Handler mHandler;
    MCsEntry main;
    String strr;
    Activity wxactivity;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxactivity = this;
        main = new MCsEntry();
        //setContentView(R.layout.test);

        //te = (TextView)findViewById(R.id.uid);
        //te.setText("dsDS");
        MCsEntry.api.handleIntent(getIntent(), this);
        main = new MCsEntry();
        getBaseContext();

        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        //完成主界面更新,拿到数据
                        String data = (String)msg.obj;
                        //te.setText(data);
                        Toast.makeText(getBaseContext(),"ok",Toast.LENGTH_LONG);
                        break;
                    default:
                        break;
                }
            }

        };
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MCsEntry.api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResp(BaseResp resp) {
        if(resp instanceof SendAuth.Resp){
            SendAuth.Resp newResp = (SendAuth.Resp) resp;
            //获取微信传回的code
            String code = newResp.token;
            String pp = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx4c01ca1627088be8&secret=5638cdafa0e0b6bc6f76c574eb9fb9e7&code="+code+"&grant_type=authorization_code";
            getJson(pp);
        }
    }

    public void getJson(final String p){

        new Thread() {
            @Override
            public void run() {
                try {
                    str = readParse(p);
                    JSONObject obj = new JSONObject(str);
                    String uid_url="https://api.weixin.qq.com/sns/userinfo?access_token="+obj.get("access_token")+"&openid="+obj.get("openid");
                    getunionid(uid_url);
                    main.testuser(uid_url, wxactivity);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getunionid(final String p){
        new Thread() {
            @Override
            public void run() {
                try {
                    str = readParse(p);
                    JSONObject obj = new JSONObject(str);
                    mHandler.sendEmptyMessage(0);
                    Message msg =new Message();
                    String getJson = obj.get("unionid")+"--------------------"+obj.get("nickname");
                    //main.saveuser(response.getString("nickname"),obj.get("unionid"),activity);
                    //main.saveuser(obj.getString("nickname"), String.valueOf(obj.get("unionid")),wxactivity);

                    msg.obj = getJson;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }



    public static String readParse(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inStream = conn.getInputStream();
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
    }*/
}
