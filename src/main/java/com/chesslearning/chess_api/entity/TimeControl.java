package com.chesslearning.chess_api.entity;

public enum TimeControl {
    BULLET("Bullet (1+0, 2+1)"),
    BLITZ("Blitz (3+0, 3+2, 5+0)"),
    RAPID("Rapid (10+0, 15+10, 30+0)"),
    CLASSICAL("Classical (90+30, 120+0)"),
    CORRESPONDENCE("Correspondence (Days per move)");
    
    private final String description;
    
    TimeControl(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}