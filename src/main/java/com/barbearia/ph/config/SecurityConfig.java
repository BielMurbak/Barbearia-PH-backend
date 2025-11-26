package com.barbearia.ph.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                // Rotas públicas
                .requestMatchers("/api/auth/**").permitAll()
                
                // Rotas que exigem ROLE_ADMIN (apenas profissionais)
                .requestMatchers("/api/profissionais/**").hasRole("ADMIN")
                .requestMatchers("/api/servicos/**").hasRole("ADMIN")
                .requestMatchers("/api/profissionais/servicos/**").hasRole("ADMIN")
                
                // Rotas que exigem ROLE_ADMIN para operações de escrita
                .requestMatchers("POST", "/api/clientes/**").hasRole("ADMIN")
                .requestMatchers("PUT", "/api/clientes/**").hasRole("ADMIN")
                .requestMatchers("DELETE", "/api/clientes/**").hasRole("ADMIN")
                
                // Rotas de agendamento - clientes podem ver seus próprios, admins podem ver todos
                .requestMatchers("/api/agendamentos/**").authenticated()
                
                // Rotas de leitura de clientes - qualquer usuário autenticado
                .requestMatchers("GET", "/api/clientes/**").authenticated()
                
                // Qualquer outra rota precisa de autenticação
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}