package com.barbearia.ph.controller;

import com.barbearia.ph.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.token-url}")
    private String keycloakTokenUrl;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "password");
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("username", request.getCelular());
            body.add("password", request.getSenha());

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(keycloakTokenUrl, entity, Map.class);
            Map<?, ?> tokenData = response.getBody();

            if (tokenData == null) {
                return ResponseEntity.status(500).body("Resposta vazia do Keycloak");
            }

            return ResponseEntity.ok(Map.of(
                    "access_token", tokenData.get("access_token"),
                    "refresh_token", tokenData.get("refresh_token"),
                    "expires_in", tokenData.get("expires_in")
            ));

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
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