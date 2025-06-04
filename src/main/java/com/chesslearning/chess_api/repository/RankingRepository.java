package com.chesslearning.chess_api.repository;

import com.chesslearning.chess_api.entity.Ranking;
import com.chesslearning.chess_api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    
    Optional<Ranking> findByUser(User user);
    
    @Query("SELECT r FROM Ranking r ORDER BY r.eloRating DESC")
    Page<Ranking> findAllOrderByEloRatingDesc(Pageable pageable);
    
    @Query("SELECT r FROM Ranking r ORDER BY r.winRate DESC")
    Page<Ranking> findAllOrderByWinRateDesc(Pageable pageable);
    
    @Query("SELECT r FROM Ranking r WHERE r.eloRating >= :minRating AND r.eloRating <= :maxRating")
    Page<Ranking> findByEloRatingBetween(@Param("minRating") Integer minRating, 
                                       @Param("maxRating") Integer maxRating, 
                                       Pageable pageable);
    
    @Query("SELECT r FROM Ranking r WHERE r.gamesPlayed >= :minGames")
    Page<Ranking> findByGamesPlayedGreaterThanEqual(@Param("minGames") Integer minGames, Pageable pageable);
    
    @Query("SELECT r FROM Ranking r ORDER BY r.eloRating DESC")
    List<Ranking> findTop10ByOrderByEloRatingDesc(Pageable pageable);
    
    @Query("SELECT COUNT(r) FROM Ranking r WHERE r.eloRating > :rating")
    Long countByEloRatingGreaterThan(@Param("rating") Integer rating);
}