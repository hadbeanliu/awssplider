package com.splider.crowler;

import com.splider.feature.Charts;
import com.splider.rule.CrawlType;
import com.splider.rule.Entity;
import com.splider.rule.UrlCrawlRule;
import com.splider.store.PageCount;
import com.splider.utils.HtmlUtils;
import com.splider.utils.PropertiesMgr;
import com.splider.utils.XLSOperater;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Worker implements Runnable{

    public static PageCount count =PageCount.getCount();
    private UrlCrawlRule rule;

    public Worker(UrlCrawlRule rule) {
        this.rule = rule;
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
                    rule.setImgUrl(getImgs(doc));
                    HistoryManager.getInstance(null).add(rule.getUrl(),1);
                    String prefix=rule.getValues().get("code");
                    String suffix = PropertiesMgr.get("img.suffix");
                    XLSOperater.getXlsWrite().add(rule.getValues());
                    Map<String,List<String>> imgs=rule.getImgUrl();
                    for(String k:imgs.keySet()){
                        String path=System.getProperty("user.dir")+"/img/"+k+"/";
                        List<String> imgList=imgs.get(k);
                        int i=1;
                        if(imgList ==null){
                            System.out.println(k+":"+rule.getUrl());
                        }else {
                            for (String url : imgList) {
                                try {
                                    downImages(path, prefix + "-" + i + suffix, url);
                                    i++;
                                } catch (Exception e) {
                                    System.out.println(k+":no found:"+url+" from:"+rule.getUrl());
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
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
            if(rule.getEntities()==null||rule.getEntities().size()==0){
                return ;
            }
            List<Entity> entities = rule.getEntities();

            for (Entity meta:entities){
                CrawlType type = meta.getType();
                switch (type){
                    case FLIP:{
                        System.out.println("翻页："+doc.select(meta.getQuery()).attr(meta.getAttr()));
//                        HtmlUtils.getInstance().startCrawler();
                        String url =doc.select(meta.getQuery()).attr(meta.getAttr());
                        UrlCrawlRule newRule=new UrlCrawlRule(url,CrawlType.FLIP);
                        newRule.setEntities(rule.getEntities());
                        HtmlUtils.getInstance().startCrawl(newRule);
                        break;
                    }
                    case LIST:{
                        CrawlType newType=CrawlType.DETAIL;
                        String value = meta.getDefaultValue()==null?doc.select(meta.getQuery()).attr(meta.getAttr()):meta.getDefaultValue();
                        Elements elements=doc.select(meta.getQuery());
                        if(elements.size()!=0){
                            if(rule.getChildren() ==null){
                              List<UrlCrawlRule> children=new ArrayList<UrlCrawlRule>();
                              rule.setChildren(children);
                            }
                            for(Element ele:elements){
                                UrlCrawlRule child=new UrlCrawlRule(ele.attr(meta.getAttr()),newType);
                                rule.getChildren().add(child);
                            }
                            HtmlUtils.getInstance().startCrawl(rule.getChildren());
                            count.addAll(rule.getChildren().size());
                            System.out.println(count);
                        }
                        break;
                    }
                    case CONTENT:{
                        Map<String,String> values = rule.getValues()==null?new HashMap<String,String>():rule.getValues();
                        Elements eles =doc.select(meta.getQuery());
                        if(meta.getValueType() == Entity.ValueType.LIST){
                            StringBuffer sb=new StringBuffer();
                            int size = eles.size();
                            int i = 1;
                            for(Element e:eles){
                                sb.append(e.attr(meta.getAttr(),meta.getDefaultValue()));
                                if(i < size)
                                    sb.append(meta.getSeparator());
                            }
                            values.put(meta.getKey(),sb.toString());
                        }else {
                            if(meta.getAttr()==null) {
                                values.put(meta.getKey(), eles.text());
                            }else {
                            values.put(meta.getKey(), eles.attr(meta.getAttr()));
                           }
                        }
                        rule.setValues(values);
                        break;
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println(rule.getUrl());
            HistoryManager.getInstance(null).add(rule.getUrl(),2);
            if(rule.getCrawlType() == CrawlType.DETAIL){
                System.out.println(rule.getUrl());
                count.addFail();
            }
        }finally {
            XLSOperater.getXlsWrite().output(count);
        }

    }
    public UrlCrawlRule getRule() {
        return rule;
    }

    public void setRule(UrlCrawlRule rule) {
        this.rule = rule;
    }
    private Map<String,String> extract(Document doc){
        Map<String,String> values=new HashMap<String, String>();
        values.put("path",listToString(doc.select("div#TopSPathList1 li:gt(0) a"),null,":", Entity.ValueType.LIST));
        values.put("name",doc.select("div.mdItemInfoTitle h2").text());
        values.put("code",doc.select("div#abuserpt p:last-child").text().substring(6));
        values.put("sub-code","");
        values.put("price",doc.select("span.elNum").text().replace(",",""));
        values.put("sale-price",doc.select("span.elNum").text().replace(",",""));
        StringBuffer sb=new StringBuffer();
        for(Element e:doc.select("div#option select")){
            sb.append(e.attr("name")).append(" ").append(listToString(e.children(),null," ",Entity.ValueType.LIST));
            sb.append("\n\n\n");
//            System.out.println(sb.toString());
        }
        values.put("options",sb.toString().trim());
        values.put("headline",doc.select("div.mdItemInfoCatch strong").text());
        values.put("caption",doc.select("div#CentItemCaption1 img").toString());
        values.put("abstract",doc.select("div.mdItemInfoTitle ul").text());
        values.put("explanation",doc.select("div.mdItemInfoLead").text());
        values.put("relevant-links",listToString(doc.select("tr.ptData a"),"abs:href","\n", Entity.ValueType.LIST));
        values.put("meta-desc",doc.select("meta[name=keywords]").attr("content"));
        values.put("meta-key",values.get("meta-desc"));
        values.put("delivery","1");
        values.put("astk-code","0");
        values.put("condition","0");

        values.put("product-category", Charts.getCharts().get(doc.select("div#bclst a span").last().text()));
//        values.put("headline",doc.select("div#itmrvw span").text());

        return values;
    }
    private Map<String,List<String>> getImgs(Document doc){
        Map<String,List<String>> imgs=new HashMap<>();
//        List<String> urls=listToList(doc.select("div.elThumbnail ul li.elList a img"),"src", Entity.ValueType.LIST);
//        System.out.println(urls);
        imgs.put("主图",listToList(doc.select("div.elThumbnail ul li.elList a img"),"src", Entity.ValueType.LIST));
        imgs.put("详情图",listToList(doc.select("div#CentItemCaption1 img.lazy"),"data-original", Entity.ValueType.LIST));

        return  imgs;
    }
    private List<String> listToList(Elements eles, String attr,Entity.ValueType valueType){

        int size =eles.size();
        if(size!=0&&valueType == Entity.ValueType.LIST){
            List<String> imgUrls=new ArrayList<>();

            int i =0;
            for(Element e:eles){
                if(attr==null)
                   imgUrls.add(e.text());
                else imgUrls.add(e.attr(attr));
            }
            return imgUrls;
        }
        return null;
    }
    private String listToString(Elements eles, String attr, String speractor, Entity.ValueType valueType){

        if(valueType == Entity.ValueType.LIST){
            StringBuffer sb=new StringBuffer();
            int size = eles.size();
            int i = 1;
            for(Element e:eles){
                if(attr==null) {
                    sb.append(e.text());
                }else {
                    sb.append(e.attr(attr));
                }
                if(i < size)
                    sb.append(speractor);
                i++;
            }
            return sb.toString();
        }
        return null;
    }

    private void sleep(){
        try {
            Thread.sleep(Integer.parseInt(PropertiesMgr.get("min.sleep.time","1500")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void imgSleep(){
        try {
            Thread.sleep(Long.parseLong(PropertiesMgr.get("download.sleep.time","200")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            System.out.println("------------"+imgUrl);
        URL url = null;
        FileOutputStream out =null;
        InputStream is =null;
        try {
            url = new URL(imgUrl);

        //链接网络地址
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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
        Worker worker=new Worker(null);
//        try {
////            worker.downImages("/home/hadoop/Downloads","dd",url);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

    }
}
