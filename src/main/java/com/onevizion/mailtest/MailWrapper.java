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

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class MailWrapper {
    private final Store store;
    private final String host;
    private final Integer port;
    private final String userName;

    public MailWrapper(Properties properties) {
        host = properties.getProperty(Credential.HOST);
        port = Integer.parseInt(properties.getProperty(Credential.PORT));
        userName = properties.getProperty(Credential.USER_NAME);
        try {
            Properties mailProperties = new Properties();
            for (String name : properties.stringPropertyNames()) {
                if (name.indexOf(Credential.MAIL_PREFIX) == 0) {
                    mailProperties.put(name, properties.get(name));
                }
            }
            Session session = Session.getInstance(mailProperties);
            boolean isMailDebugAuth = Boolean.parseBoolean((String) properties.getOrDefault(Credential.MAIL_DEBUG, Credential.MAIL_DEBUG_DEFAULT_VALUE));
            session.setDebug(isMailDebugAuth);
            store = session.getStore(Credential.PROTOCOL);
        } catch (NoSuchProviderException e) {
            log.info("Mail provider was not found");
            throw new RuntimeException(e);
        }
    }

    public void connect(String token) throws MessagingException {
        store.connect(host, port, userName, token);
    }
}
