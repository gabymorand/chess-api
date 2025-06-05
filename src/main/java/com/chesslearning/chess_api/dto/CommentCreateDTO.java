package com.chesslearning.chess_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for creating a new comment")
public class CommentCreateDTO {
    
    @NotNull(message = "Game ID is required")
    @Schema(description = "ID of the game to comment on", example = "12", required = true)
    private Long gameId;
    
    @NotNull(message = "Author ID is required")
    @Schema(description = "ID of the comment author", example = "13", required = true)
    private Long authorId;
    
    @NotBlank(message = "Comment content is required")
    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    @Schema(description = "Content of the comment", example = "Excellent opening! Classic e4 move.", required = true)
    private String content;
    
    @Schema(description = "Move number this comment refers to", example = "1")
    private Integer moveNumber;
    
    // Constructeurs et getters/setters
    public CommentCreateDTO() {}
    
    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }
    
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Integer getMoveNumber() { return moveNumber; }
    public void setMoveNumber(Integer moveNumber) { this.moveNumber = moveNumber; }
}