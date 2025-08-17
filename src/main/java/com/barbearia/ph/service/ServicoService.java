package com.barbearia.ph.service;


import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {
    private final ServicoRepository servicoRepository;

    public ServicoEntity save(ServicoEntity servicoEntity) {
        return servicoRepository.save(servicoEntity);
    }

    public List<ServicoEntity> findAll(){
        return servicoRepository.findAll();
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

    public void delete(Long id){
        findById(id);
        servicoRepository.deleteById(id);
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
