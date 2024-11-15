package com.tugasakhir.ideapedia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
    @Bean(name = "securityFilterChain1")
    public SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())  // Menambahkan konfigurasi CORS dengan default
                .csrf(csrf -> csrf.disable())    // Menonaktifkan CSRF
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/auth/**", "/idea/**", "/user/**", "/detail/**", "/unit/**").permitAll() // Membuka akses ke endpoint /auth/**
                        .anyRequest().authenticated() // Menyaring semua request lainnya untuk memerlukan autentikasi
                );
        return http.build();
    }
}