package com.barbearia.ph.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ServicoRequestDTO {

    @NotBlank(message = "O campo descrição é obrigatório")
    private String descricao;

    @NotNull(message = "O campo duração é obrigatório")
    @Positive(message = "A duração deve ser maior que zero")
    private Integer minDeDuracao;
}
