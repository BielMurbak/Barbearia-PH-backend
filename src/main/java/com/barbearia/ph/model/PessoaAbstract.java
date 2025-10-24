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

    public String getNomeCompleto() {
        if (nome == null || sobrenome == null) {
            return "";
        }
        return nome.trim() + " " + sobrenome.trim();
    }

    public boolean isValidCelular() {
        if (celular == null || celular.trim().isEmpty()) {
            return false;
        }
        return celular.replaceAll("[^0-9]", "").length() >= 10;
    }
}
