package com.barbearia.ph.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<ProfissionalServicoEntity> profissionalServicoEntity;
}
