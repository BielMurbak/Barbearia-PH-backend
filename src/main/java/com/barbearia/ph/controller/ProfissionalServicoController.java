package com.barbearia.ph.controller;

import com.barbearia.ph.dto.DtoMapper;
import com.barbearia.ph.dto.ProfissionalServicoRequestDTO;
import com.barbearia.ph.dto.ProfissionalServicoResponseDTO;
import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.service.ProfissionalServicoService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<ProfissionalServicoResponseDTO> save(@Valid @RequestBody ProfissionalServicoRequestDTO dto) {
        ProfissionalServicoEntity salvo = profissionalServicoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalServicoResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProfissionalServicoRequestDTO dto) {
        ProfissionalServicoEntity updated = profissionalServicoService.update(id, dto);
        return ResponseEntity.ok(DtoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profissionalServicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalServicoResponseDTO>> findAll() {
        List<ProfissionalServicoResponseDTO> list = profissionalServicoService.findAll().stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalServicoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(DtoMapper.toResponse(profissionalServicoService.findById(id)));
    }
}