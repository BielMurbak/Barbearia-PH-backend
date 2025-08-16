package com.barbearia.ph.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class ClienteEntity extends PessoaAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "clienteEntity", cascade = CascadeType.ALL)
     @JsonIgnore
    private List<AgendamentoEntity> agendamentos;
}
