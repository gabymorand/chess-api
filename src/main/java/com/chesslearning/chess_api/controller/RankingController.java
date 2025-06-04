package com.chesslearning.chess_api.controller;

import com.chesslearning.chess_api.dto.RankingCreateDTO;
import com.chesslearning.chess_api.dto.RankingResponseDTO;
import com.chesslearning.chess_api.entity.Ranking;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.mapper.RankingMapper;
import com.chesslearning.chess_api.service.RankingService;
import com.chesslearning.chess_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rankings")
@Tag(name = "Ranking Management", description = "APIs for managing chess player rankings")
public class RankingController {
    
    @Autowired
    private RankingService rankingService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RankingMapper rankingMapper;
    
    // CRUD Operations
    
    @PostMapping
    @Operation(summary = "Create a new ranking", description = "Create a new player ranking")
    public ResponseEntity<RankingResponseDTO> createRanking(@Valid @RequestBody RankingCreateDTO rankingCreateDTO) {
        try {
            Ranking ranking = rankingMapper.toEntity(rankingCreateDTO);
            Ranking savedRanking = rankingService.createRanking(ranking);
            RankingResponseDTO responseDTO = rankingMapper.toResponseDTO(savedRanking);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get ranking by ID", description = "Retrieve a specific ranking by its ID")
    public ResponseEntity<RankingResponseDTO> getRankingById(
            @Parameter(description = "Ranking ID") @PathVariable Long id) {
        return rankingService.getRankingById(id)
                .map(ranking -> {
                    RankingResponseDTO responseDTO = rankingMapper.toResponseDTO(ranking);
                    return ResponseEntity.ok(responseDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "Get all rankings", description = "Retrieve all rankings with pagination")
    public ResponseEntity<Page<RankingResponseDTO>> getAllRankings(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "eloRating") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Ranking> rankings = rankingService.getAllRankings(pageable);
        Page<RankingResponseDTO> responseDTOs = rankings.map(rankingMapper::toResponseDTO);
        
        return ResponseEntity.ok(responseDTOs);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update ranking", description = "Update an existing ranking")
    public ResponseEntity<RankingResponseDTO> updateRanking(
            @Parameter(description = "Ranking ID") @PathVariable Long id,
            @Valid @RequestBody RankingCreateDTO rankingCreateDTO) {
        try {
            Ranking rankingDetails = rankingMapper.toEntity(rankingCreateDTO);
            Ranking updatedRanking = rankingService.updateRanking(id, rankingDetails);
            RankingResponseDTO responseDTO = rankingMapper.toResponseDTO(updatedRanking);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ranking", description = "Delete a ranking by ID")
    public ResponseEntity<Void> deleteRanking(
            @Parameter(description = "Ranking ID") @PathVariable Long id) {
        try {
            rankingService.deleteRanking(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Endpoints spécialisés pour le classement
    
    @GetMapping("/player/{userId}")
    @Operation(summary = "Get ranking by player", description = "Retrieve ranking for a specific player")
    public ResponseEntity<RankingResponseDTO> getRankingByPlayer(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            return rankingService.getRankingByUser(user)
                    .map(ranking -> {
                        RankingResponseDTO responseDTO = rankingMapper.toResponseDTO(ranking);
                        return ResponseEntity.ok(responseDTO);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/leaderboard")
    @Operation(summary = "Get leaderboard", description = "Get rankings ordered by ELO rating")
    public ResponseEntity<Page<RankingResponseDTO>> getLeaderboard(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Ranking> rankings = rankingService.getRankingsByEloRating(pageable);
        Page<RankingResponseDTO> responseDTOs = rankings.map(rankingMapper::toResponseDTO);
        
        return ResponseEntity.ok(responseDTOs);
    }
    
    @GetMapping("/by-winrate")
    @Operation(summary = "Get rankings by win rate", description = "Get rankings ordered by win rate")
    public ResponseEntity<Page<RankingResponseDTO>> getRankingsByWinRate(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Ranking> rankings = rankingService.getRankingsByWinRate(pageable);
        Page<RankingResponseDTO> responseDTOs = rankings.map(rankingMapper::toResponseDTO);
        
        return ResponseEntity.ok(responseDTOs);
    }
    
    @GetMapping("/elo-range")
    @Operation(summary = "Get rankings by ELO range", description = "Get rankings within a specific ELO range")
    public ResponseEntity<Page<RankingResponseDTO>> getRankingsByEloRange(
            @Parameter(description = "Minimum ELO rating") @RequestParam Integer minRating,
            @Parameter(description = "Maximum ELO rating") @RequestParam Integer maxRating,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("eloRating").descending());
        Page<Ranking> rankings = rankingService.getRankingsByEloRange(minRating, maxRating, pageable);
        Page<RankingResponseDTO> responseDTOs = rankings.map(rankingMapper::toResponseDTO);
        
        return ResponseEntity.ok(responseDTOs);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active players", description = "Get rankings for players with minimum games played")
    public ResponseEntity<Page<RankingResponseDTO>> getActiveRankings(
            @Parameter(description = "Minimum games played") @RequestParam(defaultValue = "5") Integer minGames,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("eloRating").descending());
        Page<Ranking> rankings = rankingService.getActiveRankings(minGames, pageable);
        Page<RankingResponseDTO> responseDTOs = rankings.map(rankingMapper::toResponseDTO);
        
        return ResponseEntity.ok(responseDTOs);
    }
    
    @GetMapping("/top/{limit}")
    @Operation(summary = "Get top players", description = "Get top N players by ELO rating")
    public ResponseEntity<List<RankingResponseDTO>> getTopPlayers(
            @Parameter(description = "Number of top players") @PathVariable int limit) {
        
        if (limit > 100) limit = 100; // Limite maximale
        
        Pageable pageable = PageRequest.of(0, limit);
        List<Ranking> rankings = rankingService.getTop10Players(); // Adapter pour utiliser limit
        List<RankingResponseDTO> responseDTOs = rankings.stream()
                .map(rankingMapper::toResponseDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responseDTOs);
    }
    
    @GetMapping("/player/{userId}/position")
    @Operation(summary = "Get player position", description = "Get the position of a player in the ranking")
    public ResponseEntity<Long> getPlayerPosition(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Ranking ranking = rankingService.getRankingByUser(user)
                    .orElseThrow(() -> new RuntimeException("Ranking not found for user"));
            
            Long position = rankingService.getPlayerPosition(ranking.getEloRating());
            return ResponseEntity.ok(position);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/update-stats")
    @Operation(summary = "Update player stats", description = "Update player statistics after a game")
    public ResponseEntity<Void> updatePlayerStats(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Game won") @RequestParam(defaultValue = "false") boolean won,
            @Parameter(description = "Game lost") @RequestParam(defaultValue = "false") boolean lost,
            @Parameter(description = "Game drawn") @RequestParam(defaultValue = "false") boolean draw) {
        
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            rankingService.updatePlayerStats(user, won, lost, draw);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}