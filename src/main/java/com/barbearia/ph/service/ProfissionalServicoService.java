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

    // FIND BY ID
    public ProfissionalServicoEntity findById(Long id) {
        Optional<ProfissionalServicoEntity> optional = profissionalServicoRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Profissional Serviço não encontrado com ID: " + id));
    }
}
