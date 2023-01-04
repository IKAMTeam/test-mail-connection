package com.onevizion.mailtest;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Properties;

@Getter
public class CredentialWrapper {
    private final String clientId;
    private final String clientSecret;
    private final String host;
    private final Integer port;
    private final String userName;
    private final String scope;
    private final String grantType;
    private final String tokenUri;
    private final String mailDebug;
    private final boolean isMailDebugAuth;
    private static final HashSet<String> keys;

    static {
        keys = new HashSet<>();
        keys.add(Credential.CLIENT_ID);
        keys.add(Credential.CLIENT_SECRET);
        keys.add(Credential.HOST);
        keys.add(Credential.PORT);
        keys.add(Credential.USER_NAME);
        keys.add(Credential.SCOPE);
        keys.add(Credential.GRANT_TYPE);
        keys.add(Credential.TOKEN_URI);
    }

    public CredentialWrapper(String path) {
        Properties props = getProperties(path);
        checkRequiredFields(props);
        clientId = props.getProperty(Credential.CLIENT_ID);
        clientSecret = props.getProperty(Credential.CLIENT_SECRET);
        host = props.getProperty(Credential.HOST);
        port = Integer.parseInt(props.getProperty(Credential.PORT));
        userName = props.getProperty(Credential.USER_NAME);
        scope = props.getProperty(Credential.SCOPE);
        grantType = props.getProperty(Credential.GRANT_TYPE);
        tokenUri = props.getProperty(Credential.TOKEN_URI);
        mailDebug = (String) props.getOrDefault(Credential.MAIL_DEBUG, Credential.MAIL_DEBUG_DEFAULT_VALUE);
        isMailDebugAuth = Boolean.parseBoolean((String) props.getOrDefault(Credential.MAIL_DEBUG_AUTH, Credential.MAIL_DEBUG_AUTH_DEFAULT_VALUE));
    }

    private static void checkRequiredFields(Properties props) {
        for (String key : keys) {
            if (!props.containsKey(key)) {
                throw new RequiredFieldNotFoundException(key);
            }
        }
    }

    public CredentialWrapper() throws IOException {
        this(Paths.get("").toAbsolutePath() + Credential.DEFAULT_PATH);
    }

    private static Properties getProperties(String path) {
        Properties prop = new Properties();
        try (InputStream inputStream = new FileInputStream(path)) {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop;
    }
}
