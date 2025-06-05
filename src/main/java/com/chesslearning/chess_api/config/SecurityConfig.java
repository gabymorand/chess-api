package com.chesslearning.chess_api.config;

import com.chesslearning.chess_api.security.JwtAuthenticationFilter;
import com.chesslearning.chess_api.security.CustomUserDetailsService;  // ✅ AJOUTE CETTE LIGNE
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
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ===== ENDPOINTS COMPLÈTEMENT PUBLICS =====
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/ai/**").permitAll()  // ✅ AI COMPLÈTEMENT PUBLIC
                .requestMatchers("/", "/health", "/readme", "/api/info").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                
                // ===== ENDPOINTS PUBLICS EN LECTURE =====
                .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/games").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/games/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/moves").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/moves/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/comments").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tournaments").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tournaments/**").permitAll()
                
                // ===== ENDPOINTS PROTÉGÉS EN ÉCRITURE =====
                .requestMatchers(HttpMethod.POST, "/api/games").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/moves").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/comments").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/tournaments").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                
                // ===== ENDPOINTS PROTÉGÉS EN MODIFICATION/SUPPRESSION =====
                .requestMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("USER", "ADMIN")
                
                // ===== TOUT LE RESTE NÉCESSITE UNE AUTHENTIFICATION =====
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}