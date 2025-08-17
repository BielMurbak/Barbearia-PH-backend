
package com.barbearia.ph.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgendamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O campo data é obrigatório")
    private LocalDate data;
    
    @NotBlank(message = "O campo local é obrigatório")
    private String local;
    
    @NotBlank(message = "O campo horário é obrigatório")
    private String horario;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "O cliente é obrigatório")
    private ClienteEntity clienteEntity;

    @ManyToOne
    @JoinColumn(name = "profissionalServico_id", nullable = false)
    @NotNull(message = "O profissional e serviço são obrigatórios")
    private ProfissionalServicoEntity profissionalServicoEntity;
}
