package com.barbearia.ph.dto;

import com.barbearia.ph.model.Especializacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProfissionalRequestDTO {

    @NotBlank(message = "O campo nome é obrigatório")
    private String nome;

    @NotBlank(message = "O campo sobrenome é obrigatório")
    private String sobrenome;

    @NotBlank(message = "O campo celular é obrigatório")
    private String celular;

    @NotBlank(message = "O campo senha é obrigatório")
    private String senha;

    @NotNull(message = "O campo especialização é obrigatório")
    private Especializacao especializacao;
}
