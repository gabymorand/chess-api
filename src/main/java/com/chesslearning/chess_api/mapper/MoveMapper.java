package com.chesslearning.chess_api.mapper;

import com.chesslearning.chess_api.dto.MoveCreateDTO;
import com.chesslearning.chess_api.dto.MoveResponseDTO;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.Move;
import com.chesslearning.chess_api.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoveMapper {
    
    @Autowired
    private GameMapper gameMapper;
    
    @Autowired
    private GameService gameService;
    
    public Move toEntity(MoveCreateDTO dto) {
        Move move = new Move();
        
        // Récupérer la partie par ID
        Game game = gameService.getGameById(dto.getGameId())
            .orElseThrow(() -> new RuntimeException("Game not found"));
        
        move.setGame(game);
        move.setMoveNumber(dto.getMoveNumber());
        move.setMoveNotation(dto.getMoveNotation());
        move.setFromSquare(dto.getFromSquare());
        move.setToSquare(dto.getToSquare());
        move.setPieceMoved(dto.getPieceMoved());
        move.setPieceCaptured(dto.getPieceCaptured());
        move.setIsCheck(dto.getIsCheck());
        move.setIsCheckmate(dto.getIsCheckmate());
        move.setIsCastling(dto.getIsCastling());
        move.setMoveTime(dto.getMoveTime());
        
        return move;
    }
    
    public MoveResponseDTO toResponseDTO(Move move) {
        return new MoveResponseDTO(
            move.getId(),
            gameMapper.toResponseDTO(move.getGame()),
            move.getMoveNumber(),
            move.getMoveNotation(),
            move.getFromSquare(),
            move.getToSquare(),
            move.getPieceMoved(),
            move.getPieceCaptured(),
            move.getIsCheck(),
            move.getIsCheckmate(),
            move.getIsCastling(),
            move.getMoveTime(),
            move.getCreatedAt()
        );
    }
}