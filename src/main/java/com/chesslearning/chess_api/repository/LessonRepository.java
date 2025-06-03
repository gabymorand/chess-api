package com.chesslearning.chess_api.repository;

import com.chesslearning.chess_api.entity.Lesson;
import com.chesslearning.chess_api.entity.LessonLevel;
import com.chesslearning.chess_api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    
    // Leçons par niveau
    Page<Lesson> findByLevel(LessonLevel level, Pageable pageable);
    
    // Leçons par coach
    Page<Lesson> findByCoach(User coach, Pageable pageable);
    
    // Leçons par coach et niveau
    Page<Lesson> findByCoachAndLevel(User coach, LessonLevel level, Pageable pageable);
    
    // Recherche par titre
    @Query("SELECT l FROM Lesson l WHERE l.title LIKE %:keyword%")
    Page<Lesson> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);
    
    // Recherche par contenu
    @Query("SELECT l FROM Lesson l WHERE l.title LIKE %:keyword% OR l.content LIKE %:keyword%")
    Page<Lesson> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Leçons avec vidéo
    @Query("SELECT l FROM Lesson l WHERE l.videoUrl IS NOT NULL")
    Page<Lesson> findLessonsWithVideo(Pageable pageable);
    
    // Compter par niveau
    long countByLevel(LessonLevel level);
    
    // Compter par coach
    long countByCoach(User coach);
}