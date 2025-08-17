package com.barbearia.ph.controller;


import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor

public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping("/save")
    public ResponseEntity<ServicoEntity> save(@Valid @RequestBody ServicoEntity servicoEntity) {
        return ResponseEntity.ok(servicoService.save(servicoEntity));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ServicoEntity>> findAll() {
        return ResponseEntity.ok(servicoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoEntity> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicoService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoEntity> update(@PathVariable Long id, @Valid @RequestBody ServicoEntity servicoEntity) {
        return ResponseEntity.ok(servicoService.update(id, servicoEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
