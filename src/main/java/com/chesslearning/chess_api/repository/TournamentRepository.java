package com.chesslearning.chess_api.repository;

import com.chesslearning.chess_api.entity.Tournament;
import com.chesslearning.chess_api.entity.TournamentStatus;
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
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    
    Page<Tournament> findByStatus(TournamentStatus status, Pageable pageable);
    
    Page<Tournament> findByOrganizer(User organizer, Pageable pageable);
    
    @Query("SELECT t FROM Tournament t WHERE t.startDate >= :startDate AND t.endDate <= :endDate")
    Page<Tournament> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate, 
                                   Pageable pageable);
    
    List<Tournament> findByStatusAndStartDateBefore(TournamentStatus status, LocalDateTime date);
    
    @Query("SELECT t FROM Tournament t WHERE t.name LIKE %:name%")
    Page<Tournament> findByNameContaining(@Param("name") String name, Pageable pageable);
}