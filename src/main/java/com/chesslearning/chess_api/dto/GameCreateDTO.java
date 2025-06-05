package com.chesslearning.chess_api.dto;

import com.chesslearning.chess_api.entity.TimeControl;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO for creating a new game")
public class GameCreateDTO {
    
    @NotNull(message = "White player ID is required")
    @Schema(description = "ID of the white player", example = "1")
    private Long playerWhiteId;  
    
    @NotNull(message = "Black player ID is required")
    @Schema(description = "ID of the black player", example = "2") 
    private Long playerBlackId;  
    
    @NotNull(message = "Time control is required")
    @Schema(description = "Time control for the game", example = "BLITZ")
    private TimeControl timeControl;
    
    public GameCreateDTO() {}

    public Long getPlayerWhiteId() { return playerWhiteId; }
    public void setPlayerWhiteId(Long playerWhiteId) { this.playerWhiteId = playerWhiteId; }
    
    public Long getPlayerBlackId() { return playerBlackId; }
    public void setPlayerBlackId(Long playerBlackId) { this.playerBlackId = playerBlackId; }
    
    public TimeControl getTimeControl() { return timeControl; }
    public void setTimeControl(TimeControl timeControl) { this.timeControl = timeControl; }
}