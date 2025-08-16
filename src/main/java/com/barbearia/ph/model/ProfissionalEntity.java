package com.barbearia.ph.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class ProfissionalEntity extends PessoaAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Especializacao especializacao;

    @OneToMany(mappedBy = "profissionalEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProfissionalServicoEntity> profissionalServicos;
}
