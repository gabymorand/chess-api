package com.chesslearning.chess_api.service;

import com.chesslearning.chess_api.dto.MistralRequestDTO;
import com.chesslearning.chess_api.dto.MistralResponseDTO;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MistralService {

    private static final Logger logger = LoggerFactory.getLogger(MistralService.class);

    @Autowired
    private WebClient mistralWebClient;

    @Autowired
    private GameService gameService;

    @Autowired
    private MoveService moveService;

    @Value("${mistral.model:mistral-small}")
    private String defaultModel;

    @Value("${mistral.api.key}")
    private String apiKey;

    @Value("${mistral.api.url}")
    private String apiUrl;

    /**
     * Chat principal avec Mistral AI
     */
    public Mono<String> chatWithMistral(String userMessage) {
        logger.info("🚀 Début communication avec Mistral AI");
        logger.info("📤 Message utilisateur: {}", userMessage);
        logger.info("🔑 API Key présente: {}", apiKey != null && !apiKey.isEmpty() ? "✅ OUI" : "❌ NON");
        logger.info("🌐 API URL: {}", apiUrl);
        logger.info("🤖 Modèle utilisé: {}", defaultModel);

        // Vérification de la clé API
        if (apiKey == null || apiKey.isEmpty() || "l3WguwUpqE0xYijOoNq6LzxqtlFhEAnv".equals(apiKey)) {
            logger.error("❌ ERREUR: Clé API Mistral non configurée ou invalide!");
            return Mono.just("Erreur: Clé API Mistral non configurée. Veuillez configurer MISTRAL_API_KEY.");
        }
        
        String systemPrompt = """
            Tu es un expert en échecs, passionné et pédagogue. 
            Tu aides les joueurs à améliorer leur jeu en expliquant les concepts d'échecs de manière claire et accessible.
            Réponds en français sauf si on te demande ou te pose la question dans une autre langue et sois enthousiaste mais concis.
            """;

        MistralRequestDTO request = new MistralRequestDTO(
                defaultModel,
                Arrays.asList(
                        new MistralRequestDTO.Message("system", systemPrompt),
                        new MistralRequestDTO.Message("user", userMessage)
                ),
                500,
                0.7
        );

        logger.info("📋 Requête préparée: Model={}, Messages={}", request.getModel(), request.getMessages().size());

        return mistralWebClient
                .post()
                .uri("/chat/completions")
                .body(Mono.just(request), MistralRequestDTO.class)
                .retrieve()
                .bodyToMono(MistralResponseDTO.class)
                .doOnNext(response -> logger.info("✅ Réponse reçue de Mistral AI"))
                .map(response -> {
                    if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                        String content = response.getChoices().get(0).getMessage().getContent();
                        logger.info("💬 Contenu de la réponse: {}...", content.substring(0, Math.min(100, content.length())));
                        return content;
                    }
                    logger.warn("⚠️ Aucun choix dans la réponse Mistral");
                    return "Désolé, je n'ai pas pu traiter votre demande.";
                })
                .doOnError(error -> {
                    logger.error("❌ Erreur lors de la communication avec Mistral AI: ", error);
                    logger.error("🔍 Type d'erreur: {}", error.getClass().getSimpleName());
                    logger.error("📝 Message d'erreur: {}", error.getMessage());
                    
                    if (error instanceof WebClientResponseException) {
                        WebClientResponseException webError = (WebClientResponseException) error;
                        logger.error("🌐 Code HTTP: {}", webError.getStatusCode());
                        logger.error("📄 Corps de la réponse: {}", webError.getResponseBodyAsString());
                    }
                })
                .onErrorReturn("Erreur lors de la communication avec Mistral AI.");
    }

    /**
     * Analyse complète d'une partie d'échecs
     */
    public Mono<String> analyzeGame(Long gameId) {
        logger.info("🔍 Analyse du jeu ID: {}", gameId);
        
        Optional<Game> gameOptional = gameService.getGameById(gameId);
        
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            Pageable pageable = PageRequest.of(0, 1000);
            Page<Move> movesPage = moveService.getMovesByGame(game, pageable);
            List<Move> moves = movesPage.getContent();
            String pgn = generatePGN(moves);
            
            String analysisPrompt = String.format("""
                Analyse cette partie d'échecs en détail :
                
                Joueur Blanc: %s
                Joueur Noir: %s
                Résultat: %s
                Nombre de coups: %d
                
                PGN des coups:
                %s
                
                Donne une analyse complète incluant :
                1. Les moments clés de la partie
                2. Les erreurs tactiques importantes
                3. Les points forts de chaque joueur
                4. Des conseils d'amélioration
                
                Réponds en français de manière structurée et pédagogique.
                """,
                game.getPlayerWhite() != null ? game.getPlayerWhite().getUsername() : "Anonyme",
                game.getPlayerBlack() != null ? game.getPlayerBlack().getUsername() : "Anonyme", 
                game.getResult() != null ? game.getResult().toString() : "En cours",
                moves.size(),
                pgn
            );

            return chatWithMistral(analysisPrompt);
        } else {
            logger.warn("⚠️ Partie non trouvée: {}", gameId);
            return Mono.just("Partie non trouvée.");
        }
    }

    /**
     * Suggestion de coup optimal
     */
    public Mono<String> suggestMove(Long gameId, String currentPosition) {
        logger.info("💡 Suggestion de coup pour le jeu ID: {}", gameId);
        
        Optional<Game> gameOptional = gameService.getGameById(gameId);
        
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            Pageable pageable = PageRequest.of(0, 50); // Derniers 50 coups
            Page<Move> movesPage = moveService.getMovesByGame(game, pageable);
            List<Move> recentMoves = movesPage.getContent();
            String recentPgn = generatePGN(recentMoves);
            
            String prompt = String.format("""
                Suggère le meilleur coup pour cette position d'échecs:
                
                Position actuelle: %s
                Derniers coups de la partie:
                %s
                
                Analyse la position et recommande :
                1. Le meilleur coup en notation algébrique
                2. La raison tactique/stratégique
                3. Les variantes principales à considérer
                4. Les pièges à éviter
                
                Réponds en français de manière claire et précise.
                """, currentPosition, recentPgn);

            return chatWithMistral(prompt);
        } else {
            logger.warn("⚠️ Partie non trouvée pour suggestion: {}", gameId);
            return Mono.just("Partie non trouvée.");
        }
    }

    /**
     * Explication détaillée d'un coup
     */
    public Mono<String> explainMove(String move, String position) {
        logger.info("📝 Explication du coup: {} en position: {}", move, position);
        
        String prompt = String.format("""
            Explique ce coup d'échecs de manière pédagogique :
            
            Coup joué: %s
            Position: %s
            
            Analyse et explique :
            1. L'objectif tactique/stratégique de ce coup
            2. Ses avantages immédiats
            3. Ses conséquences à long terme
            4. Les risques potentiels
            5. Les alternatives possibles
            
            Réponds en français de manière claire et éducative.
            """, move, position);

        return chatWithMistral(prompt);
    }

    /**
     * Génération de quiz tactiques
     */
    public Mono<String> generateChessQuiz(String difficulty) {
        logger.info("🧩 Génération d'un quiz niveau: {}", difficulty);
        
        String prompt = String.format("""
            Génère un problème d'échecs tactique de niveau %s.
            
            Format demandé :
            📋 **POSITION**
            [Description de la position en notation algébrique ou FEN]
            
            ❓ **QUESTION**
            [Question tactique claire : "Les Blancs jouent et gagnent", "Mat en 2 coups", etc.]
            
            🎯 **OPTIONS**
            A) [Premier coup possible]
            B) [Deuxième coup possible]  
            C) [Troisième coup possible]
            
            ✅ **SOLUTION**
            [Bonne réponse avec explication détaillée]
            
            Assure-toi que le problème soit :
            - Adapté au niveau %s
            - Éducatif et instructif
            - Avec une solution claire
            
            Réponds en français.
            """, difficulty, difficulty);

        return chatWithMistral(prompt);
    }

    /**
     * Conseils personnalisés d'amélioration
     */
    public Mono<String> getImprovementTips(String playerStats) {
        logger.info("📈 Conseils d'amélioration basés sur: {}", playerStats);
        
        String prompt = String.format("""
            Analyse ces statistiques d'un joueur d'échecs et donne des conseils personnalisés :
            
            📊 **STATISTIQUES DU JOUEUR**
            %s
            
            Fournis des conseils structurés :
            
            🎯 **POINTS FORTS IDENTIFIÉS**
            [Ce que le joueur fait bien]
            
            ⚠️ **DOMAINES À AMÉLIORER**
            [Faiblesses principales détectées]
            
            📚 **PLAN D'ENTRAÎNEMENT**
            [Exercices et études recommandés]
            
            🏆 **OBJECTIFS À COURT TERME**
            [Buts réalisables sur 1-3 mois]
            
            💡 **CONSEILS PRATIQUES**
            [Astuces concrètes pour progresser]
            
            Réponds en français de manière motivante et constructive.
            """, playerStats);

        return chatWithMistral(prompt);
    }

    /**
     * Test de santé du service Mistral
     */
    public Mono<String> healthCheck() {
        logger.info("🏥 Test de santé Mistral AI");
        return chatWithMistral("Réponds simplement 'Service Mistral opérationnel!' pour confirmer que tu fonctionnes.");
    }

    /**
     * Génération de PGN à partir d'une liste de coups
     */
    private String generatePGN(List<Move> moves) {
        if (moves == null || moves.isEmpty()) {
            return "Aucun coup enregistré.";
        }
        
        StringBuilder pgn = new StringBuilder();
        int moveNumber = 1;
        boolean isWhiteMove = true;
        
        List<Move> sortedMoves = moves.stream()
                .sorted((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                .collect(Collectors.toList());
        
        for (Move move : sortedMoves) {
            if (isWhiteMove) {
                pgn.append(moveNumber).append(". ");
                pgn.append(move.getMoveNotation()).append(" "); 
                isWhiteMove = false;
            } else {
                pgn.append(move.getMoveNotation()).append(" ");
                isWhiteMove = true;
                moveNumber++;
            }
        }
        
        return pgn.toString().trim();
    }


    public boolean isConfigurationValid() {
        boolean valid = apiKey != null && 
                       !apiKey.isEmpty() && 
                       !apiKey.equals("EDCi9Uvu4RWSKJ3kauFFyOKyPdXicOzX") &&
                       apiUrl != null && 
                       !apiUrl.isEmpty() &&
                       defaultModel != null && 
                       !defaultModel.isEmpty();
        
        logger.info("🔧 Configuration Mistral valide: {}", valid ? "✅ OUI" : "❌ NON");
        
        if (!valid) {
            logger.error("❌ Configuration Mistral invalide:");
            logger.error("   - API Key: {}", apiKey != null && !apiKey.isEmpty() ? "✅" : "❌");
            logger.error("   - API URL: {}", apiUrl != null && !apiUrl.isEmpty() ? "✅" : "❌");
            logger.error("   - Model: {}", defaultModel != null && !defaultModel.isEmpty() ? "✅" : "❌");
        }
        
        return valid;
    }
}