package com.splider.utils;

import com.splider.feature.Fields;
import com.splider.store.PageCount;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XLSOperater {

    List<Map<String,String>> result=new Vector<>();
    Fields fields=new Fields();
    private WritableWorkbook globalwwk=null;
    private boolean flag=true;
    private String fileName;

    private static XLSOperater xlsWrite;

    public static XLSOperater getXlsWrite(){
        if(xlsWrite==null)
            xlsWrite=new XLSOperater();
        return xlsWrite;
    }
    public synchronized void output(PageCount count,boolean force){
        if(force||flag&&(count.getFailNum()+count.getSuccessNum() >= count.getAll())&&result.size()>0){
            Collections.sort(result, new Comparator<Map<String, String>>() {
                @Override
                public int compare(Map<String, String> o1, Map<String, String> o2) {
                    return o1.get("name").compareTo(o2.get("name"));
                }
            });
            SimpleDateFormat format=new SimpleDateFormat("YYYY-MM-dd HH-mm");
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
        if(PropertiesMgr.getInt("down.write.onTime",0)==1){
            try {
            if(fileName == null){
                flag = false;
                SimpleDateFormat format=new SimpleDateFormat("YYYY-MM-dd HH-mm");
                fileName=System.getProperty("user.dir")+"/"+format.format(new Date())+".xls";

                    globalwwk=Workbook.createWorkbook(new File(fileName));
                    WritableSheet sheet=globalwwk.createSheet("item",0);
                    String[] cols=fields.getCols();
                    int colSize=cols.length;
                    for(int i=0;i<colSize;i++){
                        Label label=new Label(i,0,cols[i]);
                        sheet.addCell(label);
                    }
                globalwwk.write();
             }
                WritableSheet sheet=globalwwk.getSheet(0);
                System.out.println("cols="+sheet.getColumns()+"::"+result.size());
                String[] cols = fields.getCols();
                for(int i=0;i<cols.length;i++){
                    sheet.addCell(new Label(i,result.size(),row.get(cols[i])));
                }
                globalwwk.write();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }

            }
        }
       private void insert(int i,Map<String,String> values){

       }

    public void close(){
        try {
            if(globalwwk == null)
                globalwwk.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
    }
