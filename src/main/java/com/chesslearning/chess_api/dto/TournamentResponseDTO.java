package com.chesslearning.chess_api.dto;

import com.chesslearning.chess_api.entity.TournamentStatus;
import java.time.LocalDateTime;
import java.util.List;

public class TournamentResponseDTO {
    
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxParticipants;
    private TournamentStatus status;
    private UserResponseDTO organizer;
    private List<GameResponseDTO> games;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public TournamentResponseDTO() {}
    
    public TournamentResponseDTO(Long id, String name, String description, LocalDateTime startDate,
                               LocalDateTime endDate, Integer maxParticipants, TournamentStatus status,
                               UserResponseDTO organizer, List<GameResponseDTO> games,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxParticipants = maxParticipants;
        this.status = status;
        this.organizer = organizer;
        this.games = games;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public Integer getMaxParticipants() {
        return maxParticipants;
    }
    
    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
    
    public TournamentStatus getStatus() {
        return status;
    }
    
    public void setStatus(TournamentStatus status) {
        this.status = status;
    }
    
    public UserResponseDTO getOrganizer() {
        return organizer;
    }
    
    public void setOrganizer(UserResponseDTO organizer) {
        this.organizer = organizer;
    }
    
    public List<GameResponseDTO> getGames() {
        return games;
    }
    
    public void setGames(List<GameResponseDTO> games) {
        this.games = games;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}