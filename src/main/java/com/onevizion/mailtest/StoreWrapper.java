package com.onevizion.mailtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Map;
import java.util.Properties;

public class StoreWrapper {
    private final Store store;
    private final Settings settings;
    private final String token;
    private static final Map<String, String> REQUIRED_MAIL_PROPERTIES = Map.of(
            "mail.store.protocol", "imap",
            "mail.imap.sasl.mechanisms", "XOAUTH2",
            "mail.imap.auth", "true",
            "mail.imap.auth.mechanisms", "XOAUTH2",
            "mail.imap.auth.plain.disable", "true",
            "mail.imap.ssl.enable", "true",
            "mail.imap.auth.xoauth2.disable", "false",
            "mail.debug.auth", "true"
    );

    public StoreWrapper(Settings settings, String token) {
        try {
            this.settings = settings;
            this.token = token;
            Properties properties = new Properties();
            properties.putAll(REQUIRED_MAIL_PROPERTIES);
            properties.putAll(settings.getMailProperties());
            Session session = Session.getInstance(properties);
            session.setDebug(settings.isDebug());
            store = session.getStore("imap");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("Mail provider is not found", e);
        }
    }

    public void connect() {
        try {
            store.connect(settings.getHost(), settings.getPort(), settings.getEmail(), token);
        } catch (MessagingException e) {
            throw new RuntimeException("Connection is not set", e);
        }
    }
}
