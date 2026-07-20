package com.barbearia.ph.dto;

import lombok.Data;

@Data
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String sobrenome;
    private String celular;
    private String nomeCompleto;
}
