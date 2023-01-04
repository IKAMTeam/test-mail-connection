package com.onevizion.mailtest;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Mail {
    private final Properties properties = new Properties();
    private final CredentialWrapper credentialWrapper;

    public Mail(CredentialWrapper credentialWrapper) {
        try (InputStream inputStream = Main.class.getResourceAsStream(Credential.MAIL_PROPERTIES_PATH)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.credentialWrapper = credentialWrapper;
        properties.put(Credential.MAIL_DEBUG, credentialWrapper.getMailDebug());
    }

    public void connect() throws MessagingException, IOException {
        Store store = Session.getInstance(properties).getStore(Credential.PROTOCOL);
        store.connect(
                credentialWrapper.getHost(),
                credentialWrapper.getPort(),
                credentialWrapper.getUserName(),
                new TokenClient(credentialWrapper).getAccessToken()
        );
    }
}
