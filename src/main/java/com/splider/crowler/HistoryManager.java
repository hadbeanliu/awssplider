package com.splider.crowler;

import java.util.Hashtable;
import java.util.Map;

public class HistoryManager {

    private boolean forceCrawl=false;
    private Map<String,Integer> hisDate=new Hashtable<String, Integer>();
    private static HistoryManager hismgr = new HistoryManager();
    private static String DATA_PATH = "/default-config.properties";
    private HistoryManager(){}
    public static HistoryManager getInstance(String path) {

        return hismgr;
    }

    //0 表示没采集过，force表示强制采集
    //1 表示采集过
    //2 表示采集异常
    public synchronized boolean hasCrawl(String url){

        return !forceCrawl&&hisDate.getOrDefault(url,0)==1;
    }

    public synchronized void add(String url,int status){

        hisDate.put(url,status);
    }
}
