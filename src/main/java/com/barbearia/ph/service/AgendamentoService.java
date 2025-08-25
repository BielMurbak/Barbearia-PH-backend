package com.barbearia.ph.service;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.repository.AgendamentoRepository;
import com.barbearia.ph.repository.ClienteRepository;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
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

        atualizarStatus(agendamento); // <-- Aqui atualiza o status automaticamente
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

    public AgendamentoEntity update(Long id, AgendamentoEntity agendamentoEntity){
        AgendamentoEntity agendamento = findById(id);
        agendamento.setData(agendamentoEntity.getData());
        agendamento.setHorario(agendamentoEntity.getHorario());
        agendamento.setLocal(agendamentoEntity.getLocal());
        agendamento.setClienteEntity(agendamentoEntity.getClienteEntity());
        agendamento.setProfissionalServicoEntity(agendamentoEntity.getProfissionalServicoEntity());

        atualizarStatus(agendamento); // <-- Atualiza status ao modificar
        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Optional<AgendamentoEntity> patch(Long id, Map<String, Object> updates) {
        return agendamentoRepository.findById(id).map(agendamento -> {
            if (updates.containsKey("data")) {
                agendamento.setData(LocalDate.parse((String) updates.get("data")));
            }
            if (updates.containsKey("horario")) {
                agendamento.setHorario((String) updates.get("horario"));
            }
            if (updates.containsKey("local")) {
                agendamento.setLocal((String) updates.get("local"));
            }
            if (updates.containsKey("clienteId")) {
                Long clienteId = Long.valueOf(updates.get("clienteId").toString());
                ClienteEntity cliente = clienteRepository.findById(clienteId)
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                agendamento.setClienteEntity(cliente);
            }
            if (updates.containsKey("profissionalServicoId")) {
                Long psId = Long.valueOf(updates.get("profissionalServicoId").toString());
                ProfissionalServicoEntity ps = profissionalServicoRepository.findById(psId)
                        .orElseThrow(() -> new RuntimeException("ProfissionalServiço não encontrado"));
                agendamento.setProfissionalServicoEntity(ps);
            }

            // Permitir CANCELADO manualmente
            if (updates.containsKey("status")) {
                String statusStr = updates.get("status").toString();
                if ("CANCELADO".equalsIgnoreCase(statusStr)) {
                    agendamento.setStatus(AgendamentoEntity.StatusAgendamento.CANCELADO);
                }
            }

            atualizarStatus(agendamento); // Atualiza automaticamente PENDENTE/CONCLUIDO se não estiver CANCELADO
            return agendamentoRepository.save(agendamento);
        });
    }

    private void atualizarStatus(AgendamentoEntity agendamento) {
        // Converte a data e hora do agendamento para LocalDateTime
        LocalDateTime inicio = LocalDateTime.of(agendamento.getData(), LocalTime.parse(agendamento.getHorario()));

        // Se já passou, concluiu, senão pendente
        if (agendamento.getStatus() != AgendamentoEntity.StatusAgendamento.CANCELADO) {
            if (inicio.isBefore(LocalDateTime.now())) {
                agendamento.setStatus(AgendamentoEntity.StatusAgendamento.CONCLUIDO);
            } else {
                agendamento.setStatus(AgendamentoEntity.StatusAgendamento.PENDENTE);
            }
        }
    }
}
