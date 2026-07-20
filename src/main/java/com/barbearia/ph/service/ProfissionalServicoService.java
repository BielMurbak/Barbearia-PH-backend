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

    // DELETE (soft delete)
    // Agendamentos antigos referenciam essa linha por chave estrangeira, então
    // "excluir" nunca apaga de verdade — só marca como inativo, o que já basta
    // pra sumir de qualquer listagem/catálogo. O agendamento continua existindo
    // e mostrando os dados congelados de quando foi feito, sem qualquer mudança.
    public void delete(Long id) {
        ProfissionalServicoEntity ps = findById(id);
        ps.setAtivo(false);
        profissionalServicoRepository.save(ps);
    }

    // FIND ALL (só os ativos — o que ainda está no catálogo)
    public List<ProfissionalServicoEntity> findAll() {
        return profissionalServicoRepository.findByAtivoTrue();
    }

    public ProfissionalServicoEntity findById(Long id) {
        return profissionalServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional Serviço não encontrado com ID: " + id));
    }
}
