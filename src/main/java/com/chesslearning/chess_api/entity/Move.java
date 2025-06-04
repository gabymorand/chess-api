package com.chesslearning.chess_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "moves")
public class Move {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    
    @NotNull(message = "Move number is required")
    @Column(name = "move_number", nullable = false)
    private Integer moveNumber;
    
    @NotBlank(message = "Move notation is required")
    @Column(name = "move_notation", nullable = false)
    private String moveNotation;
    
    @Column(name = "from_square")
    private String fromSquare;
    
    @Column(name = "to_square")
    private String toSquare;
    
    @Column(name = "piece_moved")
    private String pieceMoved;
    
    @Column(name = "piece_captured")
    private String pieceCaptured;
    
    @Column(name = "is_check")
    private Boolean isCheck = false;
    
    @Column(name = "is_checkmate")
    private Boolean isCheckmate = false;
    
    @Column(name = "is_castling")
    private Boolean isCastling = false;
    
    @Column(name = "move_time")
    private LocalDateTime moveTime;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructeurs
    public Move() {
        this.createdAt = LocalDateTime.now();
        this.moveTime = LocalDateTime.now();
    }
    
    public Move(Game game, Integer moveNumber, String moveNotation, String fromSquare, String toSquare) {
        this();
        this.game = game;
        this.moveNumber = moveNumber;
        this.moveNotation = moveNotation;
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Game getGame() {
        return game;
    }
    
    public void setGame(Game game) {
        this.game = game;
    }
    
    public Integer getMoveNumber() {
        return moveNumber;
    }
    
    public void setMoveNumber(Integer moveNumber) {
        this.moveNumber = moveNumber;
    }
    
    public String getMoveNotation() {
        return moveNotation;
    }
    
    public void setMoveNotation(String moveNotation) {
        this.moveNotation = moveNotation;
    }
    
    public String getFromSquare() {
        return fromSquare;
    }
    
    public void setFromSquare(String fromSquare) {
        this.fromSquare = fromSquare;
    }
    
    public String getToSquare() {
        return toSquare;
    }
    
    public void setToSquare(String toSquare) {
        this.toSquare = toSquare;
    }
    
    public String getPieceMoved() {
        return pieceMoved;
    }
    
    public void setPieceMoved(String pieceMoved) {
        this.pieceMoved = pieceMoved;
    }
    
    public String getPieceCaptured() {
        return pieceCaptured;
    }
    
    public void setPieceCaptured(String pieceCaptured) {
        this.pieceCaptured = pieceCaptured;
    }
    
    public Boolean getIsCheck() {
        return isCheck;
    }
    
    public void setIsCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }
    
    public Boolean getIsCheckmate() {
        return isCheckmate;
    }
    
    public void setIsCheckmate(Boolean isCheckmate) {
        this.isCheckmate = isCheckmate;
    }
    
    public Boolean getIsCastling() {
        return isCastling;
    }
    
    public void setIsCastling(Boolean isCastling) {
        this.isCastling = isCastling;
    }
    
    public LocalDateTime getMoveTime() {
        return moveTime;
    }
    
    public void setMoveTime(LocalDateTime moveTime) {
        this.moveTime = moveTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}