package com.barbearia.ph.repository;

import com.barbearia.ph.model.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicoRepository extends JpaRepository<ServicoEntity, Long> {
    
    List<ServicoEntity> findByDescricaoIgnoreCaseContaining(String descricao);
    
    List<ServicoEntity> findByMinDeDuracaoLessThanEqual(int duracao);
    
    @Query("SELECT s FROM ServicoEntity s WHERE s.minDeDuracao BETWEEN :duracaoMin AND :duracaoMax")
    List<ServicoEntity> findByDuracaoRange(@Param("duracaoMin") int duracaoMin, @Param("duracaoMax") int duracaoMax);
}
