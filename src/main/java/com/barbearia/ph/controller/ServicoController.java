package com.barbearia.ph.controller;

import com.barbearia.ph.dto.DtoMapper;
import com.barbearia.ph.dto.ServicoRequestDTO;
import com.barbearia.ph.dto.ServicoResponseDTO;
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
@CrossOrigin(origins = "*")
public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> save(@Valid @RequestBody ServicoRequestDTO dto) {
        ServicoEntity salvo = servicoService.save(DtoMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toResponse(salvo));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> findAll() {
        List<ServicoResponseDTO> servicos = servicoService.findAll().stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(DtoMapper.toResponse(servicoService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ServicoRequestDTO dto) {
        ServicoEntity atualizado = servicoService.update(id, DtoMapper.toEntity(dto));
        return ResponseEntity.ok(DtoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/descricao")
    public ResponseEntity<List<ServicoResponseDTO>> findByDescricao(@RequestParam String descricao) {
        List<ServicoResponseDTO> servicos = servicoService.findByDescricao(descricao).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/duracao-maxima")
    public ResponseEntity<List<ServicoResponseDTO>> findByDuracaoMaxima(@RequestParam int duracao) {
        List<ServicoResponseDTO> servicos = servicoService.findByDuracaoMaxima(duracao).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/duracao-range")
    public ResponseEntity<List<ServicoResponseDTO>> findByDuracaoRange(@RequestParam int duracaoMin, @RequestParam int duracaoMax) {
        List<ServicoResponseDTO> servicos = servicoService.findByDuracaoRange(duracaoMin, duracaoMax).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(servicos);
    }
}