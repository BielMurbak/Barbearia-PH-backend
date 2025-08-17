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
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao salvar agendamento: " + ex.getMessage());
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
}
