package com.splider.utils;

import com.splider.rule.ListPage;
import com.splider.rule.PageRule;
import com.splider.rule.UrlCrawlRule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class HtmlUtils {

    private HtmlParser parser =new HtmlParser();
    private static HtmlUtils htmlUtils=new HtmlUtils();
    public static void main(String[] args){
//        try {
//            Document doc = Jsoup.connect("https://store.shopping.yahoo.co.jp/allhqfashion/yogapants0002.html#")
//                    .data("query", "Java")
//                    .userAgent("Mozilla")
//                    .cookie("auth", "token")
//                    .timeout(3000)
//                    .get();
//            System.out.println(doc.select("div#TopSPathList1 a:gt(0)"));
//        }catch(IOException e){
//            e.printStackTrace();
//        }


    }
    private HtmlUtils(){}

    public static HtmlUtils getInstance(){
        return htmlUtils;
    }
    public void startCrawl(UrlCrawlRule rule){
        parser.parser(rule);
    }

    public void startCrawl(List<UrlCrawlRule> rules){
        parser.parser(rules);
    }

    private boolean check(PageRule pageRule){
        return true;
    }
}
