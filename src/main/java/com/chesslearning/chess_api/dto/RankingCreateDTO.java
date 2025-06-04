package com.chesslearning.chess_api.dto;

import jakarta.validation.constraints.NotNull;

public class RankingCreateDTO {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private Integer eloRating = 1200;
    private Integer gamesPlayed = 0;
    private Integer gamesWon = 0;
    private Integer gamesLost = 0;
    private Integer gamesDrawn = 0;
    
    // Constructeurs
    public RankingCreateDTO() {}
    
    public RankingCreateDTO(Long userId, Integer eloRating) {
        this.userId = userId;
        this.eloRating = eloRating;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getEloRating() {
        return eloRating;
    }
    
    public void setEloRating(Integer eloRating) {
        this.eloRating = eloRating;
    }
    
    public Integer getGamesPlayed() {
        return gamesPlayed;
    }
    
    public void setGamesPlayed(Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    
    public Integer getGamesWon() {
        return gamesWon;
    }
    
    public void setGamesWon(Integer gamesWon) {
        this.gamesWon = gamesWon;
    }
    
    public Integer getGamesLost() {
        return gamesLost;
    }
    
    public void setGamesLost(Integer gamesLost) {
        this.gamesLost = gamesLost;
    }
    
    public Integer getGamesDrawn() {
        return gamesDrawn;
    }
    
    public void setGamesDrawn(Integer gamesDrawn) {
        this.gamesDrawn = gamesDrawn;
    }
}