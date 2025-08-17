package com.barbearia.ph.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O campo descrição é obrigatório")
    private String descricao;
    
    @NotNull(message = "O campo duração é obrigatório")
    private int minDeDuracao;

    @OneToMany(mappedBy = "servicoEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProfissionalServicoEntity> profissionalServicoEntity;


    @ManyToMany(mappedBy = "servicos")
    @JsonIgnore
    private List<ProfissionalEntity> profissionais;
}
