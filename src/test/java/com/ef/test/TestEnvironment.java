package com.ef.test;

import java.io.IOException;
import java.net.Socket;

import static org.assertj.core.api.Assumptions.assumeThat;

public class TestEnvironment {

    public static final String NO_DB = "nodb";
    public static final String WITH_DB = "withdb";

    public static void assumeMySqlIsUp() {
        assumeThat(isPortAvailable("127.0.0.1", 3306))
                .as("MySQL should be available to run this test")
                .isTrue();
    }

    private static boolean isPortAvailable(String host, int port) {
        try (Socket ignored = new Socket(host, port)) {
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}
