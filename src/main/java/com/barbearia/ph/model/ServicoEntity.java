package com.barbearia.ph.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private int minDeDuracao;

    @OneToMany(mappedBy = "servicoEntity", cascade = CascadeType.ALL)
    private List<ProfissionalServicoEntity> profissionalServicoEntity;
}
