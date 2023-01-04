package com.onevizion.mailtest;

import lombok.extern.slf4j.Slf4j;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TokenClient {
    private final HttpPost httpPost;
    private final CredentialWrapper credentialWrapper;

    public TokenClient(CredentialWrapper credentialWrapper) throws UnsupportedEncodingException {
        this.credentialWrapper = credentialWrapper;
        httpPost = new HttpPost(credentialWrapper.getTokenUri());
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(Credential.CLIENT_ID, credentialWrapper.getClientId()));
        params.add(new BasicNameValuePair(Credential.CLIENT_SECRET, credentialWrapper.getClientSecret()));
        params.add(new BasicNameValuePair(Credential.SCOPE, credentialWrapper.getScope()));
        params.add(new BasicNameValuePair(Credential.GRANT_TYPE, credentialWrapper.getGrantType()));
        httpPost.setEntity(new UrlEncodedFormEntity(params, Credential.CHARSET));
    }

    public String getAccessToken() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String responseJson = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(responseJson);
                String token = jsonObject.getString(Credential.ACCESS_TOKEN);
                if (credentialWrapper.isMailDebugAuth()) {
                    log.info("secret = {}, token = {}", credentialWrapper.getClientSecret(), token);
                }
                return jsonObject.getString(Credential.ACCESS_TOKEN);
            }
        } catch (IOException exception) {
            throw new TokenException(exception.getMessage());
        }
    }
}
