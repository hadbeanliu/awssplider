package com.splider.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesMgr {

    private Properties props;
    private static PropertiesMgr mgr=new PropertiesMgr();

    private PropertiesMgr(){
        getProps();
    }

    private static PropertiesMgr instance(){

        return new PropertiesMgr();
    }
    public Properties getProps(){
        if(props==null){
            props=new Properties();
            try {
                props.load(getClass().getResourceAsStream("/default-config.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;
    }

    public static int getInt(String key,int defaultValue){
        String value= mgr.getProps().getProperty(key);
        return value ==null? defaultValue:Integer.parseInt(value);
    }

    public static String get(String key){
        return mgr.getProps().getProperty(key);
    }
    public static String get(String key,String defaultValue){
        return mgr.getProps().getProperty(key,defaultValue);
    }
    public static boolean save(){
//        mgr.getProps().store();
        return false;
    }
    public static void set(String key,String value){
        mgr.getProps().setProperty(key,value);
    }
    public static void main(String[] args){
        System.out.println(System.getProperty("user.dir"));
        String s=PropertiesMgr.get("current.dir",System.getProperty("user.dir"));

        System.out.println(s);
        PropertiesMgr.set("aaa","sss");

        System.out.println(PropertiesMgr.get("aaa"));

    }
}

