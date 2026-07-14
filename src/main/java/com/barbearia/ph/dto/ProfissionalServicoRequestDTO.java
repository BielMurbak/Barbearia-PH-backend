package com.barbearia.ph.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProfissionalServicoRequestDTO {

    @NotNull(message = "O profissional é obrigatório")
    private Long profissionalId;

    @NotNull(message = "O serviço é obrigatório")
    private Long servicoId;

    @NotNull(message = "O campo preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    private Double preco;
}
