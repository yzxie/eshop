package com.yzxie.study.eshoptest;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-31
 * Description:
 **/
public class TestSecKill {

    @Test
    public void testSeckill() {
        final CountDownLatch startUpLatch = new CountDownLatch(1);
        int concurrentLevel = 100;
        final CountDownLatch concurrentLatch = new CountDownLatch(concurrentLevel);

        final JSONObject params = new JSONObject();
        params.put("productId", 1);
        params.put("userId", "1");
        params.put("num", 1);
        params.put("price", 100.0);
        final String seckillUrl = "http://localhost:8081/seckill/create";

        for (int i = 0; i < concurrentLevel; i++) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        startUpLatch.await();
                        sendPostRequest(seckillUrl, params.toJSONString());
                        concurrentLatch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        startUpLatch.countDown();
        try {
            concurrentLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPostRequest(String url, String jsonParams) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(jsonParams, "UTF-8");
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
