package com.splider.rule;

public class Filter {

    private boolean flag;
    private String regex;

    public Filter(boolean flag, String regex) {
        this.flag = flag;
        this.regex = regex;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public boolean filter(String str){
        return !((str.indexOf(regex)!=-1)^flag);
    }
    public static void main(String[] args){



        Filter filter=new Filter(true,"1688.com");
        System.out.println(filter.filter("https://jiaoyanshu.1688.com/page/offerlist_6873430.htm?spm=a261y.7663282.0.0.74e4206cN5deNY"));

    }
}
