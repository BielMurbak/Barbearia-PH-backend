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
    private final ClienteRepository clienteRepository;
    private final ProfissionalServicoRepository profissionalServicoRepository;
    private final AgendamentoRepository agendamentoRepository;


    @PostMapping("/save")
    public ResponseEntity<AgendamentoEntity> salvarAgendamento(@RequestBody AgendamentoEntity agendamento) {

        ClienteEntity cliente = clienteRepository.findById(agendamento.getClienteEntity().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        ProfissionalServicoEntity profServ = profissionalServicoRepository.findById(agendamento.getProfissionalServicoEntity().getId())
                .orElseThrow(() -> new RuntimeException("ProfissionalServico não encontrado"));

        agendamento.setClienteEntity(cliente);
        agendamento.setProfissionalServicoEntity(profServ);

        AgendamentoEntity salvo = agendamentoRepository.save(agendamento);

        return ResponseEntity.ok(salvo);
    }


}
