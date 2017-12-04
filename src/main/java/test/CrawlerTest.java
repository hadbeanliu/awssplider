package test;

import com.splider.feature.Charts;
import com.splider.rule.Entity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CrawlerTest {

    public static void main(String[] args){
        //https://store.shopping.yahoo.co.jp/allhqfashion/a5dba1bca5.html
        String url ="https://store.shopping.yahoo.co.jp/allhqfashion/yoga0005.html";
        try {
            Document doc = Jsoup.connect(url)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();
            CrawlerTest test=new CrawlerTest();

            System.out.println(doc.select("div#bclst a span").last().text());
            System.out.println(Charts.getCharts().get(doc.select("div#bclst a span").last().text()));
//            for()

//            System.out.println(test.extract(doc).get("relevant-links"));
        } catch (IOException e) {
            e.printStackTrace();
        }


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
}
