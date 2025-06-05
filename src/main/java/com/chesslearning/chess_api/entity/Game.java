package com.chesslearning.chess_api.entity;

import jakarta.persistence.*;
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
    @Column(nullable = false)
    private GameResult result;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "time_control", nullable = false)
    private TimeControl timeControl;
    
    @Column(columnDefinition = "TEXT")
    private String pgnData;
    
    @Column(name = "game_date")
    private LocalDateTime gameDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructeurs
    public Game() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.gameDate = LocalDateTime.now();
        this.result = GameResult.ONGOING;
    }
    
    public Game(User playerWhite, User playerBlack, TimeControl timeControl) {
        this();
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        this.timeControl = timeControl;
    }
    
    public Game(User playerWhite, User playerBlack, GameResult result, TimeControl timeControl, String pgnData, LocalDateTime gameDate) {
        this();
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        this.result = result;
        this.timeControl = timeControl;
        this.pgnData = pgnData;
        this.gameDate = gameDate;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters et Setters
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
    
    public TimeControl getTimeControl() {
        return timeControl;
    }
    
    public void setTimeControl(TimeControl timeControl) {
        this.timeControl = timeControl;
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
    
    public Tournament getTournament() {
        return tournament;
    }
    
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
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