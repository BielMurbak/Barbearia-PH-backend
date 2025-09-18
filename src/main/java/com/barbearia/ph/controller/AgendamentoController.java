package com.barbearia.ph.controller;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.repository.AgendamentoRepository;
import com.barbearia.ph.repository.ClienteRepository;
import com.barbearia.ph.repository.ProfissionalRepository;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import com.barbearia.ph.service.AgendamentoService;
import com.barbearia.ph.service.ProfissionalServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AgendamentoController {


    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody AgendamentoEntity agendamento) {
        try {
            AgendamentoEntity salvo = agendamentoService.save(agendamento);
            return ResponseEntity.ok(salvo);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de negócio: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody AgendamentoEntity agendamentoEntity) {
        try {
            AgendamentoEntity atualizado = agendamentoService.update(id, agendamentoEntity);
            return ResponseEntity.ok(atualizado);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar agendamento com ID " + id + ": " + ex.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchAgendamento(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            Optional<AgendamentoEntity> agendamentoAtualizado = agendamentoService.patch(id, updates);
            return agendamentoAtualizado
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Agendamento não encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar agendamento: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<AgendamentoEntity> agendamentos = agendamentoService.findAll();
            return ResponseEntity.ok(agendamentos);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao listar agendamentos: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            AgendamentoEntity agendamento = agendamentoService.findById(id);
            return ResponseEntity.ok(agendamento);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar agendamento com ID " + id + ": " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            agendamentoService.delete(id);
            return ResponseEntity.ok("Agendamento com ID " + id + " removido com sucesso.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao deletar agendamento com ID " + id + ": " + ex.getMessage());
        }
    }

    @GetMapping("/data")
    public ResponseEntity<?> findByData(@RequestParam String data) {
        try {
            java.time.LocalDate localDate = java.time.LocalDate.parse(data);
            List<AgendamentoEntity> agendamentos = agendamentoService.findByData(localDate);
            return ResponseEntity.ok(agendamentos);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar agendamentos por data: " + ex.getMessage());
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> findByCliente(@PathVariable Long clienteId) {
        try {
            List<AgendamentoEntity> agendamentos = agendamentoService.findByCliente(clienteId);
            return ResponseEntity.ok(agendamentos);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar agendamentos por cliente: " + ex.getMessage());
        }
    }

    @GetMapping("/periodo")
    public ResponseEntity<?> findByPeriodo(@RequestParam String dataInicio, @RequestParam String dataFim) {
        try {
            java.time.LocalDate inicio = java.time.LocalDate.parse(dataInicio);
            java.time.LocalDate fim = java.time.LocalDate.parse(dataFim);
            List<AgendamentoEntity> agendamentos = agendamentoService.findByPeriodo(inicio, fim);
            return ResponseEntity.ok(agendamentos);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar agendamentos por período: " + ex.getMessage());
        }
    }
}
