package com.barbearia.ph.service;


import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfissionalServicoService {

    private final ProfissionalServicoRepository profissionalServicoRepository;

    public ProfissionalServicoEntity save(ProfissionalServicoEntity profissionalServicoEntity) {
        return profissionalServicoRepository.save(profissionalServicoEntity);
    }

}
