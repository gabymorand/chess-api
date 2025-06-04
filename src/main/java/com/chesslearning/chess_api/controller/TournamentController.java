package com.chesslearning.chess_api.controller;

import com.chesslearning.chess_api.dto.TournamentCreateDTO;
import com.chesslearning.chess_api.dto.TournamentResponseDTO;
import com.chesslearning.chess_api.entity.Tournament;
import com.chesslearning.chess_api.entity.TournamentStatus;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.mapper.TournamentMapper;
import com.chesslearning.chess_api.service.TournamentService;
import com.chesslearning.chess_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tournaments")
@Tag(name = "Tournament Management", description = "APIs for managing chess tournaments")
public class TournamentController {
    
    @Autowired
    private TournamentService tournamentService;
    
    @Autowired
    private TournamentMapper tournamentMapper;
    
    @Autowired
    private UserService userService;
    
    @Operation(summary = "Create a new tournament", description = "Creates a new chess tournament")
    @PostMapping
    public ResponseEntity<TournamentResponseDTO> createTournament(@Valid @RequestBody TournamentCreateDTO tournamentCreateDTO) {
        try {
            Tournament tournament = tournamentMapper.toEntity(tournamentCreateDTO);
            Tournament createdTournament = tournamentService.createTournament(tournament);
            TournamentResponseDTO response = tournamentMapper.toResponseDTO(createdTournament);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = "Get tournament by ID", description = "Retrieves a tournament by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponseDTO> getTournamentById(
            @Parameter(description = "Tournament ID") @PathVariable Long id) {
        Optional<Tournament> tournament = tournamentService.getTournamentById(id);
        return tournament.map(value -> new ResponseEntity<>(tournamentMapper.toResponseDTO(value), HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Get all tournaments", description = "Retrieves all tournaments with pagination")
    @GetMapping
    public ResponseEntity<Page<TournamentResponseDTO>> getAllTournaments(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "startDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Tournament> tournaments = tournamentService.getAllTournaments(pageable);
        Page<TournamentResponseDTO> tournamentDTOs = tournaments.map(tournamentMapper::toResponseDTO);
        return new ResponseEntity<>(tournamentDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Update tournament", description = "Updates an existing tournament")
    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponseDTO> updateTournament(
            @Parameter(description = "Tournament ID") @PathVariable Long id,
            @Valid @RequestBody TournamentCreateDTO tournamentUpdateDTO) {
        try {
            Tournament tournamentDetails = tournamentMapper.toEntity(tournamentUpdateDTO);
            Tournament updatedTournament = tournamentService.updateTournament(id, tournamentDetails);
            TournamentResponseDTO response = tournamentMapper.toResponseDTO(updatedTournament);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Delete tournament", description = "Deletes a tournament by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(
            @Parameter(description = "Tournament ID") @PathVariable Long id) {
        try {
            tournamentService.deleteTournament(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Get tournaments by status", description = "Retrieves tournaments by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TournamentResponseDTO>> getTournamentsByStatus(
            @Parameter(description = "Tournament status") @PathVariable TournamentStatus status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Tournament> tournaments = tournamentService.getTournamentsByStatus(status, pageable);
        Page<TournamentResponseDTO> tournamentDTOs = tournaments.map(tournamentMapper::toResponseDTO);
        return new ResponseEntity<>(tournamentDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Get tournaments by organizer", description = "Retrieves tournaments organized by a specific user")
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<Page<TournamentResponseDTO>> getTournamentsByOrganizer(
            @Parameter(description = "Organizer ID") @PathVariable Long organizerId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        try {
            User organizer = userService.getUserById(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));
            
            Pageable pageable = PageRequest.of(page, size);
            Page<Tournament> tournaments = tournamentService.getTournamentsByOrganizer(organizer, pageable);
            Page<TournamentResponseDTO> tournamentDTOs = tournaments.map(tournamentMapper::toResponseDTO);
            return new ResponseEntity<>(tournamentDTOs, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Search tournaments by name", description = "Search tournaments by name")
    @GetMapping("/search")
    public ResponseEntity<Page<TournamentResponseDTO>> searchTournaments(
            @Parameter(description = "Tournament name") @RequestParam String name,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Tournament> tournaments = tournamentService.getTournamentsByName(name, pageable);
        Page<TournamentResponseDTO> tournamentDTOs = tournaments.map(tournamentMapper::toResponseDTO);
        return new ResponseEntity<>(tournamentDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Get upcoming tournaments", description = "Get tournaments starting soon")
    @GetMapping("/upcoming")
    public ResponseEntity<List<TournamentResponseDTO>> getUpcomingTournaments() {
        List<Tournament> tournaments = tournamentService.getUpcomingTournaments();
        List<TournamentResponseDTO> tournamentDTOs = tournaments.stream()
            .map(tournamentMapper::toResponseDTO)
            .toList();
        return new ResponseEntity<>(tournamentDTOs, HttpStatus.OK);
    }
}