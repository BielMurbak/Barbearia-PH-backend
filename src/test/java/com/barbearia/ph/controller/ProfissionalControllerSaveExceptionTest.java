package com.barbearia.ph.controller;

import com.barbearia.ph.model.Especializacao;
import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.service.ProfissionalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfissionalControllerSaveExceptionTest {

    @Mock
    private ProfissionalService profissionalService;

    @InjectMocks
    private ProfissionalController profissionalController;

    @Test
    @DisplayName("Deve capturar exceção no bloco catch do save")
    void deveCapturaExcecaoNoBlocoCatchDoSave() {
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("João");
        profissional.setSobrenome("Silva");
        profissional.setCelular("45 99999-9999");
        profissional.setEspecializacao(Especializacao.Corte);

        // Forçar exceção no service para ativar o catch do controller
        when(profissionalService.save(any())).thenThrow(new RuntimeException("Erro no banco de dados"));

        ResponseEntity<?> response = profissionalController.save(profissional);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao salvar profissional: Erro no banco de dados", response.getBody());
    }
}