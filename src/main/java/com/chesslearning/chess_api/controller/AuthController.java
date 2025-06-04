package com.chesslearning.chess_api.controller;

import com.chesslearning.chess_api.dto.AuthResponse;
import com.chesslearning.chess_api.dto.LoginRequest;
import com.chesslearning.chess_api.dto.RegisterRequest;
import com.chesslearning.chess_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and authorization")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token", description = "Get a new JWT token using refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<AuthResponse> refreshToken(
            @Parameter(description = "Refresh token") @RequestParam String refreshToken) {
        try {
            AuthResponse response = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logout user (client-side token invalidation)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<String> logout() {
        // Dans une application JWT stateless, le logout est généralement géré côté client
        // en supprimant le token du stockage local/session storage
        return ResponseEntity.ok("Logout successful. Please remove the token from client storage.");
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate JWT token", description = "Check if the current JWT token is valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    })
    public ResponseEntity<String> validateToken() {
        // Si on arrive ici, c'est que le token est valide (grâce au filtre JWT)
        return ResponseEntity.ok("Token is valid");
    }
}