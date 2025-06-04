package com.chesslearning.chess_api.dto;

import java.time.LocalDateTime;

public class RankingResponseDTO {
    
    private Long id;
    private UserResponseDTO user;
    private Integer eloRating;
    private Integer gamesPlayed;
    private Integer gamesWon;
    private Integer gamesLost;
    private Integer gamesDrawn;
    private Double winRate;
    private Integer rankPosition;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public RankingResponseDTO() {}
    
    public RankingResponseDTO(Long id, UserResponseDTO user, Integer eloRating, Integer gamesPlayed,
                             Integer gamesWon, Integer gamesLost, Integer gamesDrawn, Double winRate,
                             Integer rankPosition, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.eloRating = eloRating;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.gamesDrawn = gamesDrawn;
        this.winRate = winRate;
        this.rankPosition = rankPosition;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserResponseDTO getUser() {
        return user;
    }
    
    public void setUser(UserResponseDTO user) {
        this.user = user;
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
    
    public Double getWinRate() {
        return winRate;
    }
    
    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }
    
    public Integer getRankPosition() {
        return rankPosition;
    }
    
    public void setRankPosition(Integer rankPosition) {
        this.rankPosition = rankPosition;
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