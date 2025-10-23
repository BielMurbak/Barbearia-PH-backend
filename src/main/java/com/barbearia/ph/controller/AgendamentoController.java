package com.barbearia.ph.controller;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoEntity> save(@Valid @RequestBody AgendamentoEntity agendamento) {
        return ResponseEntity.ok(agendamentoService.save(agendamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoEntity> update(@PathVariable Long id, @Valid @RequestBody AgendamentoEntity agendamentoEntity) {
        return ResponseEntity.ok(agendamentoService.update(id, agendamentoEntity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AgendamentoEntity> patchAgendamento(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.of(agendamentoService.patch(id, updates));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoEntity>> findAll() {
        return ResponseEntity.ok(agendamentoService.findAllWithDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoEntity> findById(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        agendamentoService.delete(id);
        return ResponseEntity.ok("Agendamento com ID " + id + " removido com sucesso.");
    }

    @GetMapping("/data")
    public ResponseEntity<List<AgendamentoEntity>> findByData(@RequestParam String data) {
        LocalDate localDate = LocalDate.parse(data);
        return ResponseEntity.ok(agendamentoService.findByData(localDate));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AgendamentoEntity>> findByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(agendamentoService.findByCliente(clienteId));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<AgendamentoEntity>> findByPeriodo(@RequestParam String dataInicio, @RequestParam String dataFim) {
        LocalDate inicio = LocalDate.parse(dataInicio);
        LocalDate fim = LocalDate.parse(dataFim);
        return ResponseEntity.ok(agendamentoService.findByPeriodo(inicio, fim));
    }
}
