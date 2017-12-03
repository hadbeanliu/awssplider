package com.splider.feature;

import com.splider.rule.UrlCrawlRule;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Fields {

    private List<String> cols;
    private static String templateFilePath="/csv_yahoo_lefutur.xls";
//    private static

    public  Fields(){
        init();
    }

    private void init(){
        if(cols == null)
            cols= new ArrayList<String>();
//        try {
//            Workbook worker=Workbook.getWorkbook(fields.getClass().getResourceAsStream("/csv_yahoo_lefutur.xls"));
//            Sheet sheets=worker.getSheets()[0];
//            List<String> label =new ArrayList<String>();
//            int rows= 1;
//            int col=sheets.getColumns();
//            for(int i=0;i<col;i++){
//                System.out.println(sheets.getCell(i,0).getContents());
//            }
//            System.out.println(sheets.getRow(0).length);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (BiffException e) {
//            e.printStackTrace();
//        }
    }

    public void createXsl(String path, List<UrlCrawlRule> data){


    }


    public static void main(String[] args){

        Fields fields=new Fields();
        try {
            Workbook worker=Workbook.getWorkbook(fields.getClass().getResourceAsStream("/csv_yahoo_lefutur.xls"));
            Sheet template=worker.getSheets()[0];
            List<String> label =new ArrayList<String>();
            int rows= 1;
            int col=template.getColumns();
            System.out.println(template.getRow(0).length);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

    }


}
