package com.barbearia.ph.model;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass

public abstract class PessoaAbstract {

    private String nome;
    private String sobrenome;
    private String celular;


}
