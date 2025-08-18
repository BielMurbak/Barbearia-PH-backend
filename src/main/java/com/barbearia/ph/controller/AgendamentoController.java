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

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody AgendamentoEntity agendamento) {
        try {
            AgendamentoEntity salvo = agendamentoService.save(agendamento);
            return ResponseEntity.ok(salvo);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de negócio: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor, tente novamente mais tarde.");
        }
    }

    @GetMapping("/listar")
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
    
    @GetMapping("/buscar/data")
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
    
    @GetMapping("/buscar/cliente/{clienteId}")
    public ResponseEntity<?> findByCliente(@PathVariable Long clienteId) {
        try {
            List<AgendamentoEntity> agendamentos = agendamentoService.findByCliente(clienteId);
            return ResponseEntity.ok(agendamentos);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar agendamentos por cliente: " + ex.getMessage());
        }
    }
    
    @GetMapping("/buscar/periodo")
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
