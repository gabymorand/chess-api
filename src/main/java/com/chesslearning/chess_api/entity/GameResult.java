package com.chesslearning.chess_api.entity;

public enum GameResult {
    WHITE_WINS("1-0"),
    BLACK_WINS("0-1"), 
    DRAW("1/2-1/2"),
    ONGOING("*");
    
    private final String pgnNotation;
    
    GameResult(String pgnNotation) {
        this.pgnNotation = pgnNotation;
    }
    
    public String getPgnNotation() {
        return pgnNotation;
    }
}