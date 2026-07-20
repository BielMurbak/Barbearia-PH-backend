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

    // Soft delete: "excluir" um serviço só o tira do catálogo (some das listagens),
    // nunca apaga a linha de verdade — agendamentos antigos referenciam essa linha
    // por chave estrangeira e não podem ficar órfãos.
    // columnDefinition com DEFAULT 1 é necessário pra migração (ddl-auto=update)
    // não zerar os serviços já existentes ao criar essa coluna.
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean ativo = true;

    @OneToMany(mappedBy = "servicoEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProfissionalServicoEntity> profissionalServicoEntity;


}
