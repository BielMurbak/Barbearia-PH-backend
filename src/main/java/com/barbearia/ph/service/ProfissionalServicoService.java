package com.barbearia.ph.service;

import com.barbearia.ph.dto.ProfissionalServicoRequestDTO;
import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.repository.ProfissionalRepository;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import com.barbearia.ph.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfissionalServicoService {

    private final ProfissionalServicoRepository profissionalServicoRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;

    public ProfissionalServicoEntity save(ProfissionalServicoRequestDTO dto) {
        ProfissionalEntity profissional = profissionalRepository.findById(dto.getProfissionalId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
        ServicoEntity servico = servicoRepository.findById(dto.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        ProfissionalServicoEntity entity = new ProfissionalServicoEntity();
        entity.setProfissionalEntity(profissional);
        entity.setServicoEntity(servico);
        entity.setPreco(dto.getPreco());
        return profissionalServicoRepository.save(entity);
    }

    public ProfissionalServicoEntity update(Long id, ProfissionalServicoRequestDTO dto) {
        ProfissionalServicoEntity entity = findById(id);
        ProfissionalEntity profissional = profissionalRepository.findById(dto.getProfissionalId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
        ServicoEntity servico = servicoRepository.findById(dto.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        entity.setProfissionalEntity(profissional);
        entity.setServicoEntity(servico);
        entity.setPreco(dto.getPreco());
        return profissionalServicoRepository.save(entity);
    }

    public void delete(Long id) {
        profissionalServicoRepository.deleteById(id);
    }

    public List<ProfissionalServicoEntity> findAll() {
        return profissionalServicoRepository.findAll();
    }

    public ProfissionalServicoEntity findById(Long id) {
        return profissionalServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional Serviço não encontrado com ID: " + id));
    }
}
