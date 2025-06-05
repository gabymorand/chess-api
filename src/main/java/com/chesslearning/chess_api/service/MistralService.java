package com.chesslearning.chess_api.service;

import com.chesslearning.chess_api.dto.MistralRequestDTO;
import com.chesslearning.chess_api.dto.MistralResponseDTO;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.Move;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MistralService {

    @Autowired
    private WebClient mistralWebClient;

    @Autowired
    private GameService gameService;

    @Autowired
    private MoveService moveService;

    @Value("${mistral.model:mistral-small}")
    private String defaultModel;

    // Pronmpt de base
    public Mono<String> chatWithMistral(String userMessage) {
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

        return mistralWebClient
                .post()
                .uri("/chat/completions")
                .body(Mono.just(request), MistralRequestDTO.class)
                .retrieve()
                .bodyToMono(MistralResponseDTO.class)
                .map(response -> {
                    if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                        return response.getChoices().get(0).getMessage().getContent();
                    }
                    return "Désolé, je n'ai pas pu traiter votre demande.";
                })
                .onErrorReturn("Erreur lors de la communication avec Mistral AI.");
    }

    public Mono<String> analyzeGame(Long gameId) {
        Optional<Game> gameOptional = gameService.getGameById(gameId);
        
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            Pageable pageable = PageRequest.of(0, 1000); // Récupérer jusqu'à 1000 moves
            Page<Move> movesPage = moveService.getMovesByGame(game, pageable);
            List<Move> moves = movesPage.getContent();
            String pgn = generatePGN(moves);
            
            String prompt = String.format("""
                Analyse cette partie d'échecs en notation PGN :
                
                %s
                
                Fournis une analyse concise incluant :
                1. Les moments clés de la partie
                2. Les erreurs principales
                3. Les bons coups remarquables
                4. Une évaluation générale
                
                Réponds en français, de manière pédagogique.
                """, pgn);

            return chatWithMistral(prompt);
        } else {
            return Mono.just("Partie non trouvée.");
        }
    }


    public Mono<String> suggestMove(Long gameId, String currentPosition) {
        Optional<Game> gameOptional = gameService.getGameById(gameId);
        
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            Pageable pageable = PageRequest.of(0, 1000);
            Page<Move> movesPage = moveService.getMovesByGame(game, pageable);
            List<Move> moves = movesPage.getContent();
            String gameHistory = moves.stream()
                    .map(move -> move.getMoveNumber() + ". " + move.getMoveNotation())
                    .collect(Collectors.joining(" "));

            String prompt = String.format("""
                Voici l'historique d'une partie d'échecs en cours :
                %s
                
                Position actuelle : %s
                
                Suggère le meilleur coup à jouer et explique pourquoi en 2-3 phrases.
                Réponds en français.
                """, gameHistory, currentPosition);

            return chatWithMistral(prompt);
        } else {
            return Mono.just("Partie non trouvée.");
        }
    }


    public Mono<String> explainMove(String move, String position) {
        String prompt = String.format("""
            Explique ce coup d'échecs : %s
            Dans cette position : %s
            
            Explique :
            1. L'objectif de ce coup
            2. Ses avantages
            3. Ses risques potentiels
            
            Réponds en français sauf si on te demande ou te pose la question dans une autre langue, de manière pédagogique.
            """, move, position);

        return chatWithMistral(prompt);
    }


    public Mono<String> generateChessQuiz(String difficulty) {
        String prompt = String.format("""
            Génère un quiz d'échecs de niveau %s.
            
            Format :
            1. Une position d'échecs décrite en notation algébrique
            2. Une question tactique
            3. 3 options de réponse (A, B, C)
            
            Assure-toi que ce soit éducatif et adapté au niveau %s.
            Réponds en français sauf si on te demande ou te pose la question dans une autre langue.
            """, difficulty, difficulty);

        return chatWithMistral(prompt);
    }


    public Mono<String> getImprovementTips(String playerStats) {
        String prompt = String.format("""
            Voici les statistiques d'un joueur d'échecs :
            %s
            
            Sur cette base, donne 3 conseils concrets pour améliorer son jeu.
            Sois spécifique et constructif.
            Réponds en français sauf si on te demande ou te pose la question dans une autre langue.
            """, playerStats);

        return chatWithMistral(prompt);
    }


    private String generatePGN(List<Move> moves) {
        StringBuilder pgn = new StringBuilder();
        for (Move move : moves) {
            Integer moveNumber = move.getMoveNumber();
            if (moveNumber != null && moveNumber % 2 == 1) {
                pgn.append(((moveNumber + 1) / 2)).append(". ");
            }

            String moveString = move.getMoveNotation();
            if (moveString != null) {
                pgn.append(moveString).append(" ");
            }
        }
        return pgn.toString().trim();
    }
}