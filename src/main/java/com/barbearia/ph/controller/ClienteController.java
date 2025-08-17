package com.barbearia.ph.controller;


import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor

public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/save")
    public ResponseEntity<ClienteEntity> save(@Valid @RequestBody ClienteEntity clienteEntity) {
        return ResponseEntity.ok(clienteService.save(clienteEntity));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ClienteEntity>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteEntity> findById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteEntity> update(@PathVariable Long id, @Valid @RequestBody ClienteEntity clienteEntity) {
        return ResponseEntity.ok(clienteService.update(id, clienteEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}