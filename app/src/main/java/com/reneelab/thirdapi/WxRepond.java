package com.reneelab.thirdapi;

/**
 * Created by Administrator on 2016/8/30.
 */
public class WxRepond {
    public String getNotify_url() {
        return notify_url;
    }
    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
    public String getOut_trade_no() {
        return out_trade_no;
    }
    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }
    public float getTotal_fee() {
        return total_fee;
    }
    public void setTotal_fee(float total_fee) {
        this.total_fee = total_fee;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getTrade_type() {
        return trade_type;
    }
    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
    public String getAppid() {
        return appid;
    }
    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getMch_id() {
        return mch_id;
    }
    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }
    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }
    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }
    public String getNonce_str() {
        return nonce_str;
    }
    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPrepay_id() {
        return prepay_id;
    }
    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }



    private String notify_url;
    private String out_trade_no;
    private float total_fee;
    private String body;

    private String prepay_id;

    private String trade_type;
    private String appid;
    private String mch_id;
    private String spbill_create_ip;
    private String nonce_str;
    private String sign;
    private String timestamp;
    private String packageValue;

    public String getPackageValue() {
        return packageValue;
    }
    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
