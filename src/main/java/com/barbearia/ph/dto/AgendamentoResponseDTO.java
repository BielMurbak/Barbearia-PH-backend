package com.barbearia.ph.dto;

import com.barbearia.ph.model.AgendamentoEntity.StatusAgendamento;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AgendamentoResponseDTO {

    private Long id;
    private LocalDate data;
    private String local;
    private String horario;
    private StatusAgendamento status;
    private String observacoes;
    private Double preco;
    private ClienteResponseDTO cliente;
    private ProfissionalServicoResponseDTO profissionalServico;
}
