package com.onevizion.mailtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.Set;

import static com.onevizion.mailtest.Property.*;

public class Settings {
    private static final Logger LOG = LoggerFactory.getLogger(Settings.class);
    private final Properties properties;
    private final static String MAIL_PROPERTY_PREFIX = "mail.";
    private static final Set<Property> REQUIRED_KEYS = Set.of(CLIENT_ID, CLIENT_SECRET, SCOPE, TOKEN_URI, HOST, PORT, EMAIL);

    public Settings(Properties properties) {
        this.properties = properties;
    }

    public static Settings parseFromPath(String path) {
        try (InputStream input = new FileInputStream(path)) {
            var properties = new Properties();
            properties.load(input);
            check(properties);
            return new Settings(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("File with properties by path '%s' is not found", path));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Properties by path '%s' are not loaded", path));
        }
    }

    private static void check(Properties properties) {
        for (Property property : REQUIRED_KEYS) {
            if (!properties.containsKey(property.getText())) {
                LOG.error("'{}' is a required property. The property is not found.", property.getText());
                throw new RuntimeException(property.getText() + " is not found");
            }
        }
    }

    public String getClientId() {
        return properties.getProperty(CLIENT_ID.getText());
    }

    public String getClientSecret() {
        return properties.getProperty(CLIENT_SECRET.getText());
    }

    public String getScope() {
        return properties.getProperty(SCOPE.getText());
    }

    public String getTokenUri() {
        return properties.getProperty(TOKEN_URI.getText());
    }

    public String getHost() {
        return properties.getProperty(HOST.getText());
    }

    public Integer getPort() {
        return Integer.parseInt(properties.getProperty(PORT.getText()));
    }

    public String getEmail() {
        return properties.getProperty(EMAIL.getText());
    }

    public boolean isDebug() {
        return Boolean.parseBoolean((String) properties.getOrDefault(IS_DEBUG.getText(), "false"));
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
