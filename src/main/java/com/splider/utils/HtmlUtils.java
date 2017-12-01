package com.splider.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HtmlUtils {

    public static void main(String[] args){
        try {
            Document doc = Jsoup.connect("https://store.shopping.yahoo.co.jp/allhqfashion/yogawear0001.html")
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();
            System.out.println(doc.selectFirst("div#CentItemCaption1").select("img"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
