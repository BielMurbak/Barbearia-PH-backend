package com.barbearia.ph.controller;

import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.service.ProfissionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping("/save")
    public ResponseEntity<ProfissionalEntity> save(@Valid @RequestBody ProfissionalEntity profissionalEntity) {
        return ResponseEntity.ok(profissionalService.save(profissionalEntity));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ProfissionalEntity>> findAll() {
        return ResponseEntity.ok(profissionalService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalEntity> findById(@PathVariable Long id) {
        return ResponseEntity.ok(profissionalService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalEntity> update(@PathVariable Long id, @Valid @RequestBody ProfissionalEntity profissionalEntity) {
        return ResponseEntity.ok(profissionalService.update(id, profissionalEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profissionalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
