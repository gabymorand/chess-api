package com.chesslearning.chess_api.dto;

import java.time.LocalDateTime;

public class MoveResponseDTO {
    
    private Long id;
    private GameResponseDTO game;
    private Integer moveNumber;
    private String moveNotation;
    private String fromSquare;
    private String toSquare;
    private String pieceMoved;
    private String pieceCaptured;
    private Boolean isCheck;
    private Boolean isCheckmate;
    private Boolean isCastling;
    private LocalDateTime moveTime;
    private LocalDateTime createdAt;
    

    public MoveResponseDTO() {}
    
    public MoveResponseDTO(Long id, GameResponseDTO game, Integer moveNumber, String moveNotation,
                          String fromSquare, String toSquare, String pieceMoved, String pieceCaptured,
                          Boolean isCheck, Boolean isCheckmate, Boolean isCastling,
                          LocalDateTime moveTime, LocalDateTime createdAt) {
        this.id = id;
        this.game = game;
        this.moveNumber = moveNumber;
        this.moveNotation = moveNotation;
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
        this.pieceMoved = pieceMoved;
        this.pieceCaptured = pieceCaptured;
        this.isCheck = isCheck;
        this.isCheckmate = isCheckmate;
        this.isCastling = isCastling;
        this.moveTime = moveTime;
        this.createdAt = createdAt;
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