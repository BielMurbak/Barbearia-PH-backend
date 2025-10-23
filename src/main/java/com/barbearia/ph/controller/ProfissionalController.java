package com.barbearia.ph.controller;

import com.barbearia.ph.model.Especializacao;
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
@CrossOrigin(origins = "http://localhost:4200")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<ProfissionalEntity> save(@Valid @RequestBody ProfissionalEntity profissionalEntity) {
        ProfissionalEntity salvo = profissionalService.save(profissionalEntity);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalEntity>> findAll() {
        List<ProfissionalEntity> profissionais = profissionalService.findAll();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalEntity> findById(@PathVariable Long id) {
        ProfissionalEntity profissional = profissionalService.findById(id);
        return ResponseEntity.ok(profissional);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalEntity> update(@PathVariable Long id, @Valid @RequestBody ProfissionalEntity profissionalEntity) {
        ProfissionalEntity atualizado = profissionalService.update(id, profissionalEntity);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        profissionalService.delete(id);
        return ResponseEntity.ok("Profissional com ID " + id + " removido com sucesso.");
    }

    @GetMapping("/nome")
    public ResponseEntity<List<ProfissionalEntity>> findByNome(@RequestParam String nome) {
        List<ProfissionalEntity> profissionais = profissionalService.findByNome(nome);
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/especializacao")
    public ResponseEntity<List<ProfissionalEntity>> findByEspecializacao(@RequestParam String especializacao) {
        Especializacao esp = Especializacao.valueOf(especializacao.toUpperCase());
        List<ProfissionalEntity> profissionais = profissionalService.findByEspecializacao(esp);
        return ResponseEntity.ok(profissionais);
    }
}