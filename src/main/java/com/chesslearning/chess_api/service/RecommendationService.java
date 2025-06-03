package com.chesslearning.chess_api.service;

import com.chesslearning.chess_api.entity.Recommendation;
import com.chesslearning.chess_api.entity.SuggestionType;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RecommendationService {
    
    @Autowired
    private RecommendationRepository recommendationRepository;
    
    // CRUD operations
    public Recommendation createRecommendation(Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }
    
    public Optional<Recommendation> getRecommendationById(Long id) {
        return recommendationRepository.findById(id);
    }
    
    public Page<Recommendation> getAllRecommendations(Pageable pageable) {
        return recommendationRepository.findAll(pageable);
    }
    
    public Recommendation updateRecommendation(Long id, Recommendation recommendationDetails) {
        Recommendation recommendation = recommendationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Recommendation not found with id: " + id));
        
        recommendation.setSuggestionType(recommendationDetails.getSuggestionType());
        recommendation.setText(recommendationDetails.getText());
        recommendation.setGeneratedByAi(recommendationDetails.getGeneratedByAi());
        
        return recommendationRepository.save(recommendation);
    }
    
    public void deleteRecommendation(Long id) {
        if (!recommendationRepository.existsById(id)) {
            throw new RuntimeException("Recommendation not found with id: " + id);
        }
        recommendationRepository.deleteById(id);
    }
    
    // Méthodes spécifiques
    public Page<Recommendation> getRecommendationsByUser(User user, Pageable pageable) {
        return recommendationRepository.findByUser(user, pageable);
    }
    
    public Page<Recommendation> getRecommendationsByType(SuggestionType suggestionType, Pageable pageable) {
        return recommendationRepository.findBySuggestionType(suggestionType, pageable);
    }
    
    public Page<Recommendation> getRecommendationsByUserAndType(User user, SuggestionType suggestionType, Pageable pageable) {
        return recommendationRepository.findByUserAndSuggestionType(user, suggestionType, pageable);
    }
    
    public Page<Recommendation> getAiRecommendations(Boolean generatedByAi, Pageable pageable) {
        return recommendationRepository.findByGeneratedByAi(generatedByAi, pageable);
    }
    
    public Page<Recommendation> getUserAiRecommendations(User user, Boolean generatedByAi, Pageable pageable) {
        return recommendationRepository.findByUserAndGeneratedByAi(user, generatedByAi, pageable);
    }
    
    public Page<Recommendation> getRecentRecommendations(LocalDateTime since, Pageable pageable) {
        return recommendationRepository.findRecentRecommendations(since, pageable);
    }
    
    public List<Recommendation> getRecentRecommendationsForUser(User user, LocalDateTime since) {
        return recommendationRepository.findRecentRecommendationsForUser(user, since);
    }
    
    // Statistiques
    public long countRecommendationsByType(SuggestionType suggestionType) {
        return recommendationRepository.countBySuggestionType(suggestionType);
    }
    
    public long countRecommendationsByUser(User user) {
        return recommendationRepository.countByUser(user);
    }
    
    public long countAiRecommendations(Boolean generatedByAi) {
        return recommendationRepository.countByGeneratedByAi(generatedByAi);
    }
    
    // Génération de recommandations (pour plus tard avec Mistral AI)
    public Recommendation generateAiRecommendation(User user, SuggestionType type, String gameData) {
        // TODO: Intégrer avec Mistral AI
        String aiText = "AI-generated recommendation based on game analysis";
        
        Recommendation recommendation = new Recommendation(user, type, aiText, true);
        return createRecommendation(recommendation);
    }
}