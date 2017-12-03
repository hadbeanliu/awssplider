//package com.splider.crowler;
//
//
//import org.apache.http.HttpEntity;
//import org.apache.http.ParseException;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//
//import java.io.IOException;
//
//public class HttpGetResource {
//
//
//    private static enum MethodSignal {
//        PUT, GET, POST
//    }
//
//    public String get(String uri) {
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        try {
//            //创建httpget
//            HttpGet httpget = new HttpGet(uri);
//            httpget.setHeader("Accept", "text/html, */*; q=0.01");
//            httpget.setHeader("Accept-Encoding", "gzip, deflate,sdch");
////            httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
//            httpget.setHeader("Connection", "keep-alive");
//            httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
//            System.out.println("executing request " + httpget.getURI());
//            //执行get请求
//            CloseableHttpResponse response = httpclient.execute(httpget);
//            try {
//                //获取响应实体
//                HttpEntity entity = response.getEntity();
//                //响应状态
//                System.out.println(response.getStatusLine());
//                if(entity != null) {
//                    //响应内容长度
////                    System.out.println("response length: " + entity.getContentLength());
//                    //响应内容
////                    System.out.println("response content: " + EntityUtils.toString(entity));
//                    String res = EntityUtils.toString(entity);
//                    return  res;
//                }
//            } finally {
//                response.close();
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            //关闭链接,释放资源
//            try {
//                httpclient.close();
//            } catch(IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    public static void main(String[] args){
//        HttpGetResource client=new HttpGetResource();
//        client.get("");
//    }
//}
