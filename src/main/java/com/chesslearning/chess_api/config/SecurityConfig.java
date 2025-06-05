package com.chesslearning.chess_api.config;

import com.chesslearning.chess_api.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // ===== ENDPOINTS PUBLICS =====
                        .requestMatchers("/", "/health", "/readme", "/api/info").permitAll()
                        
                        // ===== AUTHENTICATION =====
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        
                        // ===== DOCUMENTATION PUBLIQUE =====
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        
                        // ===== IA HEALTH CHECK PUBLIC =====
                        .requestMatchers(HttpMethod.GET, "/api/ai/health").permitAll()
                        
                        // ===== ENDPOINTS DE LECTURE PUBLICS =====
                        .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/username/{username}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tournaments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tournaments/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tournaments/upcoming").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/games").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/games/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/games/result/{result}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rankings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rankings/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rankings/leaderboard").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments/game/{gameId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments/game/{gameId}/ordered").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/moves").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/moves/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/moves/game/{gameId}").permitAll()
                        
                        // ===== ENDPOINTS USER (AUTHENTIFIÉ) =====
                        .requestMatchers(HttpMethod.POST, "/api/comments").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/comments/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/games").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/games/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/moves").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/moves/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/tournaments").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/tournaments/{id}").hasRole("USER")
                        
                        // ===== ENDPOINTS IA (AUTHENTIFIÉ) =====
                        .requestMatchers(HttpMethod.POST, "/api/ai/chat").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/ai/analyze/game/{gameId}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/ai/suggest/move/{gameId}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/ai/explain/move").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/ai/quiz").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/ai/tips/improvement").hasRole("USER")
                        
                        // ===== ENDPOINTS ADMIN SEULEMENT =====
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/tournaments/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/games/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/moves/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/rankings/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}/role").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/rankings/update-stats").hasRole("ADMIN")
                        
                        // Tout le reste nécessite une authentification
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}