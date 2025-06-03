package com.chesslearning.chess_api.repository;

import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.GameResult;
import com.chesslearning.chess_api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    
    // Parties d'un joueur (blanc ou noir)
    @Query("SELECT g FROM Game g WHERE g.playerWhite = :player OR g.playerBlack = :player")
    Page<Game> findByPlayer(@Param("player") User player, Pageable pageable);
    
    // Parties par résultat
    Page<Game> findByResult(GameResult result, Pageable pageable);
    
    // Parties entre deux joueurs
    @Query("SELECT g FROM Game g WHERE (g.playerWhite = :player1 AND g.playerBlack = :player2) OR (g.playerWhite = :player2 AND g.playerBlack = :player1)")
    List<Game> findGamesBetweenPlayers(@Param("player1") User player1, @Param("player2") User player2);
    
    // Parties par période
    Page<Game> findByGameDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Statistiques d'un joueur
    @Query("SELECT COUNT(g) FROM Game g WHERE (g.playerWhite = :player AND g.result = 'WHITE_WINS') OR (g.playerBlack = :player AND g.result = 'BLACK_WINS')")
    long countWinsByPlayer(@Param("player") User player);
    
    @Query("SELECT COUNT(g) FROM Game g WHERE g.playerWhite = :player OR g.playerBlack = :player")
    long countTotalGamesByPlayer(@Param("player") User player);
}