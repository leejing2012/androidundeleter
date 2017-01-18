package com.reneelab.DataModel;

import com.reneelab.thirdapi.WxRepond;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Administrator on 2016/6/1.
 */
public class MCsXmlAnalysis {
    InputStream is;

    public InputStream GetUrl(String URl){
        try {
            URL url = new URL(URl);
            HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5* 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) throw new RuntimeException("");
            is = conn.getInputStream();
            //analysisDatal(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return is;
    }

    public  List<ProductType> analysisDatal(InputStream in){
        List<ProductType> rivers = new ArrayList<ProductType>();
        DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(in);
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("PRODUCT");
            for (int i = 0; i < items.getLength(); i++) {
                ProductType ri = new ProductType();
                Element riNode = (Element) items.item(i);
                NodeList childsNodes = riNode.getChildNodes();
                for (int j = 0; j < childsNodes.getLength(); j++) {
                    Node node = (Node) childsNodes.item(j);
                    if(node.getNodeType() == Node.ELEMENT_NODE){
                        Element childNode = (Element) node;
                        if ("name".equals(childNode.getNodeName())) {
                            ri.setName(childNode.getFirstChild().getNodeValue());
                            System.err.println("name----"+childNode.getFirstChild().getNodeValue());
                        }else if ("price".equals(childNode.getNodeName())) {
                            ri.setPrice(new Float(childNode.getFirstChild().getNodeValue()));
                            System.err.println("price----"+new Float(childNode.getFirstChild().getNodeValue()));
                        }else if("proid".equals(childNode.getNodeName())){
                            ri.setId(new Integer(childNode.getFirstChild().getNodeValue()));
                            System.err.println("proid----"+new Integer(childNode.getFirstChild().getNodeValue()));
                        }
                    }
                }
                rivers.add(ri);
            }
              in.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return rivers;
    }

    /*支付宝恢复解析*/
    public  List<AlipayRepond> alipayReponds(InputStream in){
        List<AlipayRepond> alipayRepondList = new ArrayList<AlipayRepond>();
        DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(in);
            Element root = dom.getDocumentElement();
            AlipayRepond ali = new AlipayRepond();
            NodeList childsNodes = root.getChildNodes();
                for (int j = 0; j < childsNodes.getLength(); j++) {
                    Node node = (Node) childsNodes.item(j);
                    if(node.getNodeType() == Node.ELEMENT_NODE){
                        Element childNode = (Element) node;
                        if ("notify_url".equals(childNode.getNodeName())) {
                            ali.setNotify_url(childNode.getFirstChild().getNodeValue());
                            //System.err.println("notify_url----"+childNode.getFirstChild().getNodeValue());
                        }else if ("out_trade_no".equals(childNode.getNodeName())) {
                            ali.setOut_trade_no(childNode.getFirstChild().getNodeValue());
                            //System.err.println("out_trade_no----"+childNode.getFirstChild().getNodeValue());
                        }else if("partner".equals(childNode.getNodeName())){
                            ali.setPartner(childNode.getFirstChild().getNodeValue());
                            //System.err.println("partner----"+childNode.getFirstChild().getNodeValue());
                        }else if("payment_type".equals(childNode.getNodeName())){
                            ali.setPayment_type(new Integer(childNode.getFirstChild().getNodeValue()));
                            //System.err.println("payment_type----"+childNode.getFirstChild().getNodeValue());
                        }else if("seller_id".equals(childNode.getNodeName())){
                            ali.setSeller_id(childNode.getFirstChild().getNodeValue());
                            //System.err.println("seller_id----"+childNode.getFirstChild().getNodeValue());
                        }else if("service".equals(childNode.getNodeName())){
                            ali.setService(childNode.getFirstChild().getNodeValue());
                            //System.err.println("service----"+childNode.getFirstChild().getNodeValue());
                        }else if("subject".equals(childNode.getNodeName())){
                            ali.setSubject(childNode.getFirstChild().getNodeValue());
                            //System.err.println("subject----"+childNode.getFirstChild().getNodeValue());
                        }else if("total_fee".equals(childNode.getNodeName())){
                            ali.setTotal_fee(new Float(childNode.getFirstChild().getNodeValue()));
                           // System.err.println("total_fee----"+new Float(childNode.getFirstChild().getNodeValue()));
                        }else if("sign".equals(childNode.getNodeName())){
                            ali.setSign(childNode.getFirstChild().getNodeValue());
                           // System.err.println("sign----"+childNode.getFirstChild().getNodeValue());
                        }else if("sign_type".equals(childNode.getNodeName())){
                            ali.setSign_type(childNode.getFirstChild().getNodeValue());
                            //System.err.println("sign_type----"+childNode.getFirstChild().getNodeValue());
                        }else if("body".equals(childNode.getNodeName())){
                            ali.setbody(childNode.getFirstChild().getNodeValue());
                            //System.err.println("sign_type----"+childNode.getFirstChild().getNodeValue());
                        }
                    }
                }
                alipayRepondList.add(ali);
            in.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return alipayRepondList;
    }

    /*微信恢复解析*/
    public  List<WxRepond> WxPayReponds(InputStream in){
        List<WxRepond> alipayRepondList = new ArrayList<WxRepond>();
        DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(in);
            Element root = dom.getDocumentElement();
            WxRepond ali = new WxRepond();
            NodeList childsNodes = root.getChildNodes();
            for (int j = 0; j < childsNodes.getLength(); j++) {
                Node node = (Node) childsNodes.item(j);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element childNode = (Element) node;
                    if ("body".equals(childNode.getNodeName())) {
                        ali.setBody(childNode.getFirstChild().getNodeValue());
                        //System.err.println("notify_url----"+childNode.getFirstChild().getNodeValue());
                    }else if ("out_trade_no".equals(childNode.getNodeName())) {
                        ali.setOut_trade_no(childNode.getFirstChild().getNodeValue());
                        //System.err.println("out_trade_no----"+childNode.getFirstChild().getNodeValue());
                    }else if("total_fee".equals(childNode.getNodeName())){
                        ali.setTotal_fee(new Float(childNode.getFirstChild().getNodeValue()));
                        //System.err.println("partner----"+childNode.getFirstChild().getNodeValue());
                    }else if("notify_url".equals(childNode.getNodeName())){
                        ali.setNotify_url(childNode.getFirstChild().getNodeValue());
                        //System.err.println("payment_type----"+childNode.getFirstChild().getNodeValue());
                    }else if("trade_type".equals(childNode.getNodeName())){
                        ali.setTrade_type(childNode.getFirstChild().getNodeValue());
                        //System.err.println("seller_id----"+childNode.getFirstChild().getNodeValue());
                    }else if("appid".equals(childNode.getNodeName())){
                        ali.setAppid(childNode.getFirstChild().getNodeValue());
                        //System.err.println("service----"+childNode.getFirstChild().getNodeValue());
                    }else if("partnerid".equals(childNode.getNodeName())){
                        ali.setMch_id(childNode.getFirstChild().getNodeValue());
                        //System.err.println("subject----"+childNode.getFirstChild().getNodeValue());
                    }else if("spbill_create_ip".equals(childNode.getNodeName())){
                        ali.setSpbill_create_ip(childNode.getFirstChild().getNodeValue());
                        // System.err.println("total_fee----"+new Float(childNode.getFirstChild().getNodeValue()));
                    }else if("noncestr".equals(childNode.getNodeName())){
                        ali.setNonce_str(childNode.getFirstChild().getNodeValue());
                        // System.err.println("sign----"+childNode.getFirstChild().getNodeValue());
                    }else if("sign".equals(childNode.getNodeName())){
                        ali.setSign(childNode.getFirstChild().getNodeValue());
                        //System.err.println("sign_type----"+childNode.getFirstChild().getNodeValue());
                    }else if("prepayid".equals(childNode.getNodeName())){
                        ali.setPrepay_id(childNode.getFirstChild().getNodeValue());
                        //System.err.println("sign_type----"+childNode.getFirstChild().getNodeValue());
                    }else if("timestamp".equals(childNode.getNodeName())){
                        ali.setTimestamp(childNode.getFirstChild().getNodeValue());
                        //System.err.println("sign_type----"+childNode.getFirstChild().getNodeValue());
                    }else if("package".equals(childNode.getNodeName())){
                        ali.setPackageValue(childNode.getFirstChild().getNodeValue());
                        //System.err.println("sign_type----"+childNode.getFirstChild().getNodeValue());
                    }
                }
            }
            alipayRepondList.add(ali);
            in.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return alipayRepondList;
    }
}
