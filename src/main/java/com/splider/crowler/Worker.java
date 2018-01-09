package com.splider.crowler;

import com.splider.feature.Charts;
import com.splider.rule.CrawlType;
import com.splider.rule.Entity;
import com.splider.rule.UrlCrawlRule;
import com.splider.store.PageCount;
import com.splider.utils.HtmlUtils;
import com.splider.utils.PropertiesMgr;
import com.splider.utils.XLSOperater;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.IOUtils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Worker implements Runnable{

    public static PageCount count =PageCount.getCount();
    public static AtomicInteger atomicInteger=new AtomicInteger();
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
                    // extract importance message
                    rule.setValues(extract(doc));

                    HistoryManager.getInstance(null).add(rule.getUrl(),1);
                    String prefix=PropertiesMgr.get("prefix",rule.getValues().get("offerId"))+"-"+atomicInteger.getAndIncrement();
                    rule.getValues().put("code",prefix);
                    String suffix = PropertiesMgr.get("img.suffix");
                    if(PropertiesMgr.getInt("download.file",0)==1){
                        StringBuffer caption=new StringBuffer();
                        Map<String,List<String>> imgs=getImgs(doc,rule.getValues());
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
                        if(caption.length()>0){
                            rule.getValues().put("caption",caption.toString());
                            rule.getValues().put("sp-additional",caption.toString().replaceAll("https://shopping.c.yimg.jp/lib/lefutur",""));
                        }
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
            XLSOperater.getXlsWrite().output(count,false);
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
        values.put("name",doc.select("h1.d-title").text());
        values.put("url",doc.baseUri());
        StringBuffer options=new StringBuffer();
        if(doc.select("div.unit-detail-spec-operator").size()>0){
            options.append("カラー ");
            List<String> colors =listToList(doc.select("div.unit-detail-spec-operator"),"data-unit-config", Entity.ValueType.LIST);
            if(colors.size() > 0){
                for (String str:colors){
                    JSONObject obj = new JSONObject(str);
                    options.append(Charts.getCharts().translate(obj.getString("name"))).append(" ");
                }
            }
            options.append("\n\n");
        }
        if(doc.select("div.obj-sku table.table-sku  tr").size()>0){
            String cata = doc.select("div.obj-sku div.obj-header").text();
            if(cata.equals("尺码"))
                options.append("サイズ ");
            else if (cata.equals("颜色"))
                options.append("カラー ");
            else options.append(cata).append(" ");
            List<String> colors =listToList(doc.select("div.obj-sku table.table-sku  tr"),"data-sku-config", Entity.ValueType.LIST);
            if(colors.size() > 0){
                for (String str:colors){
                    JSONObject obj = new JSONObject(str);
                    options.append(Charts.getCharts().translate(obj.getString("skuName"))).append(" ");
                }
            }
            options.append("\n\n");
        }
        URL url =null;
        try {
            String baseUrl = doc.baseUri();
            String itemId = baseUrl.substring(baseUrl.indexOf("offer")+6,baseUrl.indexOf(".html"));
            values.put("offerId",itemId);
            url = new URL("https://laputa.1688.com/offer/ajax/widgetList.do?callback=jQuery&blocks=&data=offerdetail_ditto_title%2Cofferdetail_common_report%2Cofferdetail_ditto_serviceDesc%2Cofferdetail_ditto_preferential%2Cofferdetail_ditto_postage%2Cofferdetail_ditto_offerSatisfaction%2Cofferdetail_w1190_guarantee%2Cofferdetail_w1190_tradeWay%2Cofferdetail_w1190_samplePromotion&offerId="+itemId);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
//            StringWriter writer=new StringWriter();
//            org.apache.commons.io.IOUtils.copy(in,writer,"GBK");
//            IOUtils.readFully(in,-1,false);
            String content = new String(IOUtils.readFully(in,-1,false));
            String json = content.substring(9,content.length()-1);
            JSONObject obj =new JSONObject(json);
            JSONObject priceObj = obj.getJSONObject("data").getJSONObject("data").getJSONObject("offerdetail_ditto_postage");
            double tranPrice = 0;
            if(priceObj.has("freightCost")){
                tranPrice = priceObj.getJSONArray("freightCost").getJSONObject(0).getDouble("totalCost");
            }
            double perPrice = Double.parseDouble(priceObj.getString("price"));
            double total = tranPrice + perPrice;
            if( 100<total && total<=500){
                total = total*2.5*17;
            }else if(total <= 100){
                total = total*3*17;
            }else {
                total = total *2*17;
            }
            values.put("price", String.valueOf(total));
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.put("options",options.toString().trim());
//        values.put("caption",doc.select("div#CentItemCaption1 img").toString());
        values.put("explanation",listToString(doc.select("div#mod-detail-attributes table tr"),null,"\n", Entity.ValueType.LIST));
        values.put("relevant-links",listToString(doc.select("tr.ptData a"),"abs:href","\n", Entity.ValueType.LIST));
        values.put("meta-key",doc.select("meta[name=keywords]").attr("content"));
        values.put("meta-desc",values.get("meta-key"));

//        values.put("meta-key",doc.select("p.elHour span span").text());
        values.put("delivery","1");
        values.put("taxable","1");
        values.put("point-code","5");
        values.put("astk-code","0");
        values.put("condition","0");


//        values.put("headline",doc.select("div#itmrvw span").text());


        return values;
    }
    private Map<String,List<String>> getImgs(Document doc,Map<String,String> values){
        Map<String,List<String>> imgs=new HashMap<>();
//        values.put("explanation",doc.select("div#desc-lazyload-container").text());

        List<String> mainImg = listToList(doc.select("li.tab-trigger"),"data-imgs", Entity.ValueType.LIST);
        mainImg = mainImg.stream().map(url -> {
            JSONObject obj = new JSONObject(url);
            return obj.getString("original");
        }).collect(Collectors.toList());
        imgs.put("主图",mainImg);
        String deTailUrl = doc.select("div#desc-lazyload-container").attr("data-tfs-url");
        try {
            Document detail = Jsoup.connect(deTailUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .timeout(7000)
                    .get();
            List<String> detailImg = listToList(detail.select("img"),"src", Entity.ValueType.LIST).stream().map(url -> url.replace("\\\"","").replace("\"","")).collect(Collectors.toList());
            imgs.put("详情图",detailImg);
            String explanation = detail.text();
            if(explanation.length() == 34){
                explanation = "";
            }else {
                explanation = explanation.substring(explanation.indexOf("=")+13,explanation.length()-2);
            }
            values.put("explanation",values.getOrDefault("explanation","")+"\n"+explanation);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return  imgs;
    }
    private List<String> listToList(Elements eles, String attr,Entity.ValueType valueType){

        int size =eles.size();
        if(size!=0&&valueType == Entity.ValueType.LIST){
            List<String> imgUrls=new ArrayList<>();
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
        URL url = null;
        FileOutputStream out =null;
        InputStream is =null;
        try {
            url = new URL(imgUrl);
        //链接网络地址
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
            System.out.println(filePath+"/"+name+"----"+imgUrl);
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
