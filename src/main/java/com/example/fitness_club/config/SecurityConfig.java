// src/main/java/com/example/fitness_club/config/SecurityConfig.java
package com.example.fitness_club.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.example.fitness_club.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean UserDetailsService userDetailsService() {
        return username ->
                userRepository.findByEmail(username)
                        .map(user -> org.springframework.security.core.userdetails.User
                                .withUsername(user.getEmail())
                                .password(user.getPassword())
                                // spring-roles: автоматически добавится "ROLE_"
                                .roles(user.getRole().name())
                                .build()
                        )
                        .orElseThrow(() ->
                                new UsernameNotFoundException("User with email " + username + " not found")
                        );
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Swagger / OpenAPI
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html","/swagger-ui/**","/webjars/**"
                        ).permitAll()

                        // Регистрация и логин
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        // Только ADMIN для /api/admin/**
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Всё остальное из /api/** доступно любому аутентифицированному (USER или ADMIN)
                        .requestMatchers("/api/**").authenticated()

                        // и (если есть какие-то статика или другие URL) — либо permitAll(), либо authenticated()/hasRole
                        .anyRequest().permitAll()
                )
                .httpBasic(withDefaults())
                .authenticationProvider(authenticationProvider());

        return http.build();
    }
}
