package com.barbearia.ph.repository;

import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.model.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfissionalServicoRepository extends JpaRepository<ProfissionalServicoEntity, Long> {
    
    List<ProfissionalServicoEntity> findByProfissionalEntity(ProfissionalEntity profissional);
    
    List<ProfissionalServicoEntity> findByPrecoLessThanEqual(Double preco);
    
    @Query("SELECT ps FROM ProfissionalServicoEntity ps WHERE ps.preco BETWEEN :precoMin AND :precoMax ORDER BY ps.preco")
    List<ProfissionalServicoEntity> findByPrecoRange(@Param("precoMin") Double precoMin, @Param("precoMax") Double precoMax);
}
