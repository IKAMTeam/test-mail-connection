package com.onevizion.mailtest;

import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.io.IOException;


@Slf4j
public class Main {
    public static void main(String[] args) throws MessagingException, IOException {
        CredentialWrapper credentialWrapper = args.length > 0 ? new CredentialWrapper(args[0]) : new CredentialWrapper();
        Mail mail = new Mail(credentialWrapper);
        mail.connect();
    }
}
