package com.splider.utils;

import com.splider.crowler.CrawlTaskExecutor;
import com.splider.crowler.Worker;
import com.splider.rule.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    private Filters filter;

    public HtmlParser(){
        filter=new Filters();
    }

    public void parser(UrlCrawlRule rule){
        if(filter.filter(rule.getUrl())) {
            Worker task = new Worker(rule);
            CrawlTaskExecutor.getInstance().execute(task);
        } else {

        }
    }

    public void parser(List<UrlCrawlRule> listPages){
        for (UrlCrawlRule listPage:listPages)
            parser(listPage);
    }

    public static void main(String[] arg) throws IOException {
        UrlCrawlRule firstUrl=new UrlCrawlRule("https://store.shopping.yahoo.co.jp/allhqfashion/a5dba1bca5.html", CrawlType.LIST);
        HtmlParser parser=new HtmlParser();
        List<Entity> entities=new ArrayList<Entity>();
        entities.add(new Entity("","li.elNext a","abs:href",CrawlType.FLIP));
        entities.add(new Entity("","div#itmlst li dt a","abs:href",CrawlType.LIST));
//        entities.add(new Entity("catalog","div#TopSPathList1 li:last-child strong",null,CrawlType.CONTENT));
        firstUrl.setEntities(entities);
        parser.parser(firstUrl);

//        System.out.println(html);
// String start = html.indexOf("");
//        HtmlParser a = new HtmlParser("https://www.amazon.cn/b/?_encoding=UTF8&bbn=813108051&ie=UTF8&node=813109051&ref_=mh_cxrd_sub_813108051_1_image&pf_rd_p=4ca2cda6-16f6-4619-a60d-2dd2768f2edd&pf_rd_s=merchandised-search-3&pf_rd_t=101&pf_rd_i=813108051&pf_rd_m=A1AJ19PSB66TGU&pf_rd_r=JTZB34HF28SEVJ7GQJC8&pf_rd_r=JTZB34HF28SEVJ7GQJC8&pf_rd_p=4ca2cda6-16f6-4619-a60d-2dd2768f2edd");
//        ArrayList<String> hrefList = a.getHrefList();
//        for (int i = 0; i < hrefList.size(); i++)
//            System.out.println(hrefList.get(i));

    }

}
