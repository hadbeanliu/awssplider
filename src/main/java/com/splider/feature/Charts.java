package com.splider.feature;

import com.splider.utils.PropertiesMgr;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Charts {

    private Map<String,String> tableMap=new HashMap<>();

    private Map<String,String> colorMap=new HashMap<>();

    private static Charts charts;

    public static Charts getCharts(){
        if(charts==null)
            charts=new Charts();

        return charts;
    }

    private Charts(){
        init();
    }

    public String translate(String str){
        return colorMap.getOrDefault(str,str);
    }
    private void init(){
        BufferedReader reader =null;
        try {
            Workbook worker=Workbook.getWorkbook(getClass().getResourceAsStream(PropertiesMgr.get("chart.dir")));
            Sheet sheet=worker.getSheets()[0];
            int count=0;
            Cell[] code= sheet.getColumn(0);
            Cell[] name= sheet.getColumn(2);
            int length = Math.min(code.length,name.length);
            for(int i=1;i<length;i++){
//                String[] categorys= name[i].getContents().split(">");
                tableMap.put(name[i].getContents().replaceAll(">",":").trim(),code[i].getContents());
                if(i == 100)
                    System.out.println(name[i].getContents().replaceAll(">",":").trim());
            }
            worker.close();

            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(PropertiesMgr.get("zh.ja.dir"))));
            String tmp =null;
            while ((tmp =reader.readLine())!=null) {
                String[] col = tmp.split(" ");
                colorMap.put(col[1],col[0]);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }finally {

        }
    }

    public String get(String key){
        return tableMap.get(key);
    }

    //https://store.shopping.yahoo.co.jp/allhqfashion/light0005.html
    //找不到
    public static void main(String[] args){
        Charts chart=Charts.getCharts();
//        " スポーツ >  アウトドア "
        System.out.println(chart.get("スマホ、タブレット、パソコン : スマホアクセサリー : スマホケース : iPhone6・6s ケース"));
    }

}
