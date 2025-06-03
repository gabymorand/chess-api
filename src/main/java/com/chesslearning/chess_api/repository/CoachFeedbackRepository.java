package com.chesslearning.chess_api.repository;

import com.chesslearning.chess_api.entity.CoachFeedback;
import com.chesslearning.chess_api.entity.Game;
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
public interface CoachFeedbackRepository extends JpaRepository<CoachFeedback, Long> {
    
    // Feedbacks d'une partie
    List<CoachFeedback> findByGame(Game game);
    
    Page<CoachFeedback> findByGame(Game game, Pageable pageable);
    
    // Feedbacks d'un coach
    Page<CoachFeedback> findByCoach(User coach, Pageable pageable);
    
    // Feedbacks d'un coach pour une partie spécifique
    List<CoachFeedback> findByGameAndCoach(Game game, User coach);
    
    // Feedbacks récents
    @Query("SELECT cf FROM CoachFeedback cf WHERE cf.timestamp >= :since ORDER BY cf.timestamp DESC")
    Page<CoachFeedback> findRecentFeedbacks(@Param("since") LocalDateTime since, Pageable pageable);
    
    // Feedbacks d'un coach récents
    @Query("SELECT cf FROM CoachFeedback cf WHERE cf.coach = :coach AND cf.timestamp >= :since ORDER BY cf.timestamp DESC")
    Page<CoachFeedback> findRecentFeedbacksByCoach(@Param("coach") User coach, @Param("since") LocalDateTime since, Pageable pageable);
    
    // Parties avec feedbacks
    @Query("SELECT DISTINCT cf.game FROM CoachFeedback cf")
    Page<Game> findGamesWithFeedback(Pageable pageable);
    
    // Compter feedbacks par coach
    long countByCoach(User coach);
    
    // Compter feedbacks par partie
    long countByGame(Game game);
}