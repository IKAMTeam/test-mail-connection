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

import static com.onevizion.mailtest.Property.*;

public class TokenClient {
    private static final Logger LOG = LoggerFactory.getLogger(TokenClient.class);
    private static final String TEMPLATE_ERROR_MESSAGE = "Access token response is failed. Status code = %s, reason = %s";
    private final HttpPost httpPost;
    private final boolean isDebug;
    private final String clientSecret;

    public TokenClient(Settings settings) {
        isDebug = settings.isDebug();
        clientSecret = settings.getClientSecret();
        httpPost = new HttpPost(settings.getTokenUri());
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(CLIENT_ID.getText(), settings.getClientId()));
        params.add(new BasicNameValuePair(CLIENT_SECRET.getText(), settings.getClientSecret()));
        params.add(new BasicNameValuePair(SCOPE.getText(), settings.getScope()));
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
    }

    private static String extractToken(CloseableHttpResponse response) {
        try {
            int code = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            if (HttpStatus.SC_OK == code) {
                return new JSONObject(body).getString("access_token");
            } else {
                throw new RuntimeException(String.format(TEMPLATE_ERROR_MESSAGE, code, body));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Response is not correct");
        }
    }

    public String getAccessToken() {
        try (var response = HttpClients.createDefault().execute(httpPost)) {
            String token = extractToken(response);

            return token;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("An access token is not received");
        }
    }
}
