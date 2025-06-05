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
    @ResponseBody
    public String readme() {
        return """
            <!DOCTYPE html>
            <html lang="fr">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>📖 README - Chess API</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 1200px;
                        margin: 0 auto;
                        padding: 20px;
                        background: #f5f5f5;
                    }
                    .readme-container {
                        background: white;
                        padding: 40px;
                        border-radius: 10px;
                        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                    }
                    h1, h2, h3 { color: #2c3e50; }
                    h1 { text-align: center; font-size: 2.5rem; margin-bottom: 10px; }
                    .badges { text-align: center; margin: 20px 0; }
                    .badge {
                        display: inline-block;
                        background: #3498db;
                        color: white;
                        padding: 5px 10px;
                        border-radius: 15px;
                        margin: 5px;
                        font-size: 0.9rem;
                    }
                    .back-btn {
                        background: #3498db;
                        color: white;
                        padding: 10px 20px;
                        border: none;
                        border-radius: 5px;
                        cursor: pointer;
                        text-decoration: none;
                        display: inline-block;
                        margin-bottom: 20px;
                    }
                    .back-btn:hover { background: #2980b9; }
                    .grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                        gap: 20px;
                        margin: 20px 0;
                    }
                    .card {
                        background: #f8f9fa;
                        padding: 20px;
                        border-radius: 8px;
                        border-left: 4px solid #3498db;
                    }
                    code {
                        background: #f1f2f6;
                        padding: 2px 5px;
                        border-radius: 3px;
                        font-family: 'Courier New', monospace;
                    }
                </style>
            </head>
            <body>
                <div class="readme-container">
                    <a href="/" class="back-btn">← Retour à l'accueil</a>
                    
                    <h1>♟️ Chess API - Documentation</h1>
                    
                    <div class="badges">
                        <span class="badge">Spring Boot 3.5.0</span>
                        <span class="badge">Java 17</span>
                        <span class="badge">PostgreSQL</span>
                        <span class="badge">JWT Security</span>
                        <span class="badge">Mistral AI</span>
                        <span class="badge">Railway Deploy</span>
                    </div>
                    
                    <h2>🎯 Aperçu du Projet</h2>
                    <p>Cette API REST moderne permet de gérer un système complet d'échecs incluant :</p>
                    <ul>
                        <li>👥 <strong>Gestion des utilisateurs</strong> avec authentification JWT</li>
                        <li>♟️ <strong>Parties d'échecs</strong> complètes avec historique des coups</li>
                        <li>🏆 <strong>Tournois</strong> et système de classement</li>
                        <li>🤖 <strong>Intelligence Artificielle</strong> intégrée (Mistral AI)</li>
                        <li>📊 <strong>Statistiques</strong> et analyses de parties</li>
                    </ul>
                    
                    <h2>🚀 Démarrage Rapide</h2>
                    <div class="grid">
                        <div class="card">
                            <h3>1. Inscription</h3>
                            <p><code>POST /api/auth/register</code></p>
                            <p>Créez un compte avec username, email et password</p>
                        </div>
                        
                        <div class="card">
                            <h3>2. Connexion</h3>
                            <p><code>POST /api/auth/login</code></p>
                            <p>Obtenez votre token JWT pour l'authentification</p>
                        </div>
                        
                        <div class="card">
                            <h3>3. Chat IA</h3>
                            <p><code>POST /api/ai/chat</code></p>
                            <p>Dialoguez avec l'IA Mistral pour des conseils échecs</p>
                        </div>
                        
                        <div class="card">
                            <h3>4. Créer une partie</h3>
                            <p><code>POST /api/games</code></p>
                            <p>Lancez une nouvelle partie d'échecs</p>
                        </div>
                    </div>
                    
                    <h2>👨‍💻 Développeur</h2>
                    <p><strong>Gabriel Morand</strong> - Projet étudiant Spring Boot 2025</p>
                    
                    <h2>🔗 Liens Utiles</h2>
                    <p>
                        <a href="/swagger-ui.html" target="_blank" class="back-btn">📚 Documentation Swagger</a>
                        <a href="/health" target="_blank" class="back-btn">❤️ Health Check</a>
                        <a href="/api/info" target="_blank" class="back-btn">🔍 API Info</a>
                    </p>
                </div>
            </body>
            </html>
            """;
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