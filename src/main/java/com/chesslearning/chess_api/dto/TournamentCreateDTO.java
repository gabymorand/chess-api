package com.chesslearning.chess_api.dto;

import com.chesslearning.chess_api.entity.TournamentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "DTO for creating a new tournament")
public class TournamentCreateDTO {
    
    @NotBlank(message = "Tournament name is required")
    @Size(max = 100, message = "Tournament name must not exceed 100 characters")
    @Schema(description = "Name of the tournament", example = "Championship Local 2025", required = true)
    private String name;
    
    @Schema(description = "Description of the tournament", example = "Annual championship tournament")
    private String description;
    
    @NotNull(message = "Start date is required")
    @Schema(description = "Tournament start date", example = "2025-07-01T10:00:00", required = true)
    private LocalDateTime startDate;
    
    @NotNull(message = "End date is required")
    @Schema(description = "Tournament end date", example = "2025-07-03T18:00:00", required = true)
    private LocalDateTime endDate;
    
    @Schema(description = "Maximum number of participants", example = "16")
    private Integer maxParticipants;
    
    @Schema(description = "Tournament status", example = "UPCOMING")
    private TournamentStatus status = TournamentStatus.UPCOMING;
    
    @NotNull(message = "Organizer ID is required")
    @Schema(description = "ID of the tournament organizer", example = "13", required = true)
    private Long organizerId;

    public TournamentCreateDTO() {}
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    
    public TournamentStatus getStatus() { return status; }
    public void setStatus(TournamentStatus status) { this.status = status; }
    
    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }
}