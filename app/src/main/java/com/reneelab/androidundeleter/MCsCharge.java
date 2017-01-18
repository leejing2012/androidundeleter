package com.reneelab.androidundeleter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reneelab.DataModel.AlipayRepond;
import com.reneelab.DataModel.MCsXmlAnalysis;
import com.reneelab.thirdapi.WxRepond;
import com.reneelab.thirdapi.alipayApi;
import com.tencent.mm.sdk.openapi.IWXAPI;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MCsCharge extends Fragment {
    private Button chargebtn;
    private ArrayList<String> getPro;
    private int orderId;
    private int pay_type = 0;
    private LinearLayout getback;
    public alipayApi aprepond;
    private CheckBox apliy_checkbox,weixin_checkbox,bank_checkbox;

    public void onCreate(Bundle savedInstanceState) {
        getPro = getArguments().getStringArrayList("ProductType");
        aprepond = new alipayApi();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay, null);
        LinearLayout  probox = (LinearLayout)view.findViewById(R.id.productbox);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String one = "确认支付";
        builder.append(one);
        int start = builder.length();
        String center = "￥"+getPro.get(2);
        int end = start +   center.length();
        builder.append(center);
        builder.setSpan(new AbsoluteSizeSpan(10), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        apliy_checkbox = (CheckBox)view.findViewById(R.id.apliy_checkbox);
        apliy_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(pay_type == 1||pay_type == 0){
                    if(isChecked){
                        pay_type = 1;
                        weixin_checkbox.setClickable(false);
                        bank_checkbox.setClickable(false);
                    }else{
                        pay_type = 0;
                        weixin_checkbox.setClickable(true);
                        bank_checkbox.setClickable(true);
                    }
                }
            }
        });

        weixin_checkbox = (CheckBox)view.findViewById(R.id.weixin_checkbox);
        weixin_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(pay_type == 0||pay_type == 2){
                    if(isChecked){
                        pay_type = 2;
                        apliy_checkbox.setClickable(false);
                        bank_checkbox.setClickable(false);
                    }else{
                        pay_type = 0;
                        apliy_checkbox.setClickable(true);
                        bank_checkbox.setClickable(true);
                    }
                }
                System.err.println("-----------"+pay_type);
            }
        });

        bank_checkbox = (CheckBox)view.findViewById(R.id.bank_checkbox);
        bank_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(pay_type == 3|| pay_type == 0){
                    if(isChecked){
                        pay_type = 3;
                        apliy_checkbox.setClickable(false);
                        weixin_checkbox.setClickable(false);
                    }else{
                        pay_type = 0;
                        apliy_checkbox.setClickable(true);
                        weixin_checkbox.setClickable(true);
                    }
                }
                System.err.println("-----------"+pay_type);
            }
        });

        chargebtn = (Button)view.findViewById(R.id.recover);
        chargebtn.setText(builder);

        View view_pro = inflater.inflate(R.layout.selectedoptions,null);
        RelativeLayout rel_options = (RelativeLayout)view_pro.findViewById(R.id.relativeSelected);

        TextView pro_price = (TextView)view_pro.findViewById(R.id.proprice);
        TextView pro_name = (TextView)view_pro.findViewById(R.id.proname);
        orderId =  Integer.parseInt(getPro.get(0));
        pro_name.setText(getPro.get(1));
        pro_price.setText("￥" + getPro.get(2));

        RelativeLayout.LayoutParams s = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        s.setMargins(0, 10, 0, 0);
        probox.addView(view_pro, s);

        getback = (LinearLayout)view.findViewById(R.id.backarea);
        getback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        chargebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Toalipay();*/
               /* if(pay_type==1) Toalipay();
                if(pay_type==2) Toalipay();
                if(pay_type==3) Toalipay();*/
                /*if(pay_type==0)*/ /*Toast.makeText(getContext(),"请选择一种付款方式",Toast.LENGTH_SHORT);*/
                getWifi();

            }
        });

        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    /*支付宝支付功能*/
    private List<AlipayRepond> alip;

    public void Toalipay(){
        new Thread(){
            public void run(){
                MCsXmlAnalysis xmlget = new MCsXmlAnalysis();
                InputStream alipayRepond=  xmlget.GetUrl("http://www.reneelab.cn/mobileapi.php?cmd=GET_SIGNED_URI&proid=100&paygate=alipay&phone=18026882640");
                alip = xmlget.alipayReponds(alipayRepond);
                for(int i=0;i<alip.size();i++){
                  System.out.println(alip.get(0).getPartner()+"--"+alip.get(0).getSeller_id()+"--"+alip.get(0).getOut_trade_no()+"--"+alip.get(0).getSubject()+"--"+alip.get(0).getbody()+"--"+alip.get(0).getTotal_fee()+"--"+alip.get(0).getNotify_url()+"--"+alip.get(0).getSign()+"--"+alip.get(0).getSign_type());
                  aprepond.StartAlipay(alip.get(0).getPartner(), alip.get(0).getSeller_id(), alip.get(0).getOut_trade_no(), alip.get(0).getSubject(), alip.get(0).getbody(), alip.get(0).getTotal_fee(), alip.get(0).getNotify_url(), alip.get(0).getSign(), alip.get(0).getSign_type(),getActivity());
                }
            }
        }.start();
    }

    /*微信支付*/
    private IWXAPI api;
    InputStream urlstream = null;
    private List<WxRepond> wx_repond;
    Boolean lee = false;

    public void WxPay(){
       /* api = WXAPIFactory.createWXAPI(getContext(), WxConstants.APP_ID);
        api.registerApp(WxConstants.APP_ID);
        new Thread(){
            public void run(){
                String url = "http://www.reneelab.cn/mobileapi.php?cmd=GET_SIGNED_URI&proid=113&paygate=weixin&phone=18026882640&userid=wx123456&mackey=1231adfbad123fasf";
                final MCsXmlAnalysis wxXml = new MCsXmlAnalysis();
                urlstream = wxXml.GetUrl(url);
                wx_repond = wxXml.WxPayReponds(urlstream);
                for(int i=0;i<alip.size();i++){
                    PayReq req          = new PayReq();
                    req.appId			= WxConstants.APP_ID;
                    req.partnerId		= wx_repond.get(0).getMch_id();
                    req.prepayId		= wx_repond.get(0).getPrepay_id();
                    req.nonceStr		= wx_repond.get(0).getNonce_str();
                    req.timeStamp		= wx_repond.get(0).getTimestamp();
                    req.packageValue	= wx_repond.get(0).getPackageValue();
                    req.sign			= wx_repond.get(0).getSign();
                    lee = api.sendReq(req);
                    System.err.println("----处理结果签名---"+req.sign);
                }
                System.err.println("----处理结果---"+lee);
            }
        }.start();*/
    }

    private void getWifi(){
       /* SharedPreferences mySharedPreferences= getContext().getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("name", "Karl");
        editor.putString("habit", "sleep");
        editor.commit();*/
    }


}
