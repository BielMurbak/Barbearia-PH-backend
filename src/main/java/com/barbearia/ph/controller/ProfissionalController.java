package com.barbearia.ph.controller;

import com.barbearia.ph.dto.DtoMapper;
import com.barbearia.ph.dto.ProfissionalRequestDTO;
import com.barbearia.ph.dto.ProfissionalResponseDTO;
import com.barbearia.ph.model.Especializacao;
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
@CrossOrigin(origins = "*")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<ProfissionalResponseDTO> save(@Valid @RequestBody ProfissionalRequestDTO dto) {
        ProfissionalEntity salvo = profissionalService.save(DtoMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toResponse(salvo));
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalResponseDTO>> findAll() {
        List<ProfissionalResponseDTO> profissionais = profissionalService.findAll().stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(DtoMapper.toResponse(profissionalService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProfissionalRequestDTO dto) {
        ProfissionalEntity atualizado = profissionalService.update(id, DtoMapper.toEntity(dto));
        return ResponseEntity.ok(DtoMapper.toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        profissionalService.delete(id);
        return ResponseEntity.ok("Profissional com ID " + id + " removido com sucesso.");
    }

    @GetMapping("/nome")
    public ResponseEntity<List<ProfissionalResponseDTO>> findByNome(@RequestParam String nome) {
        List<ProfissionalResponseDTO> profissionais = profissionalService.findByNome(nome).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/especializacao")
    public ResponseEntity<List<ProfissionalResponseDTO>> findByEspecializacao(@RequestParam String especializacao) {
        Especializacao esp = Especializacao.valueOf(especializacao.toUpperCase());
        List<ProfissionalResponseDTO> profissionais = profissionalService.findByEspecializacao(esp).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(profissionais);
    }
}
