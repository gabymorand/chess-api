package com.chesslearning.chess_api.controller;

import com.chesslearning.chess_api.dto.MoveCreateDTO;
import com.chesslearning.chess_api.dto.MoveResponseDTO;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.Move;
import com.chesslearning.chess_api.mapper.MoveMapper;
import com.chesslearning.chess_api.service.GameService;
import com.chesslearning.chess_api.service.MoveService;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/moves")
@Tag(name = "Move Management", description = "APIs for managing chess moves")
public class MoveController {
    
    @Autowired
    private MoveService moveService;
    
    @Autowired
    private MoveMapper moveMapper;
    
    @Autowired
    private GameService gameService;
    
    @Operation(summary = "Create a new move", description = "Creates a new chess move")
    @PostMapping
    public ResponseEntity<MoveResponseDTO> createMove(@Valid @RequestBody MoveCreateDTO moveCreateDTO) {
        try {
            Move move = moveMapper.toEntity(moveCreateDTO);
            Move createdMove = moveService.createMove(move);
            MoveResponseDTO response = moveMapper.toResponseDTO(createdMove);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = "Get move by ID", description = "Retrieves a move by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<MoveResponseDTO> getMoveById(
            @Parameter(description = "Move ID") @PathVariable Long id) {
        Optional<Move> move = moveService.getMoveById(id);
        return move.map(value -> new ResponseEntity<>(moveMapper.toResponseDTO(value), HttpStatus.OK))
                  .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Get all moves", description = "Retrieves all moves with pagination")
    @GetMapping
    public ResponseEntity<Page<MoveResponseDTO>> getAllMoves(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "moveTime") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Move> moves = moveService.getAllMoves(pageable);
        Page<MoveResponseDTO> moveDTOs = moves.map(moveMapper::toResponseDTO);
        return new ResponseEntity<>(moveDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Update move", description = "Updates an existing move")
    @PutMapping("/{id}")
    public ResponseEntity<MoveResponseDTO> updateMove(
            @Parameter(description = "Move ID") @PathVariable Long id,
            @Valid @RequestBody MoveCreateDTO moveUpdateDTO) {
        try {
            Move moveDetails = moveMapper.toEntity(moveUpdateDTO);
            Move updatedMove = moveService.updateMove(id, moveDetails);
            MoveResponseDTO response = moveMapper.toResponseDTO(updatedMove);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Delete move", description = "Deletes a move by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMove(
            @Parameter(description = "Move ID") @PathVariable Long id) {
        try {
            moveService.deleteMove(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Get moves by game", description = "Retrieves moves for a specific game")
    @GetMapping("/game/{gameId}")
    public ResponseEntity<Page<MoveResponseDTO>> getMovesByGame(
            @Parameter(description = "Game ID") @PathVariable Long gameId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        try {
            Game game = gameService.getGameById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
            
            Pageable pageable = PageRequest.of(page, size);
            Page<Move> moves = moveService.getMovesByGame(game, pageable);
            Page<MoveResponseDTO> moveDTOs = moves.map(moveMapper::toResponseDTO);
            return new ResponseEntity<>(moveDTOs, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Get moves by game ordered", description = "Get all moves of a game in order")
    @GetMapping("/game/{gameId}/ordered")
    public ResponseEntity<List<MoveResponseDTO>> getMovesByGameOrdered(
            @Parameter(description = "Game ID") @PathVariable Long gameId) {
        
        try {
            Game game = gameService.getGameById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
            
            List<Move> moves = moveService.getMovesByGameOrdered(game);
            List<MoveResponseDTO> moveDTOs = moves.stream()
                .map(moveMapper::toResponseDTO)
                .toList();
            return new ResponseEntity<>(moveDTOs, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Get moves by piece", description = "Get moves by piece type")
    @GetMapping("/piece/{piece}")
    public ResponseEntity<Page<MoveResponseDTO>> getMovesByPiece(
            @Parameter(description = "Piece type") @PathVariable String piece,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Move> moves = moveService.getMovesByPiece(piece, pageable);
        Page<MoveResponseDTO> moveDTOs = moves.map(moveMapper::toResponseDTO);
        return new ResponseEntity<>(moveDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Get check moves", description = "Get all moves that resulted in check")
    @GetMapping("/checks")
    public ResponseEntity<Page<MoveResponseDTO>> getCheckMoves(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Move> moves = moveService.getCheckMoves(pageable);
        Page<MoveResponseDTO> moveDTOs = moves.map(moveMapper::toResponseDTO);
        return new ResponseEntity<>(moveDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Get checkmate moves", description = "Get all moves that resulted in checkmate")
    @GetMapping("/checkmates")
    public ResponseEntity<Page<MoveResponseDTO>> getCheckmateMoves(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Move> moves = moveService.getCheckmateMoves(pageable);
        Page<MoveResponseDTO> moveDTOs = moves.map(moveMapper::toResponseDTO);
        return new ResponseEntity<>(moveDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Get castling moves", description = "Get all castling moves")
    @GetMapping("/castling")
    public ResponseEntity<Page<MoveResponseDTO>> getCastlingMoves(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Move> moves = moveService.getCastlingMoves(pageable);
        Page<MoveResponseDTO> moveDTOs = moves.map(moveMapper::toResponseDTO);
        return new ResponseEntity<>(moveDTOs, HttpStatus.OK);
    }
}