package com.splider.utils;

import com.splider.crowler.HttpGetResource;
import com.splider.rule.PageRule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser {
    /**
     * 要分析的网页
     */
    String htmlUrl;

    /**
     * 分析结果
     */
    ArrayList<String> hrefList = new ArrayList();

    /**
     * 网页编码方式
     */
    String charSet;

    public HtmlParser(String htmlUrl) {
        // TODO 自动生成的构造函数存根
        this.htmlUrl = htmlUrl;
    }

    /**
     * 获取分析结果
     *
     * @throws java.io.IOException
     */
    public ArrayList<String> getHrefList() throws IOException {

        parser();
        return hrefList;
    }

    /**
     * 解析网页链接
     *
     * @return
     * @throws IOException
     */
    private void parser() throws IOException {
        URL url = new URL(htmlUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);

        String contenttype = connection.getContentType();
        System.out.println(connection.getResponseMessage());
        charSet = getCharset(contenttype);
        System.out.println(charSet);
        InputStreamReader isr = new InputStreamReader(
                connection.getInputStream(), charSet);
        BufferedReader br = new BufferedReader(isr);

        String str = null, rs = null;
        while ((str = br.readLine()) != null) {
            rs = getHref(str);

            if (rs != null)
                hrefList.add(rs);
        }

    }

    /**
     * 获取网页编码方式
     *
     * @param str
     */
    private String getCharset(String str) {
        Pattern pattern = Pattern.compile("charset=.*");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find())
            return matcher.group(0).split("charset=")[1];
        return null;
    }

    /**
     * 从一行字符串中读取链接
     *
     * @return
     */
    private String getHref(String str) {
        Pattern pattern = Pattern.compile("<a href=.*</a>");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find())
            return matcher.group(0);
        return null;
    }

    public static void main(String[] arg) throws IOException {
        HttpGetResource client =new HttpGetResource();
        String html = client.get("https://www.amazon.cn/dp/B071RT9WR9/ref=cngwdyfloorv2_recs_0?pf_rd_p=05f2b7d6-37ec-49bf-8fcf-5d2fec23a061&pf_rd_s=desktop-2&pf_rd_t=36701&pf_rd_i=desktop&pf_rd_m=A1AJ19PSB66TGU&pf_rd_r=CY5J84045XPAS6TCVGW1&th=1");
//
        System.out.println(html);
        PageRule rule =new PageRule();
        rule.setStartStr("data[\"colorImages\"] = ");
        rule.setEndStr("data[\"heroImage\"]");
        int st = html.indexOf(rule.getStartStr());
        int end = html.indexOf(rule.getEndStr());
        System.out.println(st+"--"+end);
        System.out.println(html.substring(st,end));
//        System.out.println(html);
// String start = html.indexOf("");
//        HtmlParser a = new HtmlParser("https://www.amazon.cn/b/?_encoding=UTF8&bbn=813108051&ie=UTF8&node=813109051&ref_=mh_cxrd_sub_813108051_1_image&pf_rd_p=4ca2cda6-16f6-4619-a60d-2dd2768f2edd&pf_rd_s=merchandised-search-3&pf_rd_t=101&pf_rd_i=813108051&pf_rd_m=A1AJ19PSB66TGU&pf_rd_r=JTZB34HF28SEVJ7GQJC8&pf_rd_r=JTZB34HF28SEVJ7GQJC8&pf_rd_p=4ca2cda6-16f6-4619-a60d-2dd2768f2edd");
//        ArrayList<String> hrefList = a.getHrefList();
//        for (int i = 0; i < hrefList.size(); i++)
//            System.out.println(hrefList.get(i));

    }

}
