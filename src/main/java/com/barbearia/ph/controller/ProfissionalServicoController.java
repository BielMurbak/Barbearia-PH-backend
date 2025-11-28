package com.barbearia.ph.controller;

import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.service.ProfissionalServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais/servicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfissionalServicoController {

    private final ProfissionalServicoService profissionalServicoService;

    // CREATE
    @PostMapping
    public ResponseEntity<ProfissionalServicoEntity> save(@RequestBody ProfissionalServicoEntity profissionalServicoEntity) {
        ProfissionalServicoEntity salvo = profissionalServicoService.save(profissionalServicoEntity);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalServicoEntity> update(
            @PathVariable Long id,
            @RequestBody ProfissionalServicoEntity profissionalServicoEntity
    ) {
        profissionalServicoEntity.setId(id);
        ProfissionalServicoEntity updated = profissionalServicoService.update(profissionalServicoEntity);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profissionalServicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // FIND ALL
    @GetMapping
    public ResponseEntity<List<ProfissionalServicoEntity>> findAll() {
        List<ProfissionalServicoEntity> list = profissionalServicoService.findAll();
        return ResponseEntity.ok(list);
    }

    // FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalServicoEntity> findById(@PathVariable Long id) {
        ProfissionalServicoEntity entity = profissionalServicoService.findById(id);
        return ResponseEntity.ok(entity);
    }
}