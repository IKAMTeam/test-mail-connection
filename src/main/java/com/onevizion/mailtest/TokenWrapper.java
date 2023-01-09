package com.onevizion.mailtest;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenWrapper {
    public static String getToken(Properties properties) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(properties.getProperty(Credential.TOKEN_URI));
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(Credential.CLIENT_ID, properties.getProperty(Credential.CLIENT_ID)));
            params.add(new BasicNameValuePair(Credential.CLIENT_SECRET, properties.getProperty(Credential.CLIENT_SECRET)));
            params.add(new BasicNameValuePair(Credential.SCOPE, properties.getProperty(Credential.SCOPE)));
            params.add(new BasicNameValuePair(Credential.GRANT_TYPE, properties.getProperty(Credential.GRANT_TYPE)));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(responseJson);
                return jsonObject.getString(Credential.ACCESS_TOKEN);
            }
        } catch (IOException exception) {
            log.info("an access token was not received");
            throw new RuntimeException(exception.getMessage());
        }
    }
}
