package com.chesslearning.chess_api.repository;

import com.chesslearning.chess_api.entity.Position;
import com.chesslearning.chess_api.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    
    // Positions d'une leçon
    List<Position> findByLesson(Lesson lesson);
    
    Page<Position> findByLesson(Lesson lesson, Pageable pageable);
    
    // Positions sans leçon (positions libres)
    List<Position> findByLessonIsNull();
    
    Page<Position> findByLessonIsNull(Pageable pageable);
    
    // Recherche par FEN exact
    Optional<Position> findByFen(String fen);
    
    // Recherche par description
    @Query("SELECT p FROM Position p WHERE p.description LIKE %:keyword%")
    Page<Position> findByDescriptionContaining(@Param("keyword") String keyword, Pageable pageable);
    
    // Vérifier si une position FEN existe
    boolean existsByFen(String fen);
    
    // Compter positions par leçon
    long countByLesson(Lesson lesson);
    
    // Compter positions libres
    long countByLessonIsNull();
}