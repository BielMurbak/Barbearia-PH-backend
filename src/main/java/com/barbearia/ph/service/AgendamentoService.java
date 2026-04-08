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
    private final ClienteService clienteService;

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

        atualizarStatus(agendamento);
        AgendamentoEntity salvo = agendamentoRepository.save(agendamento);
        // Retorna com todos os detalhes
        return agendamentoRepository.findByIdWithDetails(salvo.getId()).orElse(salvo);
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

    @Transactional
    public AgendamentoEntity update(Long id, AgendamentoEntity agendamentoEntity){
        AgendamentoEntity agendamento = findById(id);
        agendamento.setData(agendamentoEntity.getData());
        agendamento.setHorario(agendamentoEntity.getHorario());
        agendamento.setLocal(agendamentoEntity.getLocal());

        // Atualiza campos opcionais
        if (agendamentoEntity.getObservacoes() != null) {
            agendamento.setObservacoes(agendamentoEntity.getObservacoes());
        }
        if (agendamentoEntity.getPreco() != null) {
            agendamento.setPreco(agendamentoEntity.getPreco());
        }
        
        // Busca cliente completo
        if (agendamentoEntity.getClienteEntity() != null && agendamentoEntity.getClienteEntity().getId() != null) {
            ClienteEntity cliente = clienteRepository.findById(agendamentoEntity.getClienteEntity().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            agendamento.setClienteEntity(cliente);
        }
        
        // Busca profissionalServico completo
        if (agendamentoEntity.getProfissionalServicoEntity() != null && agendamentoEntity.getProfissionalServicoEntity().getId() != null) {
            ProfissionalServicoEntity profServ = profissionalServicoRepository.findById(agendamentoEntity.getProfissionalServicoEntity().getId())
                    .orElseThrow(() -> new RuntimeException("ProfissionalServico não encontrado"));
            agendamento.setProfissionalServicoEntity(profServ);
        }

        atualizarStatus(agendamento);
        AgendamentoEntity salvo = agendamentoRepository.save(agendamento);
        // Retorna com todos os detalhes (servicoEntity, profissionalEntity, clienteEntity completos)
        return agendamentoRepository.findByIdWithDetails(salvo.getId()).orElse(salvo);
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
            if (updates.containsKey("observacoes")) {
                agendamento.setObservacoes((String) updates.get("observacoes"));
            }
            if (updates.containsKey("preco")) {
                agendamento.setPreco(Double.valueOf(updates.get("preco").toString()));
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

            // Permite CANCELADO manualmente
            if (updates.containsKey("status")) {
                String statusStr = updates.get("status").toString();
                if ("CANCELADO".equalsIgnoreCase(statusStr)) {
                    agendamento.setStatus(AgendamentoEntity.StatusAgendamento.CANCELADO);
                } else if ("CONCLUIDO".equalsIgnoreCase(statusStr)) {
                    agendamento.setStatus(AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                } else if ("PENDENTE".equalsIgnoreCase(statusStr)) {
                    agendamento.setStatus(AgendamentoEntity.StatusAgendamento.PENDENTE);
                }
            } else {
                // Atualiza automaticamente PENDENTE/CONCLUIDO se não estiver CANCELADO
                atualizarStatus(agendamento);
            }

            return agendamentoRepository.save(agendamento);
        });
    }

    public List<AgendamentoEntity> findAllWithDetails() {
        return agendamentoRepository.findAllWithDetails();
    }

    private void atualizarStatus(AgendamentoEntity agendamento) {
        LocalDateTime inicio = LocalDateTime.of(agendamento.getData(), LocalTime.parse(agendamento.getHorario()));
        if (agendamento.getStatus() != AgendamentoEntity.StatusAgendamento.CANCELADO) {
            if (inicio.isBefore(LocalDateTime.now())) {
                agendamento.setStatus(AgendamentoEntity.StatusAgendamento.CONCLUIDO);
            } else {
                agendamento.setStatus(AgendamentoEntity.StatusAgendamento.PENDENTE);
            }
        }
    }

    public Long getClienteIdByCelular(String celular) {
        ClienteEntity cliente = clienteRepository.findByCelular(celular)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return cliente.getId();
    }
}
