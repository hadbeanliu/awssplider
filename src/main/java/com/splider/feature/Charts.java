package com.splider.feature;

import com.splider.utils.PropertiesMgr;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Charts {

    private Map<String,String> tableMap=new HashMap<>();
    private static Charts charts;

    public static Charts getCharts(){
        if(charts==null)
            charts=new Charts();

        return charts;
    }

    private Charts(){
        init();
    }

    private void init(){
        try {
            Workbook worker=Workbook.getWorkbook(getClass().getResourceAsStream(PropertiesMgr.get("chart.dir")));
            Sheet sheet=worker.getSheets()[0];
            int count=0;
            Cell[] code= sheet.getColumn(0);
            Cell[] name= sheet.getColumn(2);
            int length = Math.min(code.length,name.length);
            for(int i=1;i<length;i++){
                String[] categorys= name[i].getContents().split(">");
                tableMap.put(categorys[categorys.length-1].trim(),code[i].getContents());
            }
            worker.close();
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
        System.out.println(chart.get("せっけん"));
    }

}
