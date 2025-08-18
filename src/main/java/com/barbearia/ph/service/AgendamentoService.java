package com.barbearia.ph.service;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.repository.AgendamentoRepository;
import com.barbearia.ph.repository.ClienteRepository;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

        int duracaoMinutos = profServ.getServicoEntity().getMinDeDuracao();

        LocalDateTime inicioNovo = LocalDateTime.of(agendamento.getData(), LocalTime.parse(agendamento.getHorario()));
        LocalDateTime fimNovo = inicioNovo.plusMinutes(duracaoMinutos);

        List<AgendamentoEntity> agendamentosDia = agendamentoRepository
                .findByDataAndProfissionalServicoEntity_ProfissionalEntity_Id(agendamento.getData(),
                        profServ.getProfissionalEntity().getId());

        for (AgendamentoEntity ag : agendamentosDia) {
            LocalDateTime inicioExistente = LocalDateTime.of(ag.getData(), LocalTime.parse(ag.getHorario()));
            int duracaoExistente = ag.getProfissionalServicoEntity().getServicoEntity().getMinDeDuracao();
            LocalDateTime fimExistente = inicioExistente.plusMinutes(duracaoExistente);

            boolean conflita = inicioNovo.isBefore(fimExistente) && fimNovo.isAfter(inicioExistente);
            if (conflita) {
                throw new RuntimeException("Horário indisponível: conflito com outro agendamento.");
            }
        }

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
    
    public List<AgendamentoEntity> findByData(java.time.LocalDate data){
        return agendamentoRepository.findByData(data);
    }
    
    public List<AgendamentoEntity> findByCliente(Long clienteId){
        ClienteEntity cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return agendamentoRepository.findByClienteEntity(cliente);
    }
    
    public List<AgendamentoEntity> findByPeriodo(java.time.LocalDate dataInicio, java.time.LocalDate dataFim){
        return agendamentoRepository.findAgendamentosPorPeriodo(dataInicio, dataFim);
    }
}
