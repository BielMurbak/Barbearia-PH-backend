package com.barbearia.ph.repository;

import com.barbearia.ph.model.Especializacao;
import com.barbearia.ph.model.ProfissionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfissionalRepository extends JpaRepository<ProfissionalEntity, Long> {
    
    List<ProfissionalEntity> findByNomeIgnoreCaseContaining(String nome);
    
    List<ProfissionalEntity> findByEspecializacao(Especializacao especializacao);
    
    @Query("SELECT p FROM ProfissionalEntity p WHERE p.especializacao = :especializacao AND p.nome LIKE %:nome%")
    List<ProfissionalEntity> findByEspecializacaoAndNomeContaining(@Param("especializacao") Especializacao especializacao, @Param("nome") String nome);
}
