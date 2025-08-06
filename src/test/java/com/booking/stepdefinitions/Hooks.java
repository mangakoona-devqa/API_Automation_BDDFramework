package com.booking.stepdefinitions;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;

public class Hooks {

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void startWireMock() {
        wireMockServer = new WireMockServer(wireMockConfig().port(9090));
        wireMockServer.start();
        System.out.println("=== WireMock started on port 9090 ===");
    }

    @AfterAll
    public static void stopWireMock() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
            System.out.println("=== WireMock stopped ===");
        }
    }
}
