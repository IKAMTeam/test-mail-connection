package com.onevizion.mailtest;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
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

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.onevizion.mailtest.Settings.Property.*;

public class TokenClient {
    private static final Logger log = LoggerFactory.getLogger(TokenClient.class);
    private final HttpPost httpPost;

    public TokenClient(Settings credentials) {
        httpPost = new HttpPost(credentials.getTokenUri());
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(CLIENT_ID, credentials.getClientId()));
        params.add(new BasicNameValuePair(CLIENT_SECRET, credentials.getClientSecret()));
        params.add(new BasicNameValuePair(SCOPE, credentials.getScope()));
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
    }

    private static String extractToken(CloseableHttpResponse response) throws IOException {
        StatusLine statusInfo = response.getStatusLine();
        if (HttpStatus.SC_OK == statusInfo.getStatusCode()) {
            String responseJson = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(responseJson);
            return jsonObject.getString("access_token");
        } else {
            String errorMessage = String.format("Status code = %s, reason = %s", statusInfo.getStatusCode(), statusInfo.getReasonPhrase());
            RuntimeException exception = new RuntimeException(errorMessage);
            log.error("Access token response is failed", exception);
            throw exception;
        }
    }

    public String getAccessToken() {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            return extractToken(response);
        } catch (IOException e) {
            log.error("An access token is not received", e);
            throw new RuntimeException(e);
        } finally {
            close(client);
            close(response);
        }
    }

    private static void close(Closeable object) {
        try {
            if (object != null) {
                object.close();
            }
        } catch (IOException e) {
            log.error("Cannot close object", e);
        }
    }

}
