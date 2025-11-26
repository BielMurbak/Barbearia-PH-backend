package com.barbearia.ph.repository;

import com.barbearia.ph.model.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    List<ClienteEntity> findByNomeIgnoreCaseContaining(String nome);
    
    List<ClienteEntity> findByCelularContaining(String celular);

    Optional<ClienteEntity> findByCelular(String celular);
    
    @Query("SELECT c FROM ClienteEntity c WHERE CONCAT(c.nome, ' ', c.sobrenome) LIKE %:nomeCompleto%")
    List<ClienteEntity> findByNomeCompletoContaining(@Param("nomeCompleto") String nomeCompleto);
}
