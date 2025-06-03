package com.chesslearning.chess_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuggestionType suggestionType;
    
    @NotBlank(message = "Recommendation text is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    
    @Column(name = "generated_by_ai", nullable = false)
    private Boolean generatedByAi = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public Recommendation() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Recommendation(User user, SuggestionType suggestionType, String text, Boolean generatedByAi) {
        this();
        this.user = user;
        this.suggestionType = suggestionType;
        this.text = text;
        this.generatedByAi = generatedByAi;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public SuggestionType getSuggestionType() {
        return suggestionType;
    }
    
    public void setSuggestionType(SuggestionType suggestionType) {
        this.suggestionType = suggestionType;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Boolean getGeneratedByAi() {
        return generatedByAi;
    }
    
    public void setGeneratedByAi(Boolean generatedByAi) {
        this.generatedByAi = generatedByAi;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}