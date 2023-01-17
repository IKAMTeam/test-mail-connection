package com.onevizion.mailtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Set;

import static com.onevizion.mailtest.Settings.Property.*;

public class Settings {
    private static final Logger log = LoggerFactory.getLogger(Settings.class);
    private final java.util.Properties properties;

    private final static String MAIL_PROPERTY_PREFIX = "mail.";

    public interface Property {
        String CLIENT_ID = "client_id";
        String CLIENT_SECRET = "client_secret";
        String SCOPE = "scope";
        String TOKEN_URI = "token_uri";
        String HOST = "host";
        String PORT = "port";
        String EMAIL = "email";
        String IS_DEBUG = "mail.debug.auth";
    }

    private static final Set<String> REQUIRED_KEYS = Set.of(CLIENT_ID, CLIENT_SECRET, SCOPE, TOKEN_URI, HOST, PORT, EMAIL);

    public Settings(java.util.Properties properties) {
        this.properties = properties;
    }

    public static Settings parseFromPath(String path) {
        InputStream input = null;
        try {
            input = new FileInputStream(path);
            java.util.Properties properties = new java.util.Properties();
            properties.load(input);
            check(properties);
            return new Settings(properties);
        } catch (FileNotFoundException e) {
            log.error(String.format("File with properties '%s' is not found", path), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error(String.format("Properties by path '%s' are not loaded", path), e);
            throw new RuntimeException(e);
        } finally {
            close(input);
        }
    }

    private static void close(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            log.error("Cannot close input", e);
        }
    }

    private static void check(java.util.Properties properties) {
        for (String key : REQUIRED_KEYS) {
            if (!properties.containsKey(key)) {
                log.error("'{}' is a required property. The property is not found.", key);
                throw new RuntimeException(key + " is not found");
            }
        }
    }

    public String getClientId() {
        return properties.getProperty(CLIENT_ID);
    }

    public String getClientSecret() {
        return properties.getProperty(CLIENT_SECRET);
    }

    public String getScope() {
        return properties.getProperty(SCOPE);
    }

    public String getTokenUri() {
        return properties.getProperty(TOKEN_URI);
    }

    public String getHost() {
        return properties.getProperty(HOST);
    }

    public Integer getPort() {
        return Integer.parseInt(properties.getProperty(PORT));
    }

    public String getEmail() {
        return properties.getProperty(EMAIL);
    }

    public boolean isDebug() {
        return Boolean.parseBoolean((String) properties.getOrDefault(IS_DEBUG, "false"));
    }

    public java.util.Properties getMailProperties() {
        java.util.Properties mailProperties = new java.util.Properties();
        for (String name : properties.stringPropertyNames()) {
            if (name.indexOf(MAIL_PROPERTY_PREFIX) == 0) {
                mailProperties.put(name, properties.get(name));
            }
        }
        return mailProperties;
    }
}
