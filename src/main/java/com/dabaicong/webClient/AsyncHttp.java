package com.dabaicong.webClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * Description 对本文件的详细描述，原则上不能少于50字 .
 * <br> @author 刘冲
 * <br> @date 2018/7/20
 * <br> @version 1.0
 * <br> Remark 认为有必要的其他信息
 */
public class AsyncHttp {

    public static void main(String[] args) throws Exception {
        //simpleTest1();
        AsyncClientHttpExchangeFutureCallback();
    }

    private static void simpleTest1() {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        try {
            httpclient.start();
            HttpPost request = new HttpPost("http://www.baidu.com");
            Future<HttpResponse> future = httpclient.execute(request, null);
            HttpResponse response = future.get();
            System.out.println("Response: " + response.getStatusLine());
            System.out.println("Shutting down");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done");
    }

    public static void AsyncClientHttpExchangeFutureCallback() throws Exception {
        lotServerAsyncClient customClient = new lotServerAsyncClient();
        CloseableHttpAsyncClient httpclient = customClient.createAsyncClient(false);
        try {
            httpclient.start();
            final HttpGet[] requests = new HttpGet[]{
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301"),
                    new HttpGet("http://127.0.0.1:8080/lotserver/system/getCacheByKey?key=betWhiteList_20180619301")
            };
            final CountDownLatch latch = new CountDownLatch(requests.length);
            for (final HttpGet request : requests) {
                httpclient.execute(request, new FutureCallback<HttpResponse>() {

                    @Override
                    public void completed(final HttpResponse response) {
                        System.out.println("Thread Name :" + Thread.currentThread().getName());
                        latch.countDown();
                        System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
                    }

                    @Override
                    public void failed(final Exception ex) {
                        latch.countDown();
                        System.out.println(request.getRequestLine() + "->" + ex);
                    }

                    @Override
                    public void cancelled() {
                        latch.countDown();
                        System.out.println(request.getRequestLine() + " cancelled");
                    }

                });
            }
            latch.await();
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

}
