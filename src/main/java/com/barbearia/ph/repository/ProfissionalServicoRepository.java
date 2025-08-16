package com.barbearia.ph.repository;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.model.ProfissionalServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfissionalServicoRepository extends JpaRepository<ProfissionalServicoEntity, Long> {
}
