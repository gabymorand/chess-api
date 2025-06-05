package com.chesslearning.chess_api.controller;

import com.chesslearning.chess_api.dto.UserCreateDTO;
import com.chesslearning.chess_api.dto.UserResponseDTO;
import com.chesslearning.chess_api.entity.User;
import com.chesslearning.chess_api.entity.Role;
import com.chesslearning.chess_api.mapper.UserMapper;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;    
    
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "User ID") @PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(userMapper.toResponseDTO(value), HttpStatus.OK))
                  .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Get all users", description = "Retrieves all users with pagination")
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userService.getAllUsers(pageable);
        Page<UserResponseDTO> userDTOs = users.map(userMapper::toResponseDTO);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Update user", description = "Updates an existing user")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Valid @RequestBody UserCreateDTO userUpdateDTO) {
        try {
            User userDetails = userMapper.toEntity(userUpdateDTO);
            User updatedUser = userService.updateUser(id, userDetails);
            UserResponseDTO response = userMapper.toResponseDTO(updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Delete user", description = "Deletes a user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Get user by username", description = "Retrieves a user by username")
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(
            @Parameter(description = "Username") @PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(value -> new ResponseEntity<>(userMapper.toResponseDTO(value), HttpStatus.OK))
                  .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @Operation(summary = "Get users by role", description = "Retrieves users by their role")
    @GetMapping("/role/{role}")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByRole(
            @Parameter(description = "User role") @PathVariable Role role,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getUsersByRole(role, pageable);
        Page<UserResponseDTO> userDTOs = users.map(userMapper::toResponseDTO);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Search users", description = "Search users by keyword")
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.searchUsers(keyword, pageable);
        Page<UserResponseDTO> userDTOs = users.map(userMapper::toResponseDTO);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }
    
    @Operation(summary = "Get user statistics", description = "Get count of users by role")
    @GetMapping("/stats/role/{role}")
    public ResponseEntity<Long> getUserCountByRole(
            @Parameter(description = "User role") @PathVariable Role role) {
        long count = userService.countUsersByRole(role);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}