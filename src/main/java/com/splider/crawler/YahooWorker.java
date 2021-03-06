package com.splider.crawler;

import com.splider.feature.Charts;
import com.splider.rule.CrawlType;
import com.splider.rule.Entity;
import com.splider.rule.UrlCrawlRule;
import com.splider.store.PageCount;
import com.splider.utils.PropertiesMgr;
import com.splider.utils.XLSOperater;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YahooWorker extends Worker{

    public static PageCount count =PageCount.getCount();

    public YahooWorker(UrlCrawlRule rule) {
        super(rule);
    }

    public void run() {
        sleep();
        try {
            Document doc = Jsoup.connect(rule.getUrl())
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .timeout(7000)
                    .get();
            if(rule.getCrawlType() ==CrawlType.DETAIL){
                try {
                    rule.setValues(extract(doc));

                    HistoryManager.getInstance(null).add(rule.getUrl(),1);
                    String prefix=rule.getValues().get("code");
                    String suffix = PropertiesMgr.get("img.suffix");
                    if(PropertiesMgr.getInt("download.file",0)==1){
                        StringBuffer caption=new StringBuffer();
                        Map<String,List<String>> imgs=getImgs(doc);
                        for(String k:imgs.keySet()){
                            String path=System.getProperty("user.dir")+"/img/"+k+"/";
                            List<String> imgList=imgs.get(k);
                            int i=0;
                            if(imgList ==null){
                                System.out.println(k+":"+rule.getUrl());
                            }else {
                                for (String url : imgList) {
                                    try {
                                        String index="";
                                        if(i > 0 ){
                                          index =   prefix + "_" + i + suffix;
                                        }else {
                                            index =   prefix + suffix;
                                        }
                                        downImages(path,index,url);
                                        if(k.equals("详情图"))
                                            caption.append("<img src=\"https://shopping.c.yimg.jp/lib/lefutur/").append(index).append("\"  width=\"100%\" alt = \"\"  style=\"margin-top:10px;\" >\n");
                                        i++;
                                    } catch (Exception e) {
                                        System.out.println(k+":no found:"+url+" from:"+rule.getUrl());
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        System.out.println(caption);
                        if(caption.length()>0)
                            rule.getValues().put("caption",caption.toString());
                    }
                    XLSOperater.getXlsWrite().add(rule.getValues());
                    rule.setStatus(1);
                    count.addSuccess();
                    System.out.println(count);
                    return;
                }catch (Exception e){
                    e.printStackTrace();
                    rule.setStatus(2);
                    count.addFail();
                    HistoryManager.getInstance(null).add(rule.getUrl(),2);
                    System.out.println(count);
                    return;
                }
            }

            changePage(doc);

        }catch(IOException e){
            e.printStackTrace();
            System.out.println(rule.getUrl());
            HistoryManager.getInstance(null).add(rule.getUrl(),2);
            if(rule.getCrawlType() == CrawlType.DETAIL){
                System.out.println(rule.getUrl());
                count.addFail();
            }
        }finally {
            XLSOperater.getXlsWrite().output(count,false);
        }

    }


    protected Map<String,String> extract(Document doc){
        Map<String,String> values=new HashMap<String, String>();
        values.put("path",listToString(doc.select("div#bclst li:gt(0) a span"),null," : ", Entity.ValueType.LIST));
        values.put("name",doc.select("div.mdItemInfoTitle h2").text());
        values.put("code",doc.select("div#abuserpt p:last-child").text().substring(6));
        values.put("sub-code","");
        values.put("price",doc.select("span.elNum").text().replace(",",""));
        values.put("sale-price",doc.select("span.elNum").text().replace(",",""));
        StringBuffer sb=new StringBuffer();
        Elements table = doc.select("div.elItem table");
        if(table.size()>0){

            String[] rowcol = table.select("caption").text().split("×");
            if(rowcol.length== 2){
                sb.append(rowcol[1]).append(" ").append(listToString(table.select("thead span"),null," ",Entity.ValueType.LIST)).append("\n\n");
                sb.append(rowcol[0]).append(" ").append(listToString(table.select("tbody th span"),null," ",Entity.ValueType.LIST)).append("\n\n");

            }else if(rowcol.length== 1){
                sb.append(rowcol[0]).append(" ").append(listToString(table.select("tbody th span"),null," ",Entity.ValueType.LIST));
                sb.append("\n\n");
            }
        }
//        for(Element e:doc.select("div#option select")){
//            sb.append(e.attr("name")).append(" ").append(listToString(e.children(),null," ",Entity.ValueType.LIST));
//            sb.append("\n\n\n");
////            System.out.println(sb.toString());
//        }
        values.put("options",sb.toString().trim());
        values.put("headline",doc.select("div.mdItemInfoCatch strong").text());
        values.put("abstract",doc.select("div.mdItemInfoTitle ul").text());
        values.put("explanation",doc.select("div.mdItemInfoLead").text());
//        values.put("relevant-links",listToString(doc.select("tr.ptData a"),"abs:href","\n", Entity.ValueType.LIST));
        values.put("meta-desc",doc.select("meta[name=keywords]").attr("content"));
        values.put("meta-key",values.get("meta-desc"));
        values.put("delivery","1");
        values.put("astk-code","0");
        values.put("condition","0");

        values.put("product-category", Charts.getCharts().get(values.get("path")));
//        values.put("headline",doc.select("div#itmrvw span").text());

        return values;
    }
    protected Map<String,List<String>> getImgs(Document doc){
        Map<String,List<String>> imgs=new HashMap<>();
//        List<String> urls=listToList(doc.select("div.elThumbnail ul li.elList a img"),"src", Entity.ValueType.LIST);
//        System.out.println(urls);
        imgs.put("主图",listToList(doc.select("div.elThumbnail ul li.elList a img"),"src", Entity.ValueType.LIST));
        imgs.put("详情图",listToList(doc.select("div#CenterTop img.lazy"),"data-original", Entity.ValueType.LIST));

        return  imgs;
    }

    public void downImages(String filePath,String name,String imgUrl) throws IOException {
        imgSleep();
        //图片url中的前面部分：例如"http://images.csdn.net/"
        String beforeUrl = imgUrl.substring(0,imgUrl.lastIndexOf("/")+1);
        //图片url中的后面部分：例如“20150529/PP6A7429_副本1.jpg”
        String fileName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
        //编码之后的fileName，空格会变成字符"+"
        String newFileName = URLEncoder.encode(fileName, "UTF-8");
        //把编码之后的fileName中的字符"+"，替换为UTF-8中的空格表示："%20"
        newFileName = newFileName.replaceAll("\\+", "\\%20");
        //编码之后的url
        imgUrl = beforeUrl + newFileName;


            //创建文件目录
            File files = new File(filePath);
            if (!files.exists()) {
                files.mkdirs();
            }
            //获取下载地址
        URL url = null;
        FileOutputStream out =null;
        InputStream is =null;
        try {
            url = new URL(imgUrl);
        //链接网络地址
            System.out.println("img:"+imgUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(30000);
            //获取链接的输出流
            is = connection.getInputStream();

            //创建文件，fileName为编码之前的文件名
            File file = new File(filePath + name);
            //根据输入流写入文件
            out = new FileOutputStream(file);
            int i = 0;
            while((i = is.read()) != -1){
                out.write(i);
            }
            out.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(out!=null)
                  out.close();
                if(is!=null)
                  is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args){
        String url ="https://51.photoup-pro.com/up/2520668352/20171202-30f5koe6s4w/if8dlvcqg0x4w.jpg";
//        try {
////            worker.downImages("/home/hadoop/Downloads","dd",url);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

    }
}
