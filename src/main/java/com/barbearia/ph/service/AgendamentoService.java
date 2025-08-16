package com.barbearia.ph.service;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private AgendamentoRepository agendamentoRepository;

    public AgendamentoEntity save(AgendamentoEntity agendamento) {
        return agendamentoRepository.save(agendamento);
    }

    public List<AgendamentoEntity> listar() {
        return agendamentoRepository.findAll();
    }
}
