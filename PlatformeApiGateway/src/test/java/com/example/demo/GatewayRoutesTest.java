package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.junit.jupiter.api.Assertions;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class GatewayRoutesTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testLoginThroughGateway() {
        String jsonBody = """
            {
              "username": "testcompany",
              "password": "password123",
              "tenant": "testcompany"
            }
        """;

        webTestClient.post()
                .uri("/api/testcompany/auth/login")
                .header("Content-Type", "application/json")
                .bodyValue(jsonBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isNotEmpty()
                .jsonPath("$.message").value(msg ->
                        Assertions.assertTrue(msg.toString().contains("Login successful"))
                );
    }
}
