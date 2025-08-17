package com.barbearia.ph.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfissionalEntity extends PessoaAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O campo especialização é obrigatório")
    private Especializacao especializacao;


    @OneToMany(mappedBy = "profissionalEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProfissionalServicoEntity> profissionalServicos;

    @ManyToMany
    @JoinTable(
            name = "profissional_servico_direto",
            joinColumns = @JoinColumn(name = "profissional_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    @JsonIgnore
    private List<ServicoEntity> servicos;
}
