package test;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleTest {

    public static void main(String[] args) throws IOException {

        String url="http://m.sbkk88.com/lizhishu/5fenzhonghemoshengrenchengweipengyou/322437.html";
        BufferedReader reader =new  BufferedReader(new InputStreamReader(System.in));
        String temp ="";
        while(temp!="exit"){
            Document doc = Jsoup.connect(url).get();
            for(Element e:doc.select("div.articleContent p")){
                System.out.println(e.text().replace("。",".\n").replaceAll("一 ",""));
            }
            temp =reader.readLine();

            if(temp.equals("up")){
                url = doc.select("a.prevPage").attr("abs:href");
            }else
              url = doc.select("a.nextPage").attr("abs:href");
        }
    }
}
