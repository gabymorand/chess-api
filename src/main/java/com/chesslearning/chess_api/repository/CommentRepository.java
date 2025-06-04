package com.chesslearning.chess_api.repository;

import com.chesslearning.chess_api.entity.Comment;
import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    Page<Comment> findByGame(Game game, Pageable pageable);
    
    Page<Comment> findByAuthor(User author, Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE c.game = :game AND c.moveNumber = :moveNumber")
    List<Comment> findByGameAndMoveNumber(@Param("game") Game game, @Param("moveNumber") Integer moveNumber);
    
    @Query("SELECT c FROM Comment c WHERE c.game = :game ORDER BY c.createdAt ASC")
    List<Comment> findByGameOrderByCreatedAtAsc(@Param("game") Game game);
    
    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:keyword%")
    Page<Comment> findByContentContaining(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.game = :game")
    Long countByGame(@Param("game") Game game);
}