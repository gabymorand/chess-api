package com.chesslearning.chess_api.dto;

public class ChessAnalysisResponse {
    
    private String analysis;
    private String type; //  j'ai mis les type "chat", "analysis", "suggestion", "explanation", "quiz", "tips"
    private Long gameId;
    
    public ChessAnalysisResponse() {}
    
    public ChessAnalysisResponse(String analysis, String type) {
        this.analysis = analysis;
        this.type = type;
    }
    
    public ChessAnalysisResponse(String analysis, String type, Long gameId) {
        this.analysis = analysis;
        this.type = type;
        this.gameId = gameId;
    }
    
    public String getAnalysis() {
        return analysis;
    }
    
    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getGameId() {
        return gameId;
    }
    
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}