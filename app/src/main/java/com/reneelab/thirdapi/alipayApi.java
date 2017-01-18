package com.reneelab.thirdapi;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;

import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/6/12.
 */
public class alipayApi {
    public Activity payactivity;

    public void StartAlipay(String PARTNER,String SELLER,String out_trade_no,String subject,String body,float price,String notify_url,String sign,String sign_type,Activity activity){
        String orderInfo = getOrderInfo(PARTNER, SELLER, out_trade_no, subject, body, price, notify_url);
        try{
            String  sign1 = URLEncoder.encode(sign,"UTF-8");
            final String payInfo = orderInfo + "&sign=" + sign1 + "&" + "sign_type=RSA";
            System.err.println("==========="+payInfo);
            payactivity = activity;
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(payactivity);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo, true);
               /* Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);*/
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }catch (Exception ex){

        }




    }


    public String getOrderInfo(String PARTNER,String SELLER,String out_trade_no,String subject,String body,float price,String notify_url) {

        /*// 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
       // orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";*/
        /*String orderInfo = "_input_charset=\"utf-8\"";

        orderInfo += "&body=" + "\"" + body + "\"";

        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";

        orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";

        orderInfo += "&partner=" + "\"" + PARTNER + "\"";

        orderInfo += "&payment_type=\"1\"";

        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        orderInfo += "&service=\"mobile.securitypay.pay\"";

        orderInfo += "&subject=" + "\"" + subject + "\"";

        orderInfo += "&total_fee=" + "\"" + price + "\"";*/

        String orderInfo = "_input_charset=utf-8";

        orderInfo += "&body=" + body;

        orderInfo += "&notify_url=" + notify_url;

        orderInfo += "&out_trade_no=" + out_trade_no;

        orderInfo += "&partner="+ PARTNER;

        orderInfo += "&payment_type=1";

        orderInfo += "&seller_id=" + SELLER;

        orderInfo += "&service=mobile.securitypay.pay";

        orderInfo += "&subject="+ subject;

        orderInfo += "&total_fee=" + price;

        //orderInfo += "&it_b_pay=\"30m\"";



        return orderInfo;
    }

}
