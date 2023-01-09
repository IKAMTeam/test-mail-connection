package com.onevizion.mailtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class StoreWrapper {
    private static final Logger log = LoggerFactory.getLogger(StoreWrapper.class);
    private final Store store;
    private final Credentials credentials;
    private final String token;

    public StoreWrapper(Credentials credentials, String token) {
        try {
            this.credentials = credentials;
            this.token = token;
            Properties properties = new Properties();
            properties.putAll(credentials.getMailProperties());
            properties.put("mail.store.protocol", "imap");
            properties.put("mail.imap.sasl.mechanisms", "XOAUTH2");
            properties.put("mail.imap.auth", "true");
            properties.put("mail.imap.auth.mechanisms", "XOAUTH2");
            properties.put("mail.imap.auth.plain.disable", "true");
            properties.put("mail.imap.ssl.enable", "true");
            Session session = Session.getInstance(properties);
            session.setDebug(credentials.isDebug());
            store = session.getStore("imap");
        } catch (NoSuchProviderException e) {
            log.error("Mail provider is not found", e);
            throw new RuntimeException(e);
        }
    }

    public void connect() {
        try {
            store.connect(credentials.getHost(), credentials.getPort(), credentials.getEmail(), token);
        } catch (MessagingException e) {
            log.error("Connection is not set", e);
        }
    }
}
