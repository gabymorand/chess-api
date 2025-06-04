package com.chesslearning.chess_api.service;

import com.chesslearning.chess_api.entity.Comment;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }
    
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }
    
    public Page<Comment> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }
    
    public Comment updateComment(Long id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        
        comment.setContent(commentDetails.getContent());
        comment.setMoveNumber(commentDetails.getMoveNumber());
        
        return commentRepository.save(comment);
    }
    
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        commentRepository.delete(comment);
    }
    
    public Page<Comment> getCommentsByGame(Game game, Pageable pageable) {
        return commentRepository.findByGame(game, pageable);
    }
    
    public Page<Comment> getCommentsByAuthor(User author, Pageable pageable) {
        return commentRepository.findByAuthor(author, pageable);
    }
    
    public List<Comment> getCommentsByGameAndMove(Game game, Integer moveNumber) {
        return commentRepository.findByGameAndMoveNumber(game, moveNumber);
    }
    
    public List<Comment> getCommentsByGameOrdered(Game game) {
        return commentRepository.findByGameOrderByCreatedAtAsc(game);
    }
    
    public Page<Comment> searchComments(String keyword, Pageable pageable) {
        return commentRepository.findByContentContaining(keyword, pageable);
    }
    
    public Long getCommentCountByGame(Game game) {
        return commentRepository.countByGame(game);
    }
}