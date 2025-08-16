package com.barbearia.ph.repository;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.model.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ServicoRepository extends JpaRepository<ServicoEntity, Long> {
}
