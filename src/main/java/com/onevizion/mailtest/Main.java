package com.onevizion.mailtest;

import java.io.File;
import java.nio.file.Paths;

public class Main {
    private static final String DEFAULT_PATH = Paths.get("").toAbsolutePath() + File.separator + "connection.properties";

    public static void main(String[] args) {
        String path = args.length > 0 ? args[0] : DEFAULT_PATH;
        Settings settings = Settings.parseFromPath(path);
        String token = new TokenClient(settings).getAccessToken();
        StoreWrapper store = new StoreWrapper(settings, token);
        store.connect();
    }
}
