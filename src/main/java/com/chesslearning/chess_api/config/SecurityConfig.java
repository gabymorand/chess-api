package com.chesslearning.chess_api.config;

import com.chesslearning.chess_api.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("GET", "/api/users").permitAll()
                        .requestMatchers("GET", "/api/users/{id}").permitAll()
                        .requestMatchers("GET", "/api/users/username/{username}").permitAll()
                        .requestMatchers("GET", "/api/tournaments").permitAll()
                        .requestMatchers("GET", "/api/tournaments/{id}").permitAll()
                        .requestMatchers("GET", "/api/tournaments/upcoming").permitAll()
                        .requestMatchers("GET", "/api/games").permitAll()
                        .requestMatchers("GET", "/api/games/{id}").permitAll()
                        .requestMatchers("GET", "/api/games/result/{result}").permitAll()
                        .requestMatchers("GET", "/api/rankings").permitAll()
                        .requestMatchers("GET", "/api/rankings/{id}").permitAll()
                        .requestMatchers("GET", "/api/rankings/leaderboard").permitAll()
                        .requestMatchers("GET", "/api/comments/game/{gameId}").permitAll()
                        .requestMatchers("GET", "/api/comments/game/{gameId}/ordered").permitAll()
                        .requestMatchers("GET", "/api/moves").permitAll()
                        .requestMatchers("GET", "/api/moves/{id}").permitAll()
                        .requestMatchers("GET", "/api/moves/game/{gameId}").permitAll()
                        
                        // Endpoints utilisateur authentifié (USER role)
                        .requestMatchers("POST", "/api/comments").hasRole("USER")
                        .requestMatchers("PUT", "/api/comments/{id}").hasRole("USER")
                        .requestMatchers("DELETE", "/api/comments/{id}").hasRole("USER")
                        .requestMatchers("POST", "/api/games").hasRole("USER")
                        .requestMatchers("PUT", "/api/games/{id}").hasRole("USER")
                        .requestMatchers("POST", "/api/moves").hasRole("USER")
                        .requestMatchers("PUT", "/api/moves/{id}").hasRole("USER")
                        .requestMatchers("POST", "/api/tournaments").hasRole("USER")
                        .requestMatchers("PUT", "/api/tournaments/{id}").hasRole("USER")
                        
                        // Endpoints admin seulement (ADMIN role)
                        .requestMatchers("DELETE", "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/api/tournaments/{id}").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/api/games/{id}").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/api/moves/{id}").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/api/rankings/{id}").hasRole("ADMIN")
                        .requestMatchers("PUT", "/api/users/{id}/role").hasRole("ADMIN")
                        .requestMatchers("POST", "/api/rankings/update-stats").hasRole("ADMIN")
                        

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