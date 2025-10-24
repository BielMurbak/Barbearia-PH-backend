package com.barbearia.ph.controller;

import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteEntity> save(@Valid @RequestBody ClienteEntity clienteEntity) {
        ClienteEntity salvo = clienteService.save(clienteEntity);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public ResponseEntity<List<ClienteEntity>> findAll() {
        List<ClienteEntity> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteEntity> findById(@PathVariable Long id) {
        ClienteEntity cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteEntity> update(@PathVariable Long id, @Valid @RequestBody ClienteEntity clienteEntity) {
        ClienteEntity atualizado = clienteService.update(id, clienteEntity);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.ok("Cliente com ID " + id + " removido com sucesso.");
    }

    @GetMapping("/nome")
    public ResponseEntity<List<ClienteEntity>> findByNome(@RequestParam String nome) {
        List<ClienteEntity> clientes = clienteService.findByNome(nome);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/celular")
    public ResponseEntity<List<ClienteEntity>> findByCelular(@RequestParam String celular) {
        List<ClienteEntity> clientes = clienteService.findByCelular(celular);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/nome-completo")
    public ResponseEntity<List<ClienteEntity>> findByNomeCompleto(@RequestParam String nomeCompleto) {
        List<ClienteEntity> clientes = clienteService.findByNomeCompleto(nomeCompleto);
        return ResponseEntity.ok(clientes);
    }
}