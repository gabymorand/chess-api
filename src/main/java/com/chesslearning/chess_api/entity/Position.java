package com.chesslearning.chess_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Entity
@Table(name = "positions")
public class Position {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "FEN notation is required")
    @Pattern(regexp = "^[rnbqkpRNBQKP1-8/\\s\\-w]+$", message = "Invalid FEN format")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String fen;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public Position() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Position(String fen, String description) {
        this();
        this.fen = fen;
        this.description = description;
    }
    
    public Position(String fen, String description, Lesson lesson) {
        this(fen, description);
        this.lesson = lesson;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFen() {
        return fen;
    }
    
    public void setFen(String fen) {
        this.fen = fen;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Lesson getLesson() {
        return lesson;
    }
    
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}