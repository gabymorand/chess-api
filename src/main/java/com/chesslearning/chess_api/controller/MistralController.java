package com.chesslearning.chess_api.controller;

import com.chesslearning.chess_api.dto.ChatRequestDTO;
import com.chesslearning.chess_api.dto.ChessAnalysisResponse;
import com.chesslearning.chess_api.service.MistralService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "Chess AI Assistant", description = "AI-powered chess analysis and assistance using Mistral AI")
public class MistralController {

    @Autowired
    private MistralService mistralService;

    @PostMapping("/chat")
    @Operation(summary = "Chat with Chess AI", description = "Have a conversation with the chess AI assistant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AI response generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "AI service error")
    })
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // ✅ ENLEVÉ - PUBLIC
    public Mono<ResponseEntity<ChessAnalysisResponse>> chatWithAI(@Valid @RequestBody ChatRequestDTO request) {
        return mistralService.chatWithMistral(request.getMessage())
                .map(response -> {
                    ChessAnalysisResponse analysisResponse = new ChessAnalysisResponse(response, "chat");
                    return ResponseEntity.ok(analysisResponse);
                })
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/analyze/game/{gameId}")
    @Operation(summary = "Analyze chess game", description = "Get AI analysis of a complete chess game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game analysis completed"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // ✅ ENLEVÉ - PUBLIC
    public Mono<ResponseEntity<ChessAnalysisResponse>> analyzeGame(
            @Parameter(description = "Game ID to analyze") @PathVariable Long gameId) {
        
        return mistralService.analyzeGame(gameId)
                .map(analysis -> {
                    ChessAnalysisResponse response = new ChessAnalysisResponse(analysis, "analysis", gameId);
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PostMapping("/suggest/move/{gameId}")
    @Operation(summary = "Suggest next move", description = "Get AI suggestion for the next move in a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Move suggestion generated"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // ✅ ENLEVÉ - PUBLIC
    public Mono<ResponseEntity<ChessAnalysisResponse>> suggestMove(
            @Parameter(description = "Game ID") @PathVariable Long gameId,
            @Parameter(description = "Current board position") @RequestParam String position) {
        
        return mistralService.suggestMove(gameId, position)
                .map(suggestion -> {
                    ChessAnalysisResponse response = new ChessAnalysisResponse(suggestion, "suggestion", gameId);
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PostMapping("/explain/move")
    @Operation(summary = "Explain chess move", description = "Get detailed explanation of a chess move")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Move explanation generated"),
            @ApiResponse(responseCode = "400", description = "Invalid move or position")
    })
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // ✅ ENLEVÉ - PUBLIC
    public Mono<ResponseEntity<ChessAnalysisResponse>> explainMove(
            @Parameter(description = "Chess move in algebraic notation") @RequestParam String move,
            @Parameter(description = "Board position") @RequestParam String position) {
        
        return mistralService.explainMove(move, position)
                .map(explanation -> {
                    ChessAnalysisResponse response = new ChessAnalysisResponse(explanation, "explanation");
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @GetMapping("/quiz")
    @Operation(summary = "Generate chess quiz", description = "Get a chess tactics quiz based on difficulty level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quiz generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid difficulty level")
    })
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // ✅ ENLEVÉ - PUBLIC
    public Mono<ResponseEntity<ChessAnalysisResponse>> generateQuiz(
            @Parameter(description = "Difficulty level: beginner, intermediate, advanced") 
            @RequestParam(defaultValue = "intermediate") String difficulty) {
        
        if (!difficulty.matches("beginner|intermediate|advanced")) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
        
        return mistralService.generateChessQuiz(difficulty)
                .map(quiz -> {
                    ChessAnalysisResponse response = new ChessAnalysisResponse(quiz, "quiz");
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @PostMapping("/tips/improvement")
    @Operation(summary = "Get improvement tips", description = "Get personalized chess improvement tips based on player statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tips generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid statistics")
    })
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // ✅ ENLEVÉ - PUBLIC
    public Mono<ResponseEntity<ChessAnalysisResponse>> getImprovementTips(
            @Parameter(description = "Player statistics in text format") @RequestParam String stats) {
        
        return mistralService.getImprovementTips(stats)
                .map(tips -> {
                    ChessAnalysisResponse response = new ChessAnalysisResponse(tips, "tips");
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @GetMapping("/health")
    @Operation(summary = "Check AI service health", description = "Check if Mistral AI service is available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AI service is healthy"),
            @ApiResponse(responseCode = "503", description = "AI service unavailable")
    })
    public Mono<ResponseEntity<String>> healthCheck() {
        return mistralService.chatWithMistral("Hello")
                .map(response -> ResponseEntity.ok("Mistral AI service is operational"))
                .onErrorReturn(ResponseEntity.status(503).body("Mistral AI service unavailable"));
    }

    // ✅ GARDE CES ENDPOINTS DE TEST POUR DEBUG
    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Test endpoint for debugging")
    public ResponseEntity<String> testAuth() {
        System.out.println("✅ MistralController test endpoint reached!");
        return ResponseEntity.ok("AI endpoints are now public!");
    }

    @PostMapping("/test-post")
    public ResponseEntity<String> testPost(@RequestBody String message) {
        System.out.println("✅ POST test endpoint reached with: " + message);
        return ResponseEntity.ok("POST works! Message: " + message);
    }
}