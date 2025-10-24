package com.barbearia.ph.controller;

import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.service.ProfissionalServicoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfissionalServicoController.class)
@ExtendWith(MockitoExtension.class)
class ProfissionalServicoControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfissionalServicoService profissionalServicoService;

    @Test
    @DisplayName("Deve retornar todos os profissionais-serviços com sucesso")
    void deveRetornarTodosProfissionaisServicosComSucesso() throws Exception {
        List<ProfissionalServicoEntity> lista = new ArrayList<>();
        ProfissionalServicoEntity profissionalServico = new ProfissionalServicoEntity();
        profissionalServico.setId(1L);
        profissionalServico.setPreco(50.0);
        lista.add(profissionalServico);

        when(profissionalServicoService.findAll()).thenReturn(lista);

        mockMvc.perform(get("/api/profissionais/servicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].preco", is(50.0)));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há profissionais-serviços")
    void deveRetornarListaVazia() throws Exception {
        when(profissionalServicoService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/profissionais/servicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}