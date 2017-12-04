package com.splider.store;

import java.util.List;

public class PageCount {

    private static PageCount count =new PageCount();

    private PageCount(){}

    private int successNum;
    private int failNum;
    private int all;


    private int fileSuccessNum;
    private int fileFailNum;
    private int fileAll;





    public static PageCount getCount(){
        return count;
    }

    public synchronized void addSuccess(){
        this.successNum++;
    }

    public synchronized void addFail(){
        this.failNum++;
    }

    public synchronized void addAll(int num){
        this.all += num;
    }
    public int getSuccessNum() {
        return successNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public int getAll() {
        return all;
    }


    @Override
    public String toString() {
        return "PageCount{" +
                "successNum=" + successNum +
                ", failNum=" + failNum +
                ", all=" + all +
                '}';
    }
}
