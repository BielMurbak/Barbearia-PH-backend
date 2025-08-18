package com.barbearia.ph.repository;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.model.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<AgendamentoEntity, Long>{
    
    List<AgendamentoEntity> findByData(LocalDate data);
    
    List<AgendamentoEntity> findByClienteEntity(ClienteEntity cliente);
    
    @Query("SELECT a FROM AgendamentoEntity a WHERE a.data BETWEEN :dataInicio AND :dataFim ORDER BY a.data, a.horario")
    List<AgendamentoEntity> findAgendamentosPorPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    List<AgendamentoEntity> findByDataAndProfissionalServicoEntity_ProfissionalEntity_Id(LocalDate data, Long profissionalId);

}
