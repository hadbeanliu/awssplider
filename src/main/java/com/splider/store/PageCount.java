package com.splider.store;

public class PageCount {

    private static PageCount count =new PageCount();


    private PageCount(){}

    private int successNum;
    private int failNum;
    private int all;


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

    public void clear(){
        this.all=0;
        this.successNum=0;
        this.failNum=0;
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
