package com.barbearia.ph.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfissionalServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "O campo preço é obrigatório")
    private Double preco;

    // Soft delete: "excluir" um serviço do catálogo de um barbeiro só o tira das
    // listagens, nunca apaga a linha — agendamentos antigos referenciam essa
    // linha por chave estrangeira e não podem ficar órfãos.
    // columnDefinition com DEFAULT 1 é necessário pra migração (ddl-auto=update)
    // não zerar os vínculos já existentes ao criar essa coluna.
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    @NotNull(message = "O profissional é obrigatório")
    private ProfissionalEntity profissionalEntity;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    @NotNull(message = "O serviço é obrigatório")
    private ServicoEntity servicoEntity;

}
