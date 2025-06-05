package com.chesslearning.chess_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Retourne la vue index.html
    }

    @GetMapping("/api")
    @ResponseBody
    public Map<String, Object> apiInfo() {
        return Map.of(
            "message", "🏆 Chess API - Projet étudiant terminé !",
            "status", "✅ Online",
            "swagger", "/swagger-ui.html",
            "docs", "/v3/api-docs",
            "health", "/actuator/health",
            "endpoints", Map.of(
                "auth", "/api/auth/register, /api/auth/login",
                "users", "/api/users",
                "games", "/api/games", 
                "tournaments", "/api/tournaments",
                "ai", "/api/ai/chat (nécessite auth)"
            ),
            "author", "Gabriel Morand",
            "technologies", "Spring Boot 3, JWT, PostgreSQL, Mistral AI, Railway"
        );
    }

    @GetMapping("/health")
    @ResponseBody
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "message", "Chess API is running!"
        );
    }
}