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

    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    @NotNull(message = "O profissional é obrigatório")
    private ProfissionalEntity profissionalEntity;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    @NotNull(message = "O serviço é obrigatório")
    private ServicoEntity servicoEntity;

}
