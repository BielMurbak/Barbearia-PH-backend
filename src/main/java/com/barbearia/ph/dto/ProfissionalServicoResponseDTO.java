package com.barbearia.ph.dto;

import lombok.Data;

@Data
public class ProfissionalServicoResponseDTO {

    private Long id;
    private Double preco;
    private ProfissionalResponseDTO profissional;
    private ServicoResponseDTO servico;
}
