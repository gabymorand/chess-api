package com.chesslearning.chess_api.mapper;

import com.chesslearning.chess_api.dto.GameCreateDTO;
import com.chesslearning.chess_api.dto.GameResponseDTO;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.GameResult;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GameMapper {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserService userService;
    
    public Game toEntity(GameCreateDTO dto) {
        Game game = new Game();
        
        User playerWhite = userService.getUserById(dto.getPlayerWhiteId())
            .orElseThrow(() -> new RuntimeException("White player not found"));
        User playerBlack = userService.getUserById(dto.getPlayerBlackId())
            .orElseThrow(() -> new RuntimeException("Black player not found"));
        
        game.setPlayerWhite(playerWhite);
        game.setPlayerBlack(playerBlack);
        game.setTimeControl(dto.getTimeControl());
        game.setResult(GameResult.ONGOING);
        game.setGameDate(LocalDateTime.now());
        //ca planter ici a cause de la date -> dt 
        
        return game;
    }
    
    public GameResponseDTO toResponseDTO(Game game) {
        return new GameResponseDTO(
            game.getId(),
            userMapper.toResponseDTO(game.getPlayerWhite()),
            userMapper.toResponseDTO(game.getPlayerBlack()),
            game.getResult(),
            game.getTimeControl(),
            game.getPgnData(),
            game.getGameDate(),
            game.getCreatedAt(),
            game.getUpdatedAt()
        );
    }
}