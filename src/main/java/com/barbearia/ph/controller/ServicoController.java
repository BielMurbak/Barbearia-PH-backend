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
            return new ResponseEntity<>(salvo, HttpStatus.CREATED); // 201 para criação
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Erro de validação: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro inesperado: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<ServicoEntity> servicos = servicoService.findAll();

            if (servicos.isEmpty()) {
                return new ResponseEntity<>("Nenhum serviço encontrado.", HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(servicos, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao buscar serviços: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            ServicoEntity servico = servicoService.findById(id);

            if (servico == null) {
                return new ResponseEntity<>("Serviço com ID " + id + " não encontrado.", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(servico, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao buscar serviço: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ServicoEntity servicoEntity) {
        try {
            ServicoEntity atualizado = servicoService.update(id, servicoEntity);

            if (atualizado == null) {
                return new ResponseEntity<>("Serviço com ID " + id + " não encontrado.", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(atualizado, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Erro de validação: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erro ao atualizar serviço: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            servicoService.delete(id);
            return ResponseEntity.ok("Serviço com ID " + id + " foi removido com sucesso.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao remover serviço: " + ex.getMessage());
        }
    }
    
    @GetMapping("/descricao")
    public ResponseEntity<?> findByDescricao(@RequestParam String descricao) {
        try {
            List<ServicoEntity> servicos = servicoService.findByDescricao(descricao);
            return ResponseEntity.ok(servicos);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar serviços por descrição: " + ex.getMessage());
        }
    }
    
    @GetMapping("/duracao-maxima")
    public ResponseEntity<?> findByDuracaoMaxima(@RequestParam int duracao) {
        try {
            List<ServicoEntity> servicos = servicoService.findByDuracaoMaxima(duracao);
            return ResponseEntity.ok(servicos);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar serviços por duração máxima: " + ex.getMessage());
        }
    }
    
    @GetMapping("/duracao-range")
    public ResponseEntity<?> findByDuracaoRange(@RequestParam int duracaoMin, @RequestParam int duracaoMax) {
        try {
            List<ServicoEntity> servicos = servicoService.findByDuracaoRange(duracaoMin, duracaoMax);
            return ResponseEntity.ok(servicos);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar serviços por faixa de duração: " + ex.getMessage());
        }
    }
}
