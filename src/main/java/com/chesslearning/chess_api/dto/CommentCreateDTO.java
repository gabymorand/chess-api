package com.chesslearning.chess_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentCreateDTO {
    
    @NotNull(message = "Game ID is required")
    private Long gameId;
    
    @NotNull(message = "Author ID is required")
    private Long authorId;
    
    @NotBlank(message = "Comment content is required")
    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    private String content;
    
    private Integer moveNumber;
    
    // Constructeurs
    public CommentCreateDTO() {}
    
    public CommentCreateDTO(Long gameId, Long authorId, String content, Integer moveNumber) {
        this.gameId = gameId;
        this.authorId = authorId;
        this.content = content;
        this.moveNumber = moveNumber;
    }
    
    // Getters and Setters
    public Long getGameId() {
        return gameId;
    }
    
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
    
    public Long getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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
}