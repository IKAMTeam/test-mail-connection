package com.onevizion.mailtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import static com.onevizion.mailtest.Credentials.Property.*;

public class Credentials {
    private static final Logger log = LoggerFactory.getLogger(Credentials.class);
    private final Properties properties;

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

    public Credentials(Properties properties) {
        this.properties = properties;
    }

    public static Credentials parseFromPath(String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            check(properties);
            return new Credentials(properties);
        } catch (IOException e) {
            log.error("Properties is not loaded", e);
            throw new RuntimeException(e);
        }
    }

    private static void check(Properties properties) {
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

    public Properties getMailProperties() {
        Properties mailProperties = new Properties();
        for (String name : properties.stringPropertyNames()) {
            if (name.indexOf(MAIL_PROPERTY_PREFIX) == 0) {
                mailProperties.put(name, properties.get(name));
            }
        }
        return mailProperties;
    }
}
