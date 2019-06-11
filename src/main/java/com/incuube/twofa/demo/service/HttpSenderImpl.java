package com.incuube.twofa.demo.service;

import com.incuube.twofa.demo.utils.CommonConstants;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Log4j2
@Service
public class HttpSenderImpl implements ISender {

    @Value("${httpSender.phoneField.name}")
    private String phoneFieldName;

    @Value("${httpSender.messageField.name}")
    private String messageFieldName;

    @Value("${httpSender.url}")
    private String httpSenderUrl;

    @Override
    public void send(String phone, String message) throws IOException{

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(httpSenderUrl);
        String json = "{\""+phoneFieldName+"\":\""+phone+"\",\""+messageFieldName+"\":\""+phone+"\"}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        client.close();

    }
}
