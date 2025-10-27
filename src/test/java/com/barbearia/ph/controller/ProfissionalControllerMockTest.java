package com.barbearia.ph.controller;

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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfissionalControllerMockTest {

    @Mock
    private ProfissionalService profissionalService;

    @InjectMocks
    private ProfissionalController profissionalController;

    @Test
    @DisplayName("Deve retornar 400 quando service lança exceção no findByNome")
    void deveRetornar400QuandoServiceLancaExcecaoFindByNome() {
        when(profissionalService.findByNome("teste"))
                .thenThrow(new RuntimeException("Erro simulado"));

        ResponseEntity<?> response = profissionalController.findByNome("teste");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao buscar profissionais por nome: Erro simulado", response.getBody());
    }

    @Test
    @DisplayName("Deve retornar 400 quando service lança exceção no findAll")
    void deveRetornar400QuandoServiceLancaExcecaoFindAll() {
        when(profissionalService.findAll())
                .thenThrow(new RuntimeException("Erro simulado"));

        ResponseEntity<?> response = profissionalController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao listar profissionais: Erro simulado", response.getBody());
    }
}