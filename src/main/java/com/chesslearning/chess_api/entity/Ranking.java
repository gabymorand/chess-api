package com.chesslearning.chess_api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rankings")
public class Ranking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(name = "elo_rating")
    private Integer eloRating = 1200;
    
    @Column(name = "games_played")
    private Integer gamesPlayed = 0;
    
    @Column(name = "games_won")
    private Integer gamesWon = 0;
    
    @Column(name = "games_lost")
    private Integer gamesLost = 0;
    
    @Column(name = "games_drawn")
    private Integer gamesDrawn = 0;
    
    @Column(name = "win_rate")
    private Double winRate = 0.0;
    
    @Column(name = "rank_position")
    private Integer rankPosition;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructeurs
    public Ranking() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Ranking(User user, Integer eloRating) {
        this();
        this.user = user;
        this.eloRating = eloRating;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.calculateWinRate();
    }
    
    public void calculateWinRate() {
        if (gamesPlayed > 0) {
            this.winRate = (double) gamesWon / gamesPlayed * 100;
        } else {
            this.winRate = 0.0;
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
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