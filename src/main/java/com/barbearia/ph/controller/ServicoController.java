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
    public ResponseEntity<?> save(@Valid @RequestBody ServicoEntity servicoEntity) {
        try {
            ServicoEntity salvo = servicoService.save(servicoEntity);
            return new ResponseEntity<>(salvo, HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao salvar serviço: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<ServicoEntity> servicos = servicoService.findAll();
            return ResponseEntity.ok(servicos);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao listar serviços: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            ServicoEntity servico = servicoService.findById(id);
            return ResponseEntity.ok(servico);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar serviço: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ServicoEntity servicoEntity) {
        try {
            ServicoEntity atualizado = servicoService.update(id, servicoEntity);
            return ResponseEntity.ok(atualizado);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar serviço: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            servicoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao deletar serviço: " + ex.getMessage());
        }
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