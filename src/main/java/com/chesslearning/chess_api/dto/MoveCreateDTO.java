package com.chesslearning.chess_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class MoveCreateDTO {
    
    @NotNull(message = "Game ID is required")
    private Long gameId;
    
    @NotNull(message = "Move number is required")
    private Integer moveNumber;
    
    @NotBlank(message = "Move notation is required")
    private String moveNotation;
    
    private String fromSquare;
    private String toSquare;
    private String pieceMoved;
    private String pieceCaptured;
    private Boolean isCheck = false;
    private Boolean isCheckmate = false;
    private Boolean isCastling = false;
    private LocalDateTime moveTime = LocalDateTime.now();
    

    public MoveCreateDTO() {}
    
    public Long getGameId() {
        return gameId;
    }
    
    public void setGameId(Long gameId) {
        this.gameId = gameId;
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
}