package com.barbearia.ph.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass

public abstract class PessoaAbstract {

    @NotBlank(message = "O campo nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "O campo sobrenome é obrigatório")
    private String sobrenome;
    
    @NotBlank(message = "O campo celular é obrigatório")
    private String celular;


}
