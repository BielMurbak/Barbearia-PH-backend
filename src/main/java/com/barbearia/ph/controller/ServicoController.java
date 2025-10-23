package com.barbearia.ph.controller;

import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping
    public ResponseEntity<ServicoEntity> save(@Valid @RequestBody ServicoEntity servicoEntity) {
        ServicoEntity salvo = servicoService.save(servicoEntity);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServicoEntity>> findAll() {
        List<ServicoEntity> servicos = servicoService.findAll();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoEntity> findById(@PathVariable Long id) {
        ServicoEntity servico = servicoService.findById(id);
        return ResponseEntity.ok(servico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoEntity> update(@PathVariable Long id, @Valid @RequestBody ServicoEntity servicoEntity) {
        ServicoEntity atualizado = servicoService.update(id, servicoEntity);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/descricao")
    public ResponseEntity<List<ServicoEntity>> findByDescricao(@RequestParam String descricao) {
        List<ServicoEntity> servicos = servicoService.findByDescricao(descricao);
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/duracao-maxima")
    public ResponseEntity<List<ServicoEntity>> findByDuracaoMaxima(@RequestParam int duracao) {
        List<ServicoEntity> servicos = servicoService.findByDuracaoMaxima(duracao);
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/duracao-range")
    public ResponseEntity<List<ServicoEntity>> findByDuracaoRange(@RequestParam int duracaoMin, @RequestParam int duracaoMax) {
        List<ServicoEntity> servicos = servicoService.findByDuracaoRange(duracaoMin, duracaoMax);
        return ResponseEntity.ok(servicos);
    }
}