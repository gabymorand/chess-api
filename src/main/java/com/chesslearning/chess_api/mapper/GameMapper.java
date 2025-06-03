package com.chesslearning.chess_api.mapper;

import com.chesslearning.chess_api.dto.GameCreateDTO;
import com.chesslearning.chess_api.dto.GameResponseDTO;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserService userService;
    
    public Game toEntity(GameCreateDTO dto) {
        Game game = new Game();
        
        // Récupérer les utilisateurs par ID
        User playerWhite = userService.getUserById(dto.getPlayerWhiteId())
            .orElseThrow(() -> new RuntimeException("White player not found"));
        User playerBlack = userService.getUserById(dto.getPlayerBlackId())
            .orElseThrow(() -> new RuntimeException("Black player not found"));
        
        game.setPlayerWhite(playerWhite);
        game.setPlayerBlack(playerBlack);
        game.setResult(dto.getResult());
        game.setPgnData(dto.getPgnData());
        game.setGameDate(dto.getGameDate());
        
        return game;
    }
    
    public GameResponseDTO toResponseDTO(Game game) {
        return new GameResponseDTO(
            game.getId(),
            userMapper.toResponseDTO(game.getPlayerWhite()),
            userMapper.toResponseDTO(game.getPlayerBlack()),
            game.getResult(),
            game.getPgnData(),
            game.getGameDate(),
            game.getCreatedAt(),
            game.getUpdatedAt()
        );
    }
}