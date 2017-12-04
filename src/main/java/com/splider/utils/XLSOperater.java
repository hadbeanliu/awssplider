package com.splider.utils;

import com.splider.feature.Fields;
import com.splider.store.PageCount;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XLSOperater {

    List<Map<String,String>> result=new Vector<>();
    Fields fields=new Fields();
    private boolean flag=true;

    private static XLSOperater xlsWrite;

    public static XLSOperater getXlsWrite(){
        if(xlsWrite==null)
            xlsWrite=new XLSOperater();
        return xlsWrite;
    }
    public synchronized void output(PageCount count){
        if(flag&&(count.getFailNum()+count.getSuccessNum() >= count.getAll())&&result.size()>0){
            Collections.sort(result, new Comparator<Map<String, String>>() {
                @Override
                public int compare(Map<String, String> o1, Map<String, String> o2) {
                    return o1.get("path").compareTo(o2.get("path"));
                }
            });
            SimpleDateFormat format=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String path=System.getProperty("user.dir")+"/"+format.format(new Date())+".xls";
            WritableWorkbook wwk=null;
            try {
                wwk=Workbook.createWorkbook(new File(path));
                WritableSheet sheet=wwk.createSheet("item",0);
                String[] cols=fields.getCols();
                int colSize=cols.length;
                int rowSize=result.size();
                for(int i=0;i<colSize;i++){
                    Label label=new Label(i,0,cols[i]);
                    sheet.addCell(label);
                }
//                WritableCell cell=sheet.getWritableCell()
                for(int j=0;j<rowSize;j++){
                    Map<String,String> values=result.get(j);
                    for(int i=0;i<colSize;i++){
                        sheet.addCell(new Label(i,j+1,values.get(cols[i])));
                    }

                }
                wwk.write();
                flag=false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }finally {
                try {
                    wwk.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public synchronized void add(Map<String,String> row){

        result.add(row);
    }
}
