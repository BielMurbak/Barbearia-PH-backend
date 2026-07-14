package com.barbearia.ph.controller;

import com.barbearia.ph.dto.AgendamentoRequestDTO;
import com.barbearia.ph.dto.AgendamentoResponseDTO;
import com.barbearia.ph.dto.DtoMapper;
import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> save(@Valid @RequestBody AgendamentoRequestDTO dto) {
        AgendamentoEntity salvo = agendamentoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequestDTO dto,
            @AuthenticationPrincipal UserDetails user) {
        AgendamentoEntity resultado = agendamentoService.update(id, dto);
        return ResponseEntity.ok(DtoMapper.toResponse(resultado));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchAgendamento(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates,
            @AuthenticationPrincipal UserDetails user) {

        boolean isCancelamento = updates.containsKey("status")
                && "CANCELADO".equalsIgnoreCase(updates.get("status").toString());

        if (isCancelamento && user != null) {
            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin) {
                try {
                    Long clienteId = agendamentoService.getClienteIdByCelular(user.getUsername());
                    AgendamentoEntity ag = agendamentoService.findById(id);
                    if (!ag.getClienteEntity().getId().equals(clienteId)) {
                        return ResponseEntity.status(403)
                                .body("Você só pode cancelar seus próprios agendamentos.");
                    }
                } catch (Exception e) {
                    return ResponseEntity.status(403).body("Acesso negado.");
                }
            }
        }

        return agendamentoService.patch(id, updates)
                .map(DtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> findAll(@AuthenticationPrincipal UserDetails user) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<AgendamentoResponseDTO> result;
        if (isAdmin) {
            result = agendamentoService.findAllWithDetails().stream()
                    .map(DtoMapper::toResponse).toList();
        } else {
            Long clienteId = agendamentoService.getClienteIdByCelular(user.getUsername());
            result = agendamentoService.findAllWithDetails().stream()
                    .filter(a -> a.getClienteEntity().getId().equals(clienteId))
                    .map(DtoMapper::toResponse).toList();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(DtoMapper.toResponse(agendamentoService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        agendamentoService.delete(id);
        return ResponseEntity.ok("Agendamento com ID " + id + " removido com sucesso.");
    }

    @GetMapping("/data")
    public ResponseEntity<List<AgendamentoResponseDTO>> findByData(@RequestParam String data) {
        List<AgendamentoResponseDTO> result = agendamentoService.findByData(LocalDate.parse(data)).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoResponseDTO>> findByCliente(@PathVariable Long clienteId) {
        List<AgendamentoResponseDTO> result = agendamentoService.findByCliente(clienteId).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoResponseDTO>> findByPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {
        List<AgendamentoResponseDTO> result = agendamentoService.findByPeriodo(
                LocalDate.parse(dataInicio), LocalDate.parse(dataFim)).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(result);
    }
}