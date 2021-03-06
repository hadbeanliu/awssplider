package com.splider.feature;

import com.splider.rule.UrlCrawlRule;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.util.List;

public class Fields implements Cloneable{

    private String[] cols;
    private static String templateFilePath="/csv_yahoo_lefutur.xls";
//    private static

    public  Fields(){
        init();
    }

    private void init(){

        try {
            Workbook worker=Workbook.getWorkbook(getClass().getResourceAsStream("/csv_yahoo_lefutur.xls"));
            Sheet template=worker.getSheets()[0];
            Cell[] tempCol=template.getRow(0);
            cols =new String[tempCol.length];
            for(int i=0;i<tempCol.length;i++){
                cols[i] =  tempCol[i].getContents();
            }
            worker.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }finally {

        }
    }

    public void createXsl(String path, List<UrlCrawlRule> data){


    }

    public String[] getCols() {
        return cols;
    }

    public void setCols(String[] cols) {
        this.cols = cols;
    }

    public static void main(String[] args){
        Fields f=new Fields();

        System.out.println(f.getCols().length);

    }


}
