package com.barbearia.ph.service;


import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import com.barbearia.ph.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {
    private final ServicoRepository servicoRepository;
    private final ProfissionalServicoRepository profissionalServicoRepository;

    public ServicoEntity save(ServicoEntity servicoEntity) {
        return servicoRepository.save(servicoEntity);
    }

    public List<ServicoEntity> findAll(){
        return servicoRepository.findByAtivoTrue();
    }

    public ServicoEntity findById(Long id){
        return servicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
    }

    public ServicoEntity update(Long id, ServicoEntity servicoEntity){
        ServicoEntity servico = findById(id);
        servico.setDescricao(servicoEntity.getDescricao());
        servico.setMinDeDuracao(servicoEntity.getMinDeDuracao());
        return servicoRepository.save(servico);
    }

    // Soft delete: agendamentos antigos referenciam o serviço (direto e via
    // ProfissionalServicoEntity) por chave estrangeira, então excluir de verdade
    // quebraria o histórico. Só marca como inativo — some do catálogo, mas a
    // linha continua existindo pros agendamentos já feitos.
    public void delete(Long id){
        ServicoEntity servico = findById(id);
        servico.setAtivo(false);
        servicoRepository.save(servico);

        List<ProfissionalServicoEntity> vinculos = profissionalServicoRepository.findByServicoEntity(servico);
        vinculos.forEach(ps -> ps.setAtivo(false));
        profissionalServicoRepository.saveAll(vinculos);
    }
    
    public List<ServicoEntity> findByDescricao(String descricao){
        return servicoRepository.findByDescricaoIgnoreCaseContaining(descricao);
    }
    
    public List<ServicoEntity> findByDuracaoMaxima(int duracao){
        return servicoRepository.findByMinDeDuracaoLessThanEqual(duracao);
    }
    
    public List<ServicoEntity> findByDuracaoRange(int duracaoMin, int duracaoMax){
        return servicoRepository.findByDuracaoRange(duracaoMin, duracaoMax);
    }
}
