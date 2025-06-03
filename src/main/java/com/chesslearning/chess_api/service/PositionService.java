package com.chesslearning.chess_api.service;

import com.chesslearning.chess_api.entity.Position;
import com.chesslearning.chess_api.entity.Lesson;
import com.chesslearning.chess_api.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PositionService {
    
    @Autowired
    private PositionRepository positionRepository;
    
    // CRUD operations
    public Position createPosition(Position position) {
        return positionRepository.save(position);
    }
    
    public Optional<Position> getPositionById(Long id) {
        return positionRepository.findById(id);
    }
    
    public Page<Position> getAllPositions(Pageable pageable) {
        return positionRepository.findAll(pageable);
    }
    
    public Position updatePosition(Long id, Position positionDetails) {
        Position position = positionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Position not found with id: " + id));
        
        position.setFen(positionDetails.getFen());
        position.setDescription(positionDetails.getDescription());
        position.setLesson(positionDetails.getLesson());
        
        return positionRepository.save(position);
    }
    
    public void deletePosition(Long id) {
        if (!positionRepository.existsById(id)) {
            throw new RuntimeException("Position not found with id: " + id);
        }
        positionRepository.deleteById(id);
    }
    
    // Méthodes spécifiques
    public List<Position> getPositionsByLesson(Lesson lesson) {
        return positionRepository.findByLesson(lesson);
    }
    
    public Page<Position> getPositionsByLesson(Lesson lesson, Pageable pageable) {
        return positionRepository.findByLesson(lesson, pageable);
    }
    
    public List<Position> getFreePositions() {
        return positionRepository.findByLessonIsNull();
    }
    
    public Page<Position> getFreePositions(Pageable pageable) {
        return positionRepository.findByLessonIsNull(pageable);
    }
    
    public Optional<Position> getPositionByFen(String fen) {
        return positionRepository.findByFen(fen);
    }
    
    public Page<Position> searchPositionsByDescription(String keyword, Pageable pageable) {
        return positionRepository.findByDescriptionContaining(keyword, pageable);
    }
    
    public boolean existsByFen(String fen) {
        return positionRepository.existsByFen(fen);
    }
    
    // Statistiques
    public long countPositionsByLesson(Lesson lesson) {
        return positionRepository.countByLesson(lesson);
    }
    
    public long countFreePositions() {
        return positionRepository.countByLessonIsNull();
    }
    
    // Validation
    public void validatePosition(Position position) {
        if (existsByFen(position.getFen())) {
            throw new RuntimeException("Position with this FEN already exists: " + position.getFen());
        }
    }
}