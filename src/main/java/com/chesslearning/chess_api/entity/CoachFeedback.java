package com.chesslearning.chess_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "coach_feedbacks")
public class CoachFeedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private User coach;
    
    @NotBlank(message = "Comment is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    // Constructors
    public CoachFeedback() {
        this.timestamp = LocalDateTime.now();
    }
    
    public CoachFeedback(Game game, User coach, String comment) {
        this();
        this.game = game;
        this.coach = coach;
        this.comment = comment;
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
    
    public User getCoach() {
        return coach;
    }
    
    public void setCoach(User coach) {
        this.coach = coach;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}