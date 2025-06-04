package com.chesslearning.chess_api.controller;

import com.chesslearning.chess_api.dto.CommentCreateDTO;
import com.chesslearning.chess_api.dto.CommentResponseDTO;
import com.chesslearning.chess_api.entity.Comment;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.mapper.CommentMapper;
import com.chesslearning.chess_api.service.CommentService;
import com.chesslearning.chess_api.service.GameService;
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
@RequestMapping("/api/comments")
@Tag(name = "Comment Management", description = "APIs for managing game comments")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CommentMapper commentMapper;
    
    // CRUD Operations
    
    @PostMapping
    @Operation(summary = "Create a new comment", description = "Create a new comment on a game")
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CommentCreateDTO commentCreateDTO) {
        try {
            Comment comment = commentMapper.toEntity(commentCreateDTO);
            Comment savedComment = commentService.createComment(comment);
            CommentResponseDTO responseDTO = commentMapper.toResponseDTO(savedComment);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get comment by ID", description = "Retrieve a specific comment by its ID")
    public ResponseEntity<CommentResponseDTO> getCommentById(
            @Parameter(description = "Comment ID") @PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(comment -> {
                    CommentResponseDTO responseDTO = commentMapper.toResponseDTO(comment);
                    return ResponseEntity.ok(responseDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "Get all comments", description = "Retrieve all comments with pagination")
    public ResponseEntity<Page<CommentResponseDTO>> getAllComments(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Comment> comments = commentService.getAllComments(pageable);
        Page<CommentResponseDTO> responseDTOs = comments.map(commentMapper::toResponseDTO);
        
        return ResponseEntity.ok(responseDTOs);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update comment", description = "Update an existing comment")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @Parameter(description = "Comment ID") @PathVariable Long id,
            @Valid @RequestBody CommentCreateDTO commentCreateDTO) {
        try {
            Comment commentDetails = commentMapper.toEntity(commentCreateDTO);
            Comment updatedComment = commentService.updateComment(id, commentDetails);
            CommentResponseDTO responseDTO = commentMapper.toResponseDTO(updatedComment);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete comment", description = "Delete a comment by ID")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "Comment ID") @PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Endpoints spécialisés
    
    @GetMapping("/game/{gameId}")
    @Operation(summary = "Get comments by game", description = "Retrieve all comments for a specific game")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByGame(
            @Parameter(description = "Game ID") @PathVariable Long gameId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        try {
            Game game = gameService.getGameById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
            Page<Comment> comments = commentService.getCommentsByGame(game, pageable);
            Page<CommentResponseDTO> responseDTOs = comments.map(commentMapper::toResponseDTO);
            
            return ResponseEntity.ok(responseDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/game/{gameId}/ordered")
    @Operation(summary = "Get comments by game ordered", description = "Retrieve all comments for a game ordered by creation time")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByGameOrdered(
            @Parameter(description = "Game ID") @PathVariable Long gameId) {
        
        try {
            Game game = gameService.getGameById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            
            List<Comment> comments = commentService.getCommentsByGameOrdered(game);
            List<CommentResponseDTO> responseDTOs = comments.stream()
                    .map(commentMapper::toResponseDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(responseDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/game/{gameId}/move/{moveNumber}")
    @Operation(summary = "Get comments by move", description = "Retrieve comments for a specific move in a game")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByMove(
            @Parameter(description = "Game ID") @PathVariable Long gameId,
            @Parameter(description = "Move number") @PathVariable Integer moveNumber) {
        
        try {
            Game game = gameService.getGameById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            
            List<Comment> comments = commentService.getCommentsByGameAndMove(game, moveNumber);
            List<CommentResponseDTO> responseDTOs = comments.stream()
                    .map(commentMapper::toResponseDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(responseDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get comments by author", description = "Retrieve all comments by a specific author")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByAuthor(
            @Parameter(description = "Author ID") @PathVariable Long authorId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        try {
            User author = userService.getUserById(authorId)
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Comment> comments = commentService.getCommentsByAuthor(author, pageable);
            Page<CommentResponseDTO> responseDTOs = comments.map(commentMapper::toResponseDTO);
            
            return ResponseEntity.ok(responseDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search comments", description = "Search comments by content keyword")
    public ResponseEntity<Page<CommentResponseDTO>> searchComments(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> comments = commentService.searchComments(keyword, pageable);
        Page<CommentResponseDTO> responseDTOs = comments.map(commentMapper::toResponseDTO);
        
        return ResponseEntity.ok(responseDTOs);
    }
    
    @GetMapping("/game/{gameId}/count")
    @Operation(summary = "Get comment count by game", description = "Get the total number of comments for a game")
    public ResponseEntity<Long> getCommentCountByGame(
            @Parameter(description = "Game ID") @PathVariable Long gameId) {
        
        try {
            Game game = gameService.getGameById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            
            Long count = commentService.getCommentCountByGame(game);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}