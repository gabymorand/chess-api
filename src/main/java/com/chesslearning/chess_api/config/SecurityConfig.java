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
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                
                // CORS ACTIVÉ
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                
                .authorizeHttpRequests(auth -> auth
                        // ===== ENDPOINTS COMPLÈTEMENT PUBLICS =====
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/", "/health", "/readme", "/api/info").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        
                        // ===== TOUS LES GET PUBLICS (pour Postman) =====
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        
                        // ===== POST/PUT/DELETE pour utilisateurs authentifiés =====
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()  // Register
                        .requestMatchers(HttpMethod.POST, "/api/comments").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/games").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/moves").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/tournaments").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/ai/**").hasAnyRole("USER", "ADMIN")
                        
                        .requestMatchers(HttpMethod.PUT, "/api/comments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/games/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/moves/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/tournaments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("USER", "ADMIN")
                        
                        // ===== DELETE ADMIN SEULEMENT =====
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/tournaments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/games/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/moves/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/rankings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").hasAnyRole("USER", "ADMIN")
                        
                        // ===== ADMIN ENDPOINTS =====
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