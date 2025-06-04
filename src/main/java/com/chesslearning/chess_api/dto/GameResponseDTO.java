package com.chesslearning.chess_api.dto;

import com.chesslearning.chess_api.entity.GameResult;
import java.time.LocalDateTime;

public class GameResponseDTO {
    
    private Long id;
    private UserResponseDTO playerWhite;
    private UserResponseDTO playerBlack;
    private GameResult result;
    private String pgnData;
    private LocalDateTime gameDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public GameResponseDTO() {}
    
    public GameResponseDTO(Long id, UserResponseDTO playerWhite, UserResponseDTO playerBlack, 
                          GameResult result, String pgnData, LocalDateTime gameDate,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        this.result = result;
        this.pgnData = pgnData;
        this.gameDate = gameDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserResponseDTO getPlayerWhite() {
        return playerWhite;
    }
    
    public void setPlayerWhite(UserResponseDTO playerWhite) {
        this.playerWhite = playerWhite;
    }
    
    public UserResponseDTO getPlayerBlack() {
        return playerBlack;
    }
    
    public void setPlayerBlack(UserResponseDTO playerBlack) {
        this.playerBlack = playerBlack;
    }
    
    public GameResult getResult() {
        return result;
    }
    
    public void setResult(GameResult result) {
        this.result = result;
    }
    
    public String getPgnData() {
        return pgnData;
    }
    
    public void setPgnData(String pgnData) {
        this.pgnData = pgnData;
    }
    
    public LocalDateTime getGameDate() {
        return gameDate;
    }
    
    public void setGameDate(LocalDateTime gameDate) {
        this.gameDate = gameDate;
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