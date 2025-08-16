package com.barbearia.ph.repository;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.model.ProfissionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfissionalRepository extends JpaRepository<ProfissionalEntity, Long> {
}
