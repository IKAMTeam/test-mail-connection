package com.onevizion.mailtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class StoreWrapper {
    private static final Logger log = LoggerFactory.getLogger(StoreWrapper.class);
    private final Store store;
    private final Settings settings;
    private final String token;

    public StoreWrapper(Settings settings, String token) {
        try {
            this.settings = settings;
            this.token = token;
            java.util.Properties properties = new java.util.Properties();
            properties.put("mail.debug.auth", "true");
            properties.putAll(settings.getMailProperties());
            properties.put("mail.store.protocol", "imap");
            properties.put("mail.imap.sasl.mechanisms", "XOAUTH2");
            properties.put("mail.imap.auth", "true");
            properties.put("mail.imap.auth.mechanisms", "XOAUTH2");
            properties.put("mail.imap.auth.plain.disable", "true");
            properties.put("mail.imap.ssl.enable", "true");
            properties.put("mail.imap.auth.xoauth2.disable", "false");
            Session session = Session.getInstance(properties);
            session.setDebug(settings.isDebug());
            store = session.getStore("imap");
        } catch (NoSuchProviderException e) {
            log.error("Mail provider is not found", e);
            throw new RuntimeException(e);
        }
    }

    public void connect() {
        try {
            store.connect(settings.getHost(), settings.getPort(), settings.getEmail(), token);
        } catch (MessagingException e) {
            log.error("Connection is not set", e);
        }
    }
}
