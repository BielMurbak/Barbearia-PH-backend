package com.barbearia.ph.controller;

import com.barbearia.ph.dto.ProfissionalRequestDTO;
import com.barbearia.ph.model.Especializacao;
import com.barbearia.ph.service.ProfissionalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfissionalControllerSaveExceptionTest {

    @Mock
    private ProfissionalService profissionalService;

    @InjectMocks
    private ProfissionalController profissionalController;

    @Test
    @DisplayName("Deve propagar exceção do service ao salvar profissional")
    void devePropagarExcecaoDoServiceAoSalvar() {
        ProfissionalRequestDTO dto = new ProfissionalRequestDTO();
        dto.setNome("João");
        dto.setSobrenome("Silva");
        dto.setCelular("45 99999-9999");
        dto.setSenha("senha123");
        dto.setEspecializacao(Especializacao.Corte);

        // Forçar exceção no service — quem converte isso em BAD_REQUEST é o
        // GlobalExceptionHandler, fora do escopo desse teste de unidade.
        when(profissionalService.save(any())).thenThrow(new RuntimeException("Erro no banco de dados"));

        assertThrows(RuntimeException.class, () -> profissionalController.save(dto));
    }
}
