package com.chesslearning.chess_api.mapper;

import com.chesslearning.chess_api.dto.RankingCreateDTO;
import com.chesslearning.chess_api.dto.RankingResponseDTO;
import com.chesslearning.chess_api.entity.Ranking;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RankingMapper {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserService userService;
    
    public Ranking toEntity(RankingCreateDTO dto) {
        Ranking ranking = new Ranking();
        
        // Récupérer l'utilisateur par ID
        User user = userService.getUserById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        ranking.setUser(user);
        ranking.setEloRating(dto.getEloRating());
        ranking.setGamesPlayed(dto.getGamesPlayed());
        ranking.setGamesWon(dto.getGamesWon());
        ranking.setGamesLost(dto.getGamesLost());
        ranking.setGamesDrawn(dto.getGamesDrawn());
        ranking.calculateWinRate();
        
        return ranking;
    }
    
    public RankingResponseDTO toResponseDTO(Ranking ranking) {
        return new RankingResponseDTO(
            ranking.getId(),
            userMapper.toResponseDTO(ranking.getUser()),
            ranking.getEloRating(),
            ranking.getGamesPlayed(),
            ranking.getGamesWon(),
            ranking.getGamesLost(),
            ranking.getGamesDrawn(),
            ranking.getWinRate(),
            ranking.getRankPosition(),
            ranking.getCreatedAt(),
            ranking.getUpdatedAt()
        );
    }
}