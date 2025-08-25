package com.barbearia.ph.service;

import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfissionalServicoService {

    private final ProfissionalServicoRepository profissionalServicoRepository;

    // CREATE
    public ProfissionalServicoEntity save(ProfissionalServicoEntity profissionalServicoEntity) {
        return profissionalServicoRepository.save(profissionalServicoEntity);
    }

    // UPDATE
    public ProfissionalServicoEntity update(ProfissionalServicoEntity profissionalServicoEntity) {
        if (profissionalServicoEntity.getId() == null) {
            throw new IllegalArgumentException("ID do profissional serviço é obrigatório para update");
        }
        return profissionalServicoRepository.save(profissionalServicoEntity);
    }

    // DELETE
    public void delete(Long id) {
        profissionalServicoRepository.deleteById(id);
    }

    // FIND ALL
    public List<ProfissionalServicoEntity> findAll() {
        return profissionalServicoRepository.findAll();
    }

    // FIND BY ID
    public ProfissionalServicoEntity findById(Long id) {
        Optional<ProfissionalServicoEntity> optional = profissionalServicoRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Profissional Serviço não encontrado com ID: " + id));
    }
}
