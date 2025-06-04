package com.chesslearning.chess_api.dto;

import java.time.LocalDateTime;

public class CommentResponseDTO {
    
    private Long id;
    private GameResponseDTO game;
    private UserResponseDTO author;
    private String content;
    private Integer moveNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    

    public CommentResponseDTO() {}
    
    public CommentResponseDTO(Long id, GameResponseDTO game, UserResponseDTO author, String content,
                             Integer moveNumber, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.game = game;
        this.author = author;
        this.content = content;
        this.moveNumber = moveNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public GameResponseDTO getGame() {
        return game;
    }
    
    public void setGame(GameResponseDTO game) {
        this.game = game;
    }
    
    public UserResponseDTO getAuthor() {
        return author;
    }
    
    public void setAuthor(UserResponseDTO author) {
        this.author = author;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getMoveNumber() {
        return moveNumber;
    }
    
    public void setMoveNumber(Integer moveNumber) {
        this.moveNumber = moveNumber;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}