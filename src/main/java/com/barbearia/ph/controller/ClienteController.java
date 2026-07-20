package com.barbearia.ph.controller;

import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody ClienteEntity clienteEntity) {
        try {
            ClienteEntity salvo = clienteService.save(clienteEntity);
            return ResponseEntity.ok(salvo);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao salvar cliente: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<ClienteEntity> clientes = clienteService.findAll();
            return ResponseEntity.ok(clientes);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao listar clientes: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            ClienteEntity cliente = clienteService.findById(id);
            return ResponseEntity.ok(cliente);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar cliente: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ClienteEntity clienteEntity) {
        try {
            ClienteEntity atualizado = clienteService.update(id, clienteEntity);
            return ResponseEntity.ok(atualizado);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar cliente: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<?> alterarSenha(
            @PathVariable Long id,
            @RequestBody AlterarSenhaRequest request,
            @AuthenticationPrincipal UserDetails user) {
        try {
            boolean isAdmin = user != null && user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin) {
                if (user == null) {
                    return ResponseEntity.status(401).body("Autenticação necessária.");
                }
                ClienteEntity dono = clienteService.findById(id);
                if (!dono.getCelular().equals(user.getUsername())) {
                    return ResponseEntity.status(403).body("Você só pode alterar sua própria senha.");
                }
            }

            clienteService.alterarSenha(id, request.getSenhaAtual(), request.getNovaSenha());
            return ResponseEntity.ok("Senha atualizada com sucesso.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            clienteService.delete(id);
            return ResponseEntity.ok("Cliente com ID " + id + " removido com sucesso.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao deletar cliente: " + ex.getMessage());
        }
    }

    @GetMapping("/nome")
    public ResponseEntity<?> findByNome(@RequestParam String nome) {
        try {
            List<ClienteEntity> clientes = clienteService.findByNome(nome);
            return ResponseEntity.ok(clientes);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar por nome: " + ex.getMessage());
        }
    }

    @GetMapping("/celular")
    public ResponseEntity<?> findByCelular(@RequestParam String celular) {
        try {
            List<ClienteEntity> clientes = clienteService.findByCelular(celular);
            return ResponseEntity.ok(clientes);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar por celular: " + ex.getMessage());
        }
    }

    @GetMapping("/nome-completo")
    public ResponseEntity<?> findByNomeCompleto(@RequestParam String nomeCompleto) {
        try {
            List<ClienteEntity> clientes = clienteService.findByNomeCompleto(nomeCompleto);
            return ResponseEntity.ok(clientes);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar por nome completo: " + ex.getMessage());
        }
    }

    public static class AlterarSenhaRequest {
        private String senhaAtual;
        private String novaSenha;

        public String getSenhaAtual() { return senhaAtual; }
        public void setSenhaAtual(String senhaAtual) { this.senhaAtual = senhaAtual; }
        public String getNovaSenha() { return novaSenha; }
        public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }
    }
}