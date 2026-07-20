package com.barbearia.ph.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClienteRequestDTO {

    @NotBlank(message = "O campo nome é obrigatório")
    private String nome;

    @NotBlank(message = "O campo sobrenome é obrigatório")
    private String sobrenome;

    @NotBlank(message = "O campo celular é obrigatório")
    private String celular;

    @NotBlank(message = "O campo senha é obrigatório")
    private String senha;
}
