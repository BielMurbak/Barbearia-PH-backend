package com.barbearia.ph.controller;

import com.barbearia.ph.service.ProfissionalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfissionalController.class)
class ProfissionalControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfissionalService profissionalService;

    @Test
    @DisplayName("Deve retornar 400 quando service lança exceção no findByNome")
    void deveRetornar400QuandoServiceLancaExcecaoFindByNome() throws Exception {
        when(profissionalService.findByNome("teste"))
                .thenThrow(new RuntimeException("Erro simulado"));

        mockMvc.perform(get("/api/profissionais/nome")
                        .param("nome", "teste")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Erro ao buscar profissionais por nome")));
    }

    @Test
    @DisplayName("Deve retornar 400 quando service lança exceção no findAll")
    void deveRetornar400QuandoServiceLancaExcecaoFindAll() throws Exception {
        when(profissionalService.findAll())
                .thenThrow(new RuntimeException("Erro simulado"));

        mockMvc.perform(get("/api/profissionais")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Erro ao listar profissionais")));
    }
}