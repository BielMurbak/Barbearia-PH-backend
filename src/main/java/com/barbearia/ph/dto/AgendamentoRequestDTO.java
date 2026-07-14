package com.barbearia.ph.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AgendamentoRequestDTO {

    @NotNull(message = "O campo data é obrigatório")
    private LocalDate data;

    @NotBlank(message = "O campo local é obrigatório")
    private String local;

    @NotBlank(message = "O campo horário é obrigatório")
    private String horario;

    @NotNull(message = "O cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O profissional e serviço são obrigatórios")
    private Long profissionalServicoId;

    private String observacoes;
    private Double preco;
}
