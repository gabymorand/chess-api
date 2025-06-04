package com.chesslearning.chess_api.service;

import com.chesslearning.chess_api.entity.Tournament;
import com.chesslearning.chess_api.entity.TournamentStatus;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {
    
    @Autowired
    private TournamentRepository tournamentRepository;
    
    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }
    
    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentRepository.findById(id);
    }
    
    public Page<Tournament> getAllTournaments(Pageable pageable) {
        return tournamentRepository.findAll(pageable);
    }
    
    public Tournament updateTournament(Long id, Tournament tournamentDetails) {
        Tournament tournament = tournamentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + id));
        
        tournament.setName(tournamentDetails.getName());
        tournament.setDescription(tournamentDetails.getDescription());
        tournament.setStartDate(tournamentDetails.getStartDate());
        tournament.setEndDate(tournamentDetails.getEndDate());
        tournament.setMaxParticipants(tournamentDetails.getMaxParticipants());
        tournament.setStatus(tournamentDetails.getStatus());
        tournament.setOrganizer(tournamentDetails.getOrganizer());
        
        return tournamentRepository.save(tournament);
    }
    
    public void deleteTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + id));
        tournamentRepository.delete(tournament);
    }
    
    public Page<Tournament> getTournamentsByStatus(TournamentStatus status, Pageable pageable) {
        return tournamentRepository.findByStatus(status, pageable);
    }
    
    public Page<Tournament> getTournamentsByOrganizer(User organizer, Pageable pageable) {
        return tournamentRepository.findByOrganizer(organizer, pageable);
    }
    
    public Page<Tournament> getTournamentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return tournamentRepository.findByDateRange(startDate, endDate, pageable);
    }
    
    public Page<Tournament> getTournamentsByName(String name, Pageable pageable) {
        return tournamentRepository.findByNameContaining(name, pageable);
    }
    
    public List<Tournament> getUpcomingTournaments() {
        return tournamentRepository.findByStatusAndStartDateBefore(TournamentStatus.UPCOMING, LocalDateTime.now());
    }
}