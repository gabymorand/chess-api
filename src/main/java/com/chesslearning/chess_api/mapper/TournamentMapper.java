package com.chesslearning.chess_api.mapper;

import com.chesslearning.chess_api.dto.TournamentCreateDTO;
import com.chesslearning.chess_api.dto.TournamentResponseDTO;
import com.chesslearning.chess_api.entity.Tournament;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TournamentMapper {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private GameMapper gameMapper;
    
    @Autowired
    private UserService userService;
    
    public Tournament toEntity(TournamentCreateDTO dto) {
        Tournament tournament = new Tournament();
        
        // Récupérer l'organisateur par ID
        User organizer = userService.getUserById(dto.getOrganizerId())
            .orElseThrow(() -> new RuntimeException("Organizer not found"));
        
        tournament.setName(dto.getName());
        tournament.setDescription(dto.getDescription());
        tournament.setStartDate(dto.getStartDate());
        tournament.setEndDate(dto.getEndDate());
        tournament.setMaxParticipants(dto.getMaxParticipants());
        tournament.setStatus(dto.getStatus());
        tournament.setOrganizer(organizer);
        
        return tournament;
    }
    
    public TournamentResponseDTO toResponseDTO(Tournament tournament) {
        return new TournamentResponseDTO(
            tournament.getId(),
            tournament.getName(),
            tournament.getDescription(),
            tournament.getStartDate(),
            tournament.getEndDate(),
            tournament.getMaxParticipants(),
            tournament.getStatus(),
            userMapper.toResponseDTO(tournament.getOrganizer()),
            tournament.getGames() != null ? 
                tournament.getGames().stream()
                    .map(gameMapper::toResponseDTO)
                    .collect(Collectors.toList()) : null,
            tournament.getCreatedAt(),
            tournament.getUpdatedAt()
        );
    }
}