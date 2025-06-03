package com.chesslearning.chess_api.repository;

import com.chesslearning.chess_api.entity.Recommendation;
import com.chesslearning.chess_api.entity.SuggestionType;
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
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    
    // Recommandations d'un utilisateur
    Page<Recommendation> findByUser(User user, Pageable pageable);
    
    // Recommandations par type
    Page<Recommendation> findBySuggestionType(SuggestionType suggestionType, Pageable pageable);
    
    // Recommandations d'un utilisateur par type
    Page<Recommendation> findByUserAndSuggestionType(User user, SuggestionType suggestionType, Pageable pageable);
    
    // Recommandations générées par IA
    Page<Recommendation> findByGeneratedByAi(Boolean generatedByAi, Pageable pageable);
    
    // Recommandations d'un utilisateur générées par IA
    Page<Recommendation> findByUserAndGeneratedByAi(User user, Boolean generatedByAi, Pageable pageable);
    
    // Recommandations récentes
    @Query("SELECT r FROM Recommendation r WHERE r.createdAt >= :since ORDER BY r.createdAt DESC")
    Page<Recommendation> findRecentRecommendations(@Param("since") LocalDateTime since, Pageable pageable);
    
    // Recommandations récentes pour un utilisateur
    @Query("SELECT r FROM Recommendation r WHERE r.user = :user AND r.createdAt >= :since ORDER BY r.createdAt DESC")
    List<Recommendation> findRecentRecommendationsForUser(@Param("user") User user, @Param("since") LocalDateTime since);
    
    // Compter par type
    long countBySuggestionType(SuggestionType suggestionType);
    
    // Compter par utilisateur
    long countByUser(User user);
    
    // Compter générées par IA
    long countByGeneratedByAi(Boolean generatedByAi);
}