package com.chesslearning.chess_api.dto;

import com.chesslearning.chess_api.entity.GameResult;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class GameCreateDTO {
    
    @NotNull(message = "White player ID is required")
    private Long playerWhiteId;
    
    @NotNull(message = "Black player ID is required")
    private Long playerBlackId;
    
    private GameResult result = GameResult.ONGOING;
    private String pgnData;
    private LocalDateTime gameDate = LocalDateTime.now();
    
    // Constructeurs
    public GameCreateDTO() {}
    
    public GameCreateDTO(Long playerWhiteId, Long playerBlackId, GameResult result, String pgnData, LocalDateTime gameDate) {
        this.playerWhiteId = playerWhiteId;
        this.playerBlackId = playerBlackId;
        this.result = result;
        this.pgnData = pgnData;
        this.gameDate = gameDate;
    }
    
    // Getters and Setters
    public Long getPlayerWhiteId() {
        return playerWhiteId;
    }
    
    public void setPlayerWhiteId(Long playerWhiteId) {
        this.playerWhiteId = playerWhiteId;
    }
    
    public Long getPlayerBlackId() {
        return playerBlackId;
    }
    
    public void setPlayerBlackId(Long playerBlackId) {
        this.playerBlackId = playerBlackId;
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
}