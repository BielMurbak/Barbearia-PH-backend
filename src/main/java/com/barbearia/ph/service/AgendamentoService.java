package com.barbearia.ph.service;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.repository.AgendamentoRepository;
import com.barbearia.ph.repository.ClienteRepository;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalServicoRepository profissionalServicoRepository;

    public AgendamentoEntity save(AgendamentoEntity agendamento) {
        ClienteEntity cliente = clienteRepository.findById(agendamento.getClienteEntity().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        ProfissionalServicoEntity profServ = profissionalServicoRepository.findById(agendamento.getProfissionalServicoEntity().getId())
                .orElseThrow(() -> new RuntimeException("ProfissionalServico não encontrado"));
        
        agendamento.setClienteEntity(cliente);
        agendamento.setProfissionalServicoEntity(profServ);
        
        return agendamentoRepository.save(agendamento);
    }

    public List<AgendamentoEntity> findAll() {
        return agendamentoRepository.findAll();
    }

    public AgendamentoEntity findById(Long id){
        return agendamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
    }

    public void delete(Long id){
        findById(id);
        agendamentoRepository.deleteById(id);
    }
}
