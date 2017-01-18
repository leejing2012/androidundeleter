package com.reneelab.DataModel;

/**
 * Created by Administrator on 2016/6/12.
 */
public class AlipayRepond {
    private String notify_url;
    private String seller_id;
    private int payment_type;
    private String out_trade_no;
    private String partner;
    private String service;
    private String subject;
    private float total_fee;
    private String sign;
    private String body;

    private String sign_type;



    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }



    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }


    public String getOut_trade_no() {
        return out_trade_no;
    }



    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }


    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }


    public float getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(float total_fee) {
        this.total_fee = total_fee;
    }


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }


    public String getbody(){
        return body;
    }

    public void setbody(String body){
        this.body = body;
    }



    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

}
