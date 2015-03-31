/*
 * 需要httpChint 4.0以上版本
 */
package com.tools.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 *
 * @author fuq
 */
public class HttpPost {

    private final Logger logger = Logger.getLogger(getClass());
    private static final String URL = "http://www.xiaohuangji.com/ajax.php";

    /**
     * 功能： 以post方式发送请求
     *
     * @param 无
     * @return 无
     * @throws 无
     */
    public String post(String url) {
        String statu = "error";
        // 核心应用类
        HttpClient httpClient = new DefaultHttpClient();

        // HTTP请求
        try {

            List formParams = new ArrayList();
            formParams.add(new BasicNameValuePair("para", "123"));

            HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
            HttpPost request = new HttpPost(url);
            request.setEntity(entity);
            // 发送请求，返回响应
            HttpResponse response = httpClient.execute(request);

            // 打印响应信息
            statu = response.getStatusLine().getReasonPhrase();
            System.out.println(response.getStatusLine());
            InputStream content = response.getEntity().getContent();
            BufferedReader br = new LineNumberReader(new InputStreamReader(content));
            StringBuilder sb = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            System.out.println(sb.toString());
        } catch (ClientProtocolException e) {
            // 协议错误
            logger.error("协议错误", e);
        } catch (IOException e) {
            // 网络异常
            logger.error("网络异常", e);
        }
        return statu;
    }

    public static void main(String[] args) {
        System.out.println(new HttpPost().post(URL));
    }
}
