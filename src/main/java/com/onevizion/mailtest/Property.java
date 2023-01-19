package com.onevizion.mailtest;

public enum Property {
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    SCOPE("scope"),
    TOKEN_URI("token_uri"),
    HOST("host"),
    PORT("port"),
    EMAIL("email"),
    IS_DEBUG("mail.debug.auth");

    private final String property;

    Property(String property) {
        this.property = property;
    }

    public String getText() {
        return property;
    }
}
