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
public class ProfissionalServicoController {

    private final ProfissionalServicoService profissionalServicoService;

    // CREATE
    @PostMapping
    public ResponseEntity<ProfissionalServicoEntity> save(@RequestBody ProfissionalServicoEntity profissionalServicoEntity) {
        try {
            return new ResponseEntity<>(profissionalServicoService.save(profissionalServicoEntity), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalServicoEntity> update(
            @PathVariable Long id,
            @RequestBody ProfissionalServicoEntity profissionalServicoEntity
    ) {
        try {
            profissionalServicoEntity.setId(id);
            ProfissionalServicoEntity updated = profissionalServicoService.update(profissionalServicoEntity);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            profissionalServicoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // FIND ALL
    @GetMapping
    public ResponseEntity<List<ProfissionalServicoEntity>> findAll() {
        try {
            List<ProfissionalServicoEntity> list = profissionalServicoService.findAll();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalServicoEntity> findById(@PathVariable Long id) {
        try {
            ProfissionalServicoEntity entity = profissionalServicoService.findById(id);
            return ResponseEntity.ok(entity);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
