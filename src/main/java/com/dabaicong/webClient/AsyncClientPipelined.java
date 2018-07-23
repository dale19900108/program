package com.dabaicong.webClient;

/**
 * Description 对本文件的详细描述，原则上不能少于50字 .
 * <br> @author 刘冲
 * <br> @date 2018/7/20
 * <br> @version 1.0
 * <br> Remark 认为有必要的其他信息
 */

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpPipeliningClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

/**
 * This example demonstrates a pipelinfed execution of multiple HTTP request / response exchanges
 * Response content is buffered in memory for simplicity.
 */
public class AsyncClientPipelined {

    public static void main(final String[] args) throws Exception {
        CloseableHttpPipeliningClient httpclient = HttpAsyncClients.createPipelining();
        try {
            httpclient.start();

            HttpHost targetHost = new HttpHost("httpbin.org", 80);
            HttpGet[] resquests = {
                    new HttpGet("/"),
                    new HttpGet("/ip"),
                    new HttpGet("/headers"),
                    new HttpGet("/get")
            };

            Future<List<HttpResponse>> future = httpclient.execute(targetHost,
                    Arrays.asList(resquests), null);
            List<HttpResponse> responses = future.get();
            for (HttpResponse response : responses) {
                System.out.println(response.getStatusLine());
                byte[] buff = new byte[1024];
                StringBuffer result = new StringBuffer();
                while (response.getEntity().getContent().read(buff) != 0) {
                    result.append(buff);
                }
                System.out.println(result);
            }
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

}
