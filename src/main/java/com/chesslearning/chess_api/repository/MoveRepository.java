package com.chesslearning.chess_api.repository;

import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.Move;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {
    
    Page<Move> findByGame(Game game, Pageable pageable);
    
    List<Move> findByGameOrderByMoveNumberAsc(Game game);
    
    @Query("SELECT m FROM Move m WHERE m.game = :game AND m.moveNumber = :moveNumber")
    Move findByGameAndMoveNumber(@Param("game") Game game, @Param("moveNumber") Integer moveNumber);
    
    @Query("SELECT m FROM Move m WHERE m.pieceMoved = :piece")
    Page<Move> findByPieceMoved(@Param("piece") String piece, Pageable pageable);
    
    @Query("SELECT m FROM Move m WHERE m.isCheck = true")
    Page<Move> findCheckMoves(Pageable pageable);
    
    @Query("SELECT m FROM Move m WHERE m.isCheckmate = true")
    Page<Move> findCheckmateMoves(Pageable pageable);
    
    @Query("SELECT m FROM Move m WHERE m.isCastling = true")
    Page<Move> findCastlingMoves(Pageable pageable);
}