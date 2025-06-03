package com.chesslearning.chess_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "games")
public class Game {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_white_id", nullable = false)
    private User playerWhite;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_black_id", nullable = false)
    private User playerBlack;
    
    @Enumerated(EnumType.STRING)
    private GameResult result;
    
    @Column(columnDefinition = "TEXT")
    @NotNull(message = "PGN data is required")
    @Size(min = 10, message = "PGN data too short")
    private String pgnData;
    
    @Column(name = "game_date", nullable = false)
    private LocalDateTime gameDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public Game() {
        this.createdAt = LocalDateTime.now();
        this.gameDate = LocalDateTime.now();
    }
    
    public Game(User playerWhite, User playerBlack, String pgnData) {
        this();
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        this.pgnData = pgnData;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getPlayerWhite() {
        return playerWhite;
    }
    
    public void setPlayerWhite(User playerWhite) {
        this.playerWhite = playerWhite;
    }
    
    public User getPlayerBlack() {
        return playerBlack;
    }
    
    public void setPlayerBlack(User playerBlack) {
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
}