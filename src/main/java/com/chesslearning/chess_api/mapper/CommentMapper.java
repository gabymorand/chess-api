package com.chesslearning.chess_api.mapper;

import com.chesslearning.chess_api.dto.CommentCreateDTO;
import com.chesslearning.chess_api.dto.CommentResponseDTO;
import com.chesslearning.chess_api.entity.Comment;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.service.GameService;
import com.chesslearning.chess_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private GameMapper gameMapper;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private GameService gameService;
    
    public Comment toEntity(CommentCreateDTO dto) {
        Comment comment = new Comment();
        
        // Récupérer la partie par ID
        Game game = gameService.getGameById(dto.getGameId())
            .orElseThrow(() -> new RuntimeException("Game not found"));
        
        // Récupérer l'auteur par ID
        User author = userService.getUserById(dto.getAuthorId())
            .orElseThrow(() -> new RuntimeException("Author not found"));
        
        comment.setGame(game);
        comment.setAuthor(author);
        comment.setContent(dto.getContent());
        comment.setMoveNumber(dto.getMoveNumber());
        
        return comment;
    }
    
    public CommentResponseDTO toResponseDTO(Comment comment) {
        return new CommentResponseDTO(
            comment.getId(),
            gameMapper.toResponseDTO(comment.getGame()),
            userMapper.toResponseDTO(comment.getAuthor()),
            comment.getContent(),
            comment.getMoveNumber(),
            comment.getCreatedAt(),
            comment.getUpdatedAt()
        );
    }
}