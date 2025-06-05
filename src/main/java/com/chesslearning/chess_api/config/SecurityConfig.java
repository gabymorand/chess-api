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
                        // ===== RÈGLES ABSOLUMENT PRIORITAIRES =====
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/", "/health", "/readme", "/api/info").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        
                        // ===== ENDPOINTS DE LECTURE PUBLICS =====
                        .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/username/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tournaments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tournaments/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tournaments/upcoming").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/games").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/games/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/games/result/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rankings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rankings/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rankings/leaderboard").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments/game/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments/game/*/ordered").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/moves").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/moves/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/moves/game/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ai/health").permitAll()
                        
                        // ===== ENDPOINTS USER (AUTHENTIFIÉ) =====
                        .requestMatchers(HttpMethod.POST, "/api/comments").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/comments/*").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/games").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/games/*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/moves").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/moves/*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/tournaments").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/tournaments/*").hasRole("USER")
                        
                        // ===== ENDPOINTS IA (AUTHENTIFIÉ) =====
                        .requestMatchers(HttpMethod.POST, "/api/ai/chat").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/ai/analyze/game/*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/ai/suggest/move/*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/ai/explain/move").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/ai/quiz").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/ai/tips/improvement").hasRole("USER")
                        
                        // ===== ENDPOINTS ADMIN SEULEMENT =====
                        .requestMatchers(HttpMethod.DELETE, "/api/users/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/tournaments/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/games/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/moves/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/rankings/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*/role").hasRole("ADMIN")
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