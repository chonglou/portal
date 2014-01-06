package com.odong.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by flamen on 13-12-30上午5:48.
 */
@Component("core.httpClient")
public class HttpClient {
    public String get(String uri) {
        return call(new HttpGet(uri));
    }

    private String call(HttpUriRequest request) {

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request)) {
            //logger.debug("请求{} 返回状态{}", url, response.getStatusLine());
            HttpEntity entity = response.getEntity();
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            EntityUtils.consume(entity);

            String callback = sb.toString();
            logger.debug("返回内容{}", callback);
            return callback;

        } catch (IOException e) {
            logger.error("HTTP Client出错", e);
        }

        return null;
    }

    private String encode(String key, String plain) {
        BasicTextEncryptor te = new BasicTextEncryptor();
        te.setPassword(key);
        return te.encrypt(plain);
    }

    private final static Logger logger = LoggerFactory.getLogger(HttpClient.class);
}
