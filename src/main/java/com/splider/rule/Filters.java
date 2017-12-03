package com.splider.rule;

import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Filters{

    private List<Filter> filters;

    public Filters(){

        if(filters ==null){
            init();
        }

    }
    private void init(){
//        getClass().getResource("/filter.txt").openStream();
        BufferedReader reader=null;
        filters=new ArrayList<Filter>();
        try {
            reader=new BufferedReader(new InputStreamReader(getClass().getResource("/filter.txt").openStream()));
            String tmp =null;
            while((tmp=reader.readLine())!=null){
                tmp=tmp.trim();
                char first=tmp.charAt(0);

                switch (first){
                    case '#':break;
                    case '+':filters.add(new Filter(true,tmp.substring(1)));break;
                    case '-':filters.add(new Filter(false,tmp.substring(1)));break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean filter(String str) {
        for(Filter filter:filters){
            if(!filter.filter(str))
                return false;
        }

        return true;
    }

    public static void main(String[] args){
        Filters filters=new Filters();
        System.out.println(filters.filter("https://store.shopping.yahoo.co.jp/allhqfashion/sleepingbag0004.html"));

    }
}
