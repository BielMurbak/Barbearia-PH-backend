package com.barbearia.ph.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class ProfissionalServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double preco;

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private ProfissionalEntity profissionalEntity;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private ServicoEntity servicoEntity;

}
