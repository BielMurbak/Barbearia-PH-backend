package com.barbearia.ph.service;


import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.repository.ClienteRepository;
import com.barbearia.ph.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalEntity save(ProfissionalEntity profissionalEntity){
        return profissionalRepository.save(profissionalEntity);
    }
}
