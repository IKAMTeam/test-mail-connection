package com.onevizion.mailtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String DEFAULT_PATH = Paths.get("").toAbsolutePath() + File.separator + "connection.properties";

    public static void main(String[] args) {
        try {
            String path = args.length > 0 ? args[0] : DEFAULT_PATH;
            Settings settings = Settings.parseFromPath(path);
            String token = new TokenClient(settings).getAccessToken();
            StoreWrapper store = new StoreWrapper(settings, token);
            store.connect();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
