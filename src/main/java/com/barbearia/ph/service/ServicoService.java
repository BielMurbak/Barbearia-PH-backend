package com.barbearia.ph.service;


import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import com.barbearia.ph.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicoService {
    private final ServicoRepository servicoRepository;

    public ServicoEntity save(ServicoEntity servicoEntity) {
        return servicoRepository.save(servicoEntity);
    }

}
