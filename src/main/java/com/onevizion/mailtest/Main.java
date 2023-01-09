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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.onevizion.mailtest.Credentials.Property.*;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String DEFAULT_PATH = Paths.get("").toAbsolutePath() + File.separator + "credentials.properties";

    public static void main(String[] args) {
        String path = args.length > 0 ? args[0] : DEFAULT_PATH;
        Credentials credentials = Credentials.parseFromPath(path);
        String token = getToken(credentials);
        StoreWrapper store = new StoreWrapper(credentials, token);
        store.connect();
        if (credentials.isDebug()) {
            log.info("secret = {}, token = {}", credentials.getClientSecret(), token);
        }
    }

    private static String getToken(Credentials credentials) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(credentials.getTokenUri());
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(CLIENT_ID, credentials.getClientId()));
            params.add(new BasicNameValuePair(CLIENT_SECRET, credentials.getClientSecret()));
            params.add(new BasicNameValuePair(SCOPE, credentials.getScope()));
            params.add(new BasicNameValuePair("grant_type", "client_credentials"));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(responseJson);
                return jsonObject.getString("access_token");
            }
        } catch (IOException e) {
            log.error("An access token is not received", e);
            throw new RuntimeException(e);
        }
    }
}
