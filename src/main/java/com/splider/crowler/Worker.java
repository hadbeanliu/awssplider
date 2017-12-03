package com.splider.crowler;

import com.splider.rule.CrawlType;
import com.splider.rule.Entity;
import com.splider.rule.UrlCrawlRule;
import com.splider.utils.HtmlParser;
import com.splider.utils.HtmlUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Worker implements Runnable{

    private UrlCrawlRule rule;

    public Worker(UrlCrawlRule rule) {
        this.rule = rule;
    }

    public void run() {
        sleep();
        try {
            Document doc = Jsoup.connect(rule.getUrl())
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();
            if(rule.getEntities()==null||rule.getEntities().size()==0){
                return ;
            }
            if(rule.getCrawlType() ==CrawlType.DETAIL){
                try {
                    extract(doc);
                    rule.setStatus(1);
                    HistoryManager.getInstance(null).add(rule.getUrl(),1);
                    return;
                }catch (Exception e){
                    e.printStackTrace();
                    rule.setStatus(2);
                    HistoryManager.getInstance(null).add(rule.getUrl(),2);
                    return;
                }
            }

            List<Entity> entities = rule.getEntities();

            for (Entity meta:entities){
                CrawlType type = meta.getType();
                switch (type){
                    case FLIP:{
                        System.out.println(1);
                        System.out.println("翻页："+doc.select(meta.getQuery()).attr(meta.getAttr()));
                        break;
                    }
                    case LIST:{
                        System.out.println("2");
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
                            HtmlUtils.getInstance().startCrawler(rule.getChildren());
                        }
                        break;
                    }
                    case CONTENT:{
                        System.out.println("3");
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
        }
        System.out.println("个数："+(rule.getChildren()==null ?0:rule.getChildren().size()));
        if(rule.getValues()!=null)
            System.out.println("val:"+rule.getValues());

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
        values.put("meta-key",doc.select("p.elHour span span").text());
        values.put("delivery","1");
        values.put("astk-code","0");
        values.put("condition","0");
//        values.put("headline",doc.select("div#itmrvw span").text());

        return values;
    }
    private Map<String,List<String>> getImgs(Document doc){
        Map<String,List<String>> imgs=new HashMap<>();
        imgs.put("mainImg",listToList(doc.select("div.elThumbnail ul li.elList"),"abs:href", Entity.ValueType.LIST));

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
                else imgUrls.add(e.attr("abs:href"));
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
            System.out.println(i+"-"+size);
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
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
