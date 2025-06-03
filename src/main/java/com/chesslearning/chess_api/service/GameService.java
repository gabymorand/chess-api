package com.chesslearning.chess_api.service;

import com.chesslearning.chess_api.entity.Game;
import com.chesslearning.chess_api.entity.GameResult;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.repository.GameRepository;
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
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;
    
    // CRUD operations
    public Game createGame(Game game) {
        return gameRepository.save(game);
    }
    
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }
    
    public Page<Game> getAllGames(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }
    
    public Game updateGame(Long id, Game gameDetails) {
        Game game = gameRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
        
        game.setResult(gameDetails.getResult());
        game.setPgnData(gameDetails.getPgnData());
        game.setGameDate(gameDetails.getGameDate());
        
        return gameRepository.save(game);
    }
    
    public void deleteGame(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new RuntimeException("Game not found with id: " + id);
        }
        gameRepository.deleteById(id);
    }
    
    // Méthodes spécifiques
    public Page<Game> getGamesByPlayer(User player, Pageable pageable) {
        return gameRepository.findByPlayer(player, pageable);
    }
    
    public Page<Game> getGamesByResult(GameResult result, Pageable pageable) {
        return gameRepository.findByResult(result, pageable);
    }
    
    public List<Game> getGamesBetweenPlayers(User player1, User player2) {
        return gameRepository.findGamesBetweenPlayers(player1, player2);
    }
    
    public Page<Game> getGamesByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return gameRepository.findByGameDateBetween(start, end, pageable);
    }
    
    // Statistiques
    public long getWinsByPlayer(User player) {
        return gameRepository.countWinsByPlayer(player);
    }
    
    public long getTotalGamesByPlayer(User player) {
        return gameRepository.countTotalGamesByPlayer(player);
    }
    
    public double getWinRate(User player) {
        long totalGames = getTotalGamesByPlayer(player);
        if (totalGames == 0) return 0.0;
        
        long wins = getWinsByPlayer(player);
        return (double) wins / totalGames * 100;
    }
}