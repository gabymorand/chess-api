package com.chesslearning.chess_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; 
    }

    @GetMapping("/api")
    @ResponseBody
    public Map<String, Object> projectInfo() {
        return Map.of(
            "project", "♟️ Chess API - Spring Boot Project",
            "author", "Gabriel Morand",
            "description", "API REST complète pour système d'échecs avec IA intégrée",
            "version", "1.0.0",
            "status", "✅ Production Ready",
            
            "technologies", Map.of(
                "backend", "Spring Boot 3.5.0",
                "database", "PostgreSQL",
                "security", "JWT Authentication", 
                "ai", "Mistral AI Integration",
                "deployment", "Railway Cloud",
                "documentation", "Swagger/OpenAPI"
            ),
            
            "features", java.util.List.of(
                "🔐 JWT Authentication avec rôles",
                "♟️ Gestion complète de parties d'échecs", 
                "🏆 Système de tournois et classements",
                "🤖 IA Mistral pour analyse et conseils",
                "📊 Statistiques et métriques détaillées",
                "📚 Documentation Swagger interactive"
            ),
            
            "links", Map.of(
                "homepage", "/",
                "swagger", "/swagger-ui.html",
                "docs", "/v3/api-docs",
                "health", "/health",
                "github", "https://github.com/GabrielMorand/chess-api"
            ),
            
            "endpoints_count", Map.of(
                "total", "30+",
                "public", "15",
                "user_protected", "10", 
                "admin_only", "5"
            )
        );
    }

    @GetMapping("/health")
    @ResponseBody
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "message", "♟️ Chess API is running perfectly!",
            "timestamp", java.time.LocalDateTime.now().toString()
        );
    }
}