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

    @GetMapping("/readme")
    public String readme() {
        return "readme"; 
    }

    @GetMapping("/api/info")
    @ResponseBody
    public Map<String, Object> apiInfo() {
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
            
            "quick_start", Map.of(
                "register", "POST /api/auth/register",
                "login", "POST /api/auth/login", 
                "chat_ai", "POST /api/ai/chat (avec JWT)",
                "get_games", "GET /api/games"
            ),
            
            "endpoints_summary", Map.of(
                "authentication", "2 endpoints",
                "users", "4 endpoints",
                "games", "6 endpoints",
                "tournaments", "5 endpoints", 
                "ai_features", "6 endpoints",
                "rankings", "4 endpoints"
            )
        );
    }

    @GetMapping("/health")
    @ResponseBody
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "message", "♟️ Chess API is running perfectly!",
            "timestamp", java.time.LocalDateTime.now().toString(),
            "uptime", "Application healthy"
        );
    }
}