package com.barbearia.ph.service;


import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalEntity save(ProfissionalEntity profissionalEntity){
        return profissionalRepository.save(profissionalEntity);
    }

    public List<ProfissionalEntity> findAll(){
        return profissionalRepository.findAll();
    }

    public ProfissionalEntity findById(Long id){
        return profissionalRepository.findById(id).orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
    }

    public ProfissionalEntity update(Long id, ProfissionalEntity profissionalEntity){
        ProfissionalEntity profissional = findById(id);
        profissional.setNome(profissionalEntity.getNome());
        profissional.setSobrenome(profissionalEntity.getSobrenome());
        profissional.setCelular(profissionalEntity.getCelular());
        profissional.setEspecializacao(profissionalEntity.getEspecializacao());
        return profissionalRepository.save(profissional);
    }

    public void delete(Long id){
        findById(id);
        profissionalRepository.deleteById(id);
    }
}
