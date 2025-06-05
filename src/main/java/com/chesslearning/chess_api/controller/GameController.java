package com.chesslearning.chess_api.controller;

import com.chesslearning.chess_api.dto.GameCreateDTO;
import com.chesslearning.chess_api.dto.GameResponseDTO;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.GameResult;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.mapper.GameMapper;
import com.chesslearning.chess_api.service.GameService;
import com.chesslearning.chess_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Game Management", description = "APIs for managing chess games")
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private GameMapper gameMapper;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    @Operation(summary = "Create a new game", description = "Creates a new chess game")
    public ResponseEntity<GameResponseDTO> createGame(@Valid @RequestBody GameCreateDTO gameCreateDTO) {
        try {
            System.out.println("🔍 Starting game creation...");
            System.out.println("PlayerWhiteId: " + gameCreateDTO.getPlayerWhiteId());
            System.out.println("PlayerBlackId: " + gameCreateDTO.getPlayerBlackId());
            System.out.println("TimeControl: " + gameCreateDTO.getTimeControl());
            
            // ✅ UTILISE LE MAPPER CORRIGÉ
            Game game = gameMapper.toEntity(gameCreateDTO);
            System.out.println("✅ Game entity created via mapper");
            
            System.out.println("🔍 Calling gameService.createGame()...");
            Game createdGame = gameService.createGame(game);
            System.out.println("✅ Game created in database with ID: " + createdGame.getId());
            
            System.out.println("🔍 Mapping to ResponseDTO...");
            GameResponseDTO response = gameMapper.toResponseDTO(createdGame);
            System.out.println("✅ ResponseDTO created");
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (RuntimeException e) {
            System.out.println("❌ RuntimeException: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("❌ General Exception: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(summary = "Get game by ID", description = "Retrieves a game by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> getGameById(
            @Parameter(description = "Game ID") @PathVariable Long id) {
        Optional<Game> game = gameService.getGameById(id);
        return game.map(value -> new ResponseEntity<>(gameMapper.toResponseDTO(value), HttpStatus.OK))
                  .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Get all games", description = "Retrieves all games with pagination")
    @GetMapping
    public ResponseEntity<Page<GameResponseDTO>> getAllGames(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "gameDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Game> games = gameService.getAllGames(pageable);
        Page<GameResponseDTO> gameDTOs = games.map(gameMapper::toResponseDTO);
        return new ResponseEntity<>(gameDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Update game", description = "Updates an existing game")
    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDTO> updateGame(
            @Parameter(description = "Game ID") @PathVariable Long id,
            @Valid @RequestBody GameCreateDTO gameUpdateDTO) {
        try {
            Game gameDetails = gameMapper.toEntity(gameUpdateDTO);
            Game updatedGame = gameService.updateGame(id, gameDetails);
            GameResponseDTO response = gameMapper.toResponseDTO(updatedGame);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Delete game", description = "Deletes a game by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(
            @Parameter(description = "Game ID") @PathVariable Long id) {
        try {
            gameService.deleteGame(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Get games by player", description = "Retrieves games for a specific player")
    @GetMapping("/player/{playerId}")
    public ResponseEntity<Page<GameResponseDTO>> getGamesByPlayer(
            @Parameter(description = "Player ID") @PathVariable Long playerId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        try {
            // ✅ RÉCUPÉRER L'UTILISATEUR PAR SON ID
            User player = userService.getUserById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
            
            Pageable pageable = PageRequest.of(page, size);
            Page<Game> games = gameService.getGamesByPlayer(player, pageable);
            Page<GameResponseDTO> gameDTOs = games.map(gameMapper::toResponseDTO);
            return new ResponseEntity<>(gameDTOs, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Get games by result", description = "Retrieves games by result")
    @GetMapping("/result/{result}")
    public ResponseEntity<Page<GameResponseDTO>> getGamesByResult(
            @Parameter(description = "Game result") @PathVariable GameResult result,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Game> games = gameService.getGamesByResult(result, pageable);
        Page<GameResponseDTO> gameDTOs = games.map(gameMapper::toResponseDTO);
        return new ResponseEntity<>(gameDTOs, HttpStatus.OK);
    }
}