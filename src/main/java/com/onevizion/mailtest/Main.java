package com.onevizion.mailtest;

import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.io.File;
import java.util.Properties;

@Slf4j
public class Main {
    public static void main(String[] args) throws MessagingException {
        CredentialWrapper credential = args.length > 0 ? new CredentialWrapper(args[0]) : new CredentialWrapper();
        Properties properties = credential.getProperties();
        String token = TokenWrapper.getToken(properties);
        log(properties, token);
        MailWrapper mailWrapper = new MailWrapper(properties);
        mailWrapper.connect(token);
    }

    private static void log(Properties properties, String token) {
        boolean isMailDebugAuth = Boolean.parseBoolean((String) properties.getOrDefault(Credential.MAIL_DEBUG_AUTH, Credential.MAIL_DEBUG_AUTH_DEFAULT_VALUE));
        if (isMailDebugAuth) {
            log.info("secret = {}, token = {}", properties.getProperty(Credential.CLIENT_SECRET), token);
        }
    }
}
