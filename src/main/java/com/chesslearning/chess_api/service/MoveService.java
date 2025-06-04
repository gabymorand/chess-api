package com.chesslearning.chess_api.service;

import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.Move;
import com.chesslearning.chess_api.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MoveService {
    
    @Autowired
    private MoveRepository moveRepository;
    
    public Move createMove(Move move) {
        return moveRepository.save(move);
    }
    
    public Optional<Move> getMoveById(Long id) {
        return moveRepository.findById(id);
    }
    
    public Page<Move> getAllMoves(Pageable pageable) {
        return moveRepository.findAll(pageable);
    }
    
    public Move updateMove(Long id, Move moveDetails) {
        Move move = moveRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Move not found with id: " + id));
        
        move.setMoveNumber(moveDetails.getMoveNumber());
        move.setMoveNotation(moveDetails.getMoveNotation());
        move.setFromSquare(moveDetails.getFromSquare());
        move.setToSquare(moveDetails.getToSquare());
        move.setPieceMoved(moveDetails.getPieceMoved());
        move.setPieceCaptured(moveDetails.getPieceCaptured());
        move.setIsCheck(moveDetails.getIsCheck());
        move.setIsCheckmate(moveDetails.getIsCheckmate());
        move.setIsCastling(moveDetails.getIsCastling());
        move.setMoveTime(moveDetails.getMoveTime());
        
        return moveRepository.save(move);
    }
    
    public void deleteMove(Long id) {
        Move move = moveRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Move not found with id: " + id));
        moveRepository.delete(move);
    }
    
    public Page<Move> getMovesByGame(Game game, Pageable pageable) {
        return moveRepository.findByGame(game, pageable);
    }
    
    public List<Move> getMovesByGameOrdered(Game game) {
        return moveRepository.findByGameOrderByMoveNumberAsc(game);
    }
    
    public Move getMoveByGameAndNumber(Game game, Integer moveNumber) {
        return moveRepository.findByGameAndMoveNumber(game, moveNumber);
    }
    
    public Page<Move> getMovesByPiece(String piece, Pageable pageable) {
        return moveRepository.findByPieceMoved(piece, pageable);
    }
    
    public Page<Move> getCheckMoves(Pageable pageable) {
        return moveRepository.findCheckMoves(pageable);
    }
    
    public Page<Move> getCheckmateMoves(Pageable pageable) {
        return moveRepository.findCheckmateMoves(pageable);
    }
    
    public Page<Move> getCastlingMoves(Pageable pageable) {
        return moveRepository.findCastlingMoves(pageable);
    }
}