package com.barbearia.ph.controller;

import com.barbearia.ph.service.CustomUserDetailsService;
import com.barbearia.ph.service.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("=== LOGIN DEBUG ===");
            System.out.println("Celular recebido: '" + request.getCelular() + "'");
            System.out.println("Senha recebida: '" + request.getSenha() + "'");

            // Busca usuário (Cliente ou Profissional/Admin)
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getCelular());

            // Verifica senha
            if (passwordEncoder.matches(request.getSenha(), userDetails.getPassword())) {
                String token = jwtUtil.generateToken(userDetails.getUsername());
                return ResponseEntity.ok(new LoginResponse(token));
            } else {
                return ResponseEntity.badRequest().body("Senha incorreta");
            }

        } catch (Exception ex) {
            System.out.println("Erro no login: " + ex.getMessage());
            return ResponseEntity.badRequest().body("Usuário não encontrado: " + ex.getMessage());
        }
    }

    @PostMapping("/validar")
    public ResponseEntity<?> validarToken(@RequestBody ValidarRequest request) {
        boolean valido = jwtUtil.validateToken(request.getToken());
        if (valido) {
            return ResponseEntity.ok("Token válido");
        } else {
            return ResponseEntity.badRequest().body("Token inválido ou expirado");
        }
    }

    // ===================== DTOs =====================
    public static class LoginRequest {
        private String celular;
        private String senha;

        public String getCelular() { return celular; }
        public void setCelular(String celular) { this.celular = celular; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    public static class LoginResponse {
        private String token;

        public LoginResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }

    public static class ValidarRequest {
        private String token;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}