package test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.splider.feature.Charts;
import com.splider.rule.Entity;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrawlerTest {


    public static void main(String[] args){
        //https://store.shopping.yahoo.co.jp/allhqfashion/a5dba1bca5.html
        String url ="https://zudele.1688.com/page/offerlist_87935940_26152260.htm?spm=a2615.7691481.0.0.1b3dbb2ecdWjxf";
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .timeout(7000)
                    .get();
            CrawlerTest test=new CrawlerTest();
            System.out.println(listToString(doc.select("ul.offer-list-row div.title a"),"href","\n", Entity.ValueType.LIST)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private Map<String,String> extract(Document doc){
        Map<String,String> values=new HashMap<String, String>();
//        values.put("path",listToString(doc.select("h1.d-title"),null,":", Entity.ValueType.LIST));
        values.put("name",doc.select("h1.d-title").text());
        values.put("sale-price", String.valueOf(Double.valueOf(values.getOrDefault("originprice","0"))+Double.valueOf(values.getOrDefault("transprice","0"))));
        StringBuffer options=new StringBuffer();
        if(doc.select("div.obj-content ul.list-leading li") !=null){
            options.append("カラー ");
            List<String> colors =listToList(doc.select("div.obj-content ul.list-leading li a.image"),"title", Entity.ValueType.LIST);
            if(colors.size() > 0){
                for (String str:colors){
                    System.out.println(str);
                    options.append(Charts.getCharts().translate(str)).append(" ");
                };
            }
            options.append("\n");
        }
        if(doc.select("div.obj-sku table.table-sku  td.name")!=null){
            String cata = doc.select("div.obj-sku div.obj-header").text();
            if(cata.equals("尺码"))
                options.append("サイズ ");
            else options.append(cata).append(" ");
            options.append(listToString(doc.select("div.obj-sku table.table-sku  td.name"),null," ", Entity.ValueType.LIST)).append("\n");
        }
        URL url =null;
        try {
            String baseUrl = doc.baseUri();
            String itemId = baseUrl.substring(baseUrl.indexOf("offer")+6,baseUrl.indexOf(".html"));
            url = new URL("https://laputa.1688.com/offer/ajax/widgetList.do?callback=jQuery&blocks=&data=offerdetail_ditto_title%2Cofferdetail_common_report%2Cofferdetail_ditto_serviceDesc%2Cofferdetail_ditto_preferential%2Cofferdetail_ditto_postage%2Cofferdetail_ditto_offerSatisfaction%2Cofferdetail_w1190_guarantee%2Cofferdetail_w1190_tradeWay%2Cofferdetail_w1190_samplePromotion&offerId="+itemId);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            StringWriter writer=new StringWriter();
            org.apache.commons.io.IOUtils.copy(in,writer,"GBK");
            String content = writer.toString();
            String json = content.substring(9,content.length()-1);
            JSONObject obj =new JSONObject(json);
            JSONObject priceObj = obj.getJSONObject("data").getJSONObject("data").getJSONObject("offerdetail_ditto_postage");
            double tranPrice = priceObj.getJSONArray("freightCost").getJSONObject(0).getDouble("totalCost");
            double perPrice = Double.parseDouble(priceObj.getString("price"));
            values.put("sale-price", String.valueOf(tranPrice+perPrice));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.put("options",options.toString().trim());
        values.put("caption",doc.select("div#CentItemCaption1 img").toString());

        values.put("explanation",doc.select("div#desc-lazyload-container").text());
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

    public List<String> listToList(Elements eles, String attr, Entity.ValueType valueType){

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
    public static String listToString(Elements eles, String attr, String speractor, Entity.ValueType valueType){

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
}
