package com.reneelab.jni;

/**
 * Created by Administrator on 2015/12/16.
 */
public class dir_file{
    public String name;
    public int cluster;
    public boolean isfolder;
    public dir_file(String name, int cluster, boolean isfolder){
        this.name=name;
        this.cluster=cluster;
        this.isfolder=isfolder;
    }
}
