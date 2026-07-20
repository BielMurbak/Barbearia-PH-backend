package com.barbearia.ph.controller;

import com.barbearia.ph.dto.ClienteRequestDTO;
import com.barbearia.ph.dto.ClienteResponseDTO;
import com.barbearia.ph.dto.DtoMapper;
import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.barbearia.ph.security.AuthUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> save(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteEntity salvo = clienteService.save(DtoMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toResponse(salvo));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> findAll() {
        List<ClienteResponseDTO> clientes = clienteService.findAll().stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(DtoMapper.toResponse(clienteService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        ClienteEntity atualizado = clienteService.update(id, DtoMapper.toEntity(dto));
        return ResponseEntity.ok(DtoMapper.toResponse(atualizado));
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<?> alterarSenha(
            @PathVariable Long id,
            @RequestBody AlterarSenhaRequest request,
            Authentication user) {
        try {
            boolean isAdmin = AuthUtils.isAdmin(user);

            if (!isAdmin) {
                if (user == null) {
                    return ResponseEntity.status(401).body("Autenticação necessária.");
                }
                ClienteEntity dono = clienteService.findById(id);
                if (!dono.getCelular().equals(AuthUtils.celular(user))) {
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
    public ResponseEntity<String> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.ok("Cliente com ID " + id + " removido com sucesso.");
    }

    @GetMapping("/nome")
    public ResponseEntity<List<ClienteResponseDTO>> findByNome(@RequestParam String nome) {
        List<ClienteResponseDTO> clientes = clienteService.findByNome(nome).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/celular")
    public ResponseEntity<List<ClienteResponseDTO>> findByCelular(@RequestParam String celular) {
        List<ClienteResponseDTO> clientes = clienteService.findByCelular(celular).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/nome-completo")
    public ResponseEntity<List<ClienteResponseDTO>> findByNomeCompleto(@RequestParam String nomeCompleto) {
        List<ClienteResponseDTO> clientes = clienteService.findByNomeCompleto(nomeCompleto).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(clientes);
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