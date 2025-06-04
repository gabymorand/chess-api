package com.chesslearning.chess_api.service;

import com.chesslearning.chess_api.entity.Ranking;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.repository.RankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RankingService {
    
    @Autowired
    private RankingRepository rankingRepository;
    
    public Ranking createRanking(Ranking ranking) {
        return rankingRepository.save(ranking);
    }
    
    public Optional<Ranking> getRankingById(Long id) {
        return rankingRepository.findById(id);
    }
    
    public Page<Ranking> getAllRankings(Pageable pageable) {
        return rankingRepository.findAll(pageable);
    }
    
    public Ranking updateRanking(Long id, Ranking rankingDetails) {
        Ranking ranking = rankingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ranking not found with id: " + id));
        
        ranking.setEloRating(rankingDetails.getEloRating());
        ranking.setGamesPlayed(rankingDetails.getGamesPlayed());
        ranking.setGamesWon(rankingDetails.getGamesWon());
        ranking.setGamesLost(rankingDetails.getGamesLost());
        ranking.setGamesDrawn(rankingDetails.getGamesDrawn());
        ranking.calculateWinRate();
        
        return rankingRepository.save(ranking);
    }
    
    public void deleteRanking(Long id) {
        Ranking ranking = rankingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ranking not found with id: " + id));
        rankingRepository.delete(ranking);
    }
    
    public Optional<Ranking> getRankingByUser(User user) {
        return rankingRepository.findByUser(user);
    }
    
    public Page<Ranking> getRankingsByEloRating(Pageable pageable) {
        return rankingRepository.findAllOrderByEloRatingDesc(pageable);
    }
    
    public Page<Ranking> getRankingsByWinRate(Pageable pageable) {
        return rankingRepository.findAllOrderByWinRateDesc(pageable);
    }
    
    public Page<Ranking> getRankingsByEloRange(Integer minRating, Integer maxRating, Pageable pageable) {
        return rankingRepository.findByEloRatingBetween(minRating, maxRating, pageable);
    }
    
    public Page<Ranking> getActiveRankings(Integer minGames, Pageable pageable) {
        return rankingRepository.findByGamesPlayedGreaterThanEqual(minGames, pageable);
    }
    
    public List<Ranking> getTop10Players() {
        return rankingRepository.findTop10ByOrderByEloRatingDesc(PageRequest.of(0, 10));
    }
    
    public Long getPlayerPosition(Integer rating) {
        return rankingRepository.countByEloRatingGreaterThan(rating) + 1;
    }
    
    public void updatePlayerStats(User user, boolean won, boolean lost, boolean draw) {
        Optional<Ranking> rankingOpt = getRankingByUser(user);
        Ranking ranking;
        
        if (rankingOpt.isPresent()) {
            ranking = rankingOpt.get();
        } else {
            ranking = new Ranking(user, 1200);
        }
        
        ranking.setGamesPlayed(ranking.getGamesPlayed() + 1);
        if (won) ranking.setGamesWon(ranking.getGamesWon() + 1);
        if (lost) ranking.setGamesLost(ranking.getGamesLost() + 1);
        if (draw) ranking.setGamesDrawn(ranking.getGamesDrawn() + 1);
        
        ranking.calculateWinRate();
        rankingRepository.save(ranking);
    }
}