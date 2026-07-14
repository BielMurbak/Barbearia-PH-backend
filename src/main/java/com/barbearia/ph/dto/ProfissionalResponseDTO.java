package com.barbearia.ph.dto;

import com.barbearia.ph.model.Especializacao;
import lombok.Data;

@Data
public class ProfissionalResponseDTO {

    private Long id;
    private String nome;
    private String sobrenome;
    private String celular;
    private String nomeCompleto;
    private Especializacao especializacao;
}
