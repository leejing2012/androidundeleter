package com.reneelab.DataModel;

/**
 * Created by Administrator on 2016/6/1.
 */
public class ProductType {
    private int proid;
    private String name;
    private float price;

    public int getId(){
        return proid;
    }

    public void setId(int id){
        this.proid = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public float getPrice(){
        return price;
    }

    public void setPrice(float price){
        this.price = price;
    }
}
