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
@CrossOrigin(origins = "*")
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
                String role = userDetails.getAuthorities().iterator().next().getAuthority();
                return ResponseEntity.ok(new LoginResponse(token, role));
            } else {
                return ResponseEntity.badRequest().body("Senha incorreta");
            }

        } catch (Exception ex) {
            System.out.println("Erro no login: " + ex.getMessage());
            return ResponseEntity.badRequest().body("Usuário não encontrado: " + ex.getMessage());
        }
    }

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registerCliente(@RequestBody RegisterClienteRequest request) {
        try {
            // Criptografa a senha
            String senhaCriptografada = passwordEncoder.encode(request.getSenha());
            request.setSenha(senhaCriptografada);
            
            // Aqui você chamaria o serviço para salvar o cliente
            // ClienteEntity cliente = clienteService.save(request.toEntity());
            
            return ResponseEntity.ok("Cliente registrado com sucesso");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Erro ao registrar cliente: " + ex.getMessage());
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
        private String role;

        public LoginResponse(String token, String role) { 
            this.token = token; 
            this.role = role;
        }
        public String getToken() { return token; }
        public String getRole() { return role; }
    }

    public static class RegisterClienteRequest {
        private String nome;
        private String celular;
        private String senha;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getCelular() { return celular; }
        public void setCelular(String celular) { this.celular = celular; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    public static class ValidarRequest {
        private String token;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}