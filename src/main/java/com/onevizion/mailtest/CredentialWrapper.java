package com.onevizion.mailtest;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Properties;

@Slf4j
@Getter
public class CredentialWrapper {
    private static final HashSet<String> REQUIRED_KEYS;
    private final Properties properties;

    static {
        REQUIRED_KEYS = new HashSet<>();
        REQUIRED_KEYS.add(Credential.CLIENT_ID);
        REQUIRED_KEYS.add(Credential.CLIENT_SECRET);
        REQUIRED_KEYS.add(Credential.HOST);
        REQUIRED_KEYS.add(Credential.PORT);
        REQUIRED_KEYS.add(Credential.USER_NAME);
        REQUIRED_KEYS.add(Credential.SCOPE);
        REQUIRED_KEYS.add(Credential.GRANT_TYPE);
        REQUIRED_KEYS.add(Credential.TOKEN_URI);
    }

    public CredentialWrapper(String path) {
        properties = new Properties();
        try (InputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
            for (String key : REQUIRED_KEYS) {
                if (!properties.containsKey(key)) {
                    log.info("'{}' is a required property. The property was not found.", key);
                    throw new RequiredFieldNotFoundException(key);
                }
            }
        } catch (IOException e) {
            log.info("Properties was not loaded");
            throw new RuntimeException(e);
        }
    }

    public CredentialWrapper() {
        this(Paths.get("").toAbsolutePath() + File.separator + Credential.DEFAULT_PATH);
    }
}
