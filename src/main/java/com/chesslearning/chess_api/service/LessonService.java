package com.chesslearning.chess_api.service;

import com.chesslearning.chess_api.entity.Lesson;
import com.chesslearning.chess_api.entity.LessonLevel;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LessonService {
    
    @Autowired
    private LessonRepository lessonRepository;
    
    // CRUD operations
    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    
    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }
    
    public Page<Lesson> getAllLessons(Pageable pageable) {
        return lessonRepository.findAll(pageable);
    }
    
    public Lesson updateLesson(Long id, Lesson lessonDetails) {
        Lesson lesson = lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + id));
        
        lesson.setTitle(lessonDetails.getTitle());
        lesson.setContent(lessonDetails.getContent());
        lesson.setLevel(lessonDetails.getLevel());
        lesson.setVideoUrl(lessonDetails.getVideoUrl());
        
        return lessonRepository.save(lesson);
    }
    
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
        lessonRepository.deleteById(id);
    }
    
    // Méthodes spécifiques
    public Page<Lesson> getLessonsByLevel(LessonLevel level, Pageable pageable) {
        return lessonRepository.findByLevel(level, pageable);
    }
    
    public Page<Lesson> getLessonsByCoach(User coach, Pageable pageable) {
        return lessonRepository.findByCoach(coach, pageable);
    }
    
    public Page<Lesson> getLessonsByCoachAndLevel(User coach, LessonLevel level, Pageable pageable) {
        return lessonRepository.findByCoachAndLevel(coach, level, pageable);
    }
    
    public Page<Lesson> searchLessonsByTitle(String keyword, Pageable pageable) {
        return lessonRepository.findByTitleContaining(keyword, pageable);
    }
    
    public Page<Lesson> searchLessons(String keyword, Pageable pageable) {
        return lessonRepository.findByKeyword(keyword, pageable);
    }
    
    public Page<Lesson> getLessonsWithVideo(Pageable pageable) {
        return lessonRepository.findLessonsWithVideo(pageable);
    }
    
    // Statistiques
    public long countLessonsByLevel(LessonLevel level) {
        return lessonRepository.countByLevel(level);
    }
    
    public long countLessonsByCoach(User coach) {
        return lessonRepository.countByCoach(coach);
    }
}