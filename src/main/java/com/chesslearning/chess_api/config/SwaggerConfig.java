package com.chesslearning.chess_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("♟️ Chess Learning API")
                        .version("1.0.0")
                        .description("API for chess learning platform with AI-powered features")
                        .contact(new Contact()
                                .name("Chess Learning Team")
                                .email("Gabrielmorand@gmail.com")))
                
                
                .servers(List.of(
                        new Server()
                                .url("https://chess-api.up.railway.app")
                                .description("Production (Railway)"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development")
                ))
                
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}