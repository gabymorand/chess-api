package com.chesslearning.chess_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MistralConfig {

    @Value("${mistral.api.url}")
    private String mistralApiUrl;

    @Value("${mistral.api.key}")
    private String mistralApiKey;

    @Bean
    public WebClient mistralWebClient() {
        return WebClient.builder()
                .baseUrl(mistralApiUrl)
                .defaultHeader("Authorization", "Bearer " + mistralApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}