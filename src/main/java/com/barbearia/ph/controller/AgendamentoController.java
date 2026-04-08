package com.barbearia.ph.controller;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<AgendamentoEntity> save(@Valid @RequestBody AgendamentoEntity agendamento) {
        return ResponseEntity.ok(agendamentoService.save(agendamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody AgendamentoEntity agendamentoEntity,
            @AuthenticationPrincipal UserDetails user) {
        try {
            AgendamentoEntity resultado = agendamentoService.update(id, agendamentoEntity);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchAgendamento(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return agendamentoService.patch(id, updates)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoEntity>> findAll(
            @AuthenticationPrincipal UserDetails user) {

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return ResponseEntity.ok(agendamentoService.findAllWithDetails());
        } else {
            Long clienteId = agendamentoService.getClienteIdByCelular(user.getUsername());
            return ResponseEntity.ok(agendamentoService.findAllWithDetails()
                    .stream()
                    .filter(a -> a.getClienteEntity().getId().equals(clienteId))
                    .toList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoEntity> findById(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            agendamentoService.delete(id);
            return ResponseEntity.ok("Agendamento com ID " + id + " removido com sucesso.");
        }

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            agendamentoService.delete(id);
        } else {
            Long clienteId = agendamentoService.getClienteIdByCelular(user.getUsername());
            AgendamentoEntity agendamento = agendamentoService.findById(id);

            if (agendamento.getClienteEntity().getId().equals(clienteId)) {
                agendamentoService.delete(id);
            } else {
                return ResponseEntity.status(403).body("Você só pode excluir seus próprios agendamentos.");
            }
        }

        return ResponseEntity.ok("Agendamento com ID " + id + " removido com sucesso.");
    }

    @GetMapping("/data")
    public ResponseEntity<List<AgendamentoEntity>> findByData(@RequestParam String data) {
        LocalDate localDate = LocalDate.parse(data);
        return ResponseEntity.ok(agendamentoService.findByData(localDate));
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoEntity>> findByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(agendamentoService.findByCliente(clienteId));
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoEntity>> findByPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {
        LocalDate inicio = LocalDate.parse(dataInicio);
        LocalDate fim = LocalDate.parse(dataFim);
        return ResponseEntity.ok(agendamentoService.findByPeriodo(inicio, fim));
    }
}
