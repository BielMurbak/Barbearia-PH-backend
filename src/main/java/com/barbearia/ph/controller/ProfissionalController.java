package com.barbearia.ph.controller;

import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.service.ProfissionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody ProfissionalEntity profissionalEntity) {
        try {
            ProfissionalEntity salvo = profissionalService.save(profissionalEntity);
            return ResponseEntity.ok(salvo);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao salvar profissional: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<ProfissionalEntity> profissionais = profissionalService.findAll();
            return ResponseEntity.ok(profissionais);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao listar profissionais: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            ProfissionalEntity profissional = profissionalService.findById(id);
            return ResponseEntity.ok(profissional);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar profissional com ID " + id + ": " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProfissionalEntity profissionalEntity) {
        try {
            ProfissionalEntity atualizado = profissionalService.update(id, profissionalEntity);
            return ResponseEntity.ok(atualizado);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar profissional com ID " + id + ": " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            profissionalService.delete(id);
            return ResponseEntity.ok("Profissional com ID " + id + " removido com sucesso.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao deletar profissional com ID " + id + ": " + ex.getMessage());
        }
    }
    
    @GetMapping("/nome")
    public ResponseEntity<?> findByNome(@RequestParam String nome) {
        try {
            List<ProfissionalEntity> profissionais = profissionalService.findByNome(nome);
            return ResponseEntity.ok(profissionais);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar profissionais por nome: " + ex.getMessage());
        }
    }
    
    @GetMapping("/especializacao")
    public ResponseEntity<?> findByEspecializacao(@RequestParam String especializacao) {
        try {
            com.barbearia.ph.model.Especializacao esp = com.barbearia.ph.model.Especializacao.valueOf(especializacao.toUpperCase());
            List<ProfissionalEntity> profissionais = profissionalService.findByEspecializacao(esp);
            return ResponseEntity.ok(profissionais);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar profissionais por especialização: " + ex.getMessage());
        }
    }
}
