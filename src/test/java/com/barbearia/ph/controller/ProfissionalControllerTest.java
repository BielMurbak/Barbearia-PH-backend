package com.barbearia.ph.controller;

import com.barbearia.ph.model.Especializacao;
import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.repository.ProfissionalRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.containsString;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfissionalControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProfissionalRepository profissionalRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void limparBanco() {
        profissionalRepository.deleteAll(); // garante banco limpo
    }

    @Test
    @DisplayName("INTEGRAÇÃO – Deve salvar profissional com sucesso e retornar 200 OK")
    void deveSalvarProfissionalComSucesso() throws Exception {
        // Arrange
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("Carlos");
        profissional.setSobrenome("Silva");
        profissional.setCelular("45 99999-8888");
        profissional.setEspecializacao(Especializacao.Corte);

        String json = objectMapper.writeValueAsString(profissional);

        // Act & Assert
        mockMvc.perform(post("/api/profissionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // Confirma que foi salvo no banco (opcional)
        assert profissionalRepository.findAll().stream()
                .anyMatch(p -> p.getNome().equals("Carlos"));
    }

    @Test
    @DisplayName("INTEGRAÇÃO – Deve retornar BadRequest para profissional inválido")
    void naoDeveSalvarProfissionalComSucesso() throws Exception {
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("");  // inválido
        profissional.setSobrenome("");
        profissional.setCelular("");
        profissional.setEspecializacao(Especializacao.Corte);

        String json = objectMapper.writeValueAsString(profissional);

        mockMvc.perform(post("/api/profissionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        assert profissionalRepository.findAll().stream()
                .noneMatch(p -> p.getNome().isEmpty());
    }


    @Test
    @DisplayName("INTEGRAÇÃO – Deve listar profissionais com sucesso")
    void deveRetornarProfissionaisComSucesso() throws Exception {
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("Joaquim");
        profissional.setSobrenome("Pedro");
        profissional.setCelular("45 99980-6733");
        profissional.setEspecializacao(Especializacao.Corte);

        profissionalRepository.save(profissional);

        mockMvc.perform(get("/api/profissionais")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Joaquim"))
                .andExpect(jsonPath("$[0].sobrenome").value("Pedro"));
    }

    @Test
    @DisplayName("INTEGRAÇÃO – Deve listar profissionais com Erro")
    void deveERRORetornarProfissionaisComSucesso() throws Exception {
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("");
        profissional.setSobrenome("");
        profissional.setCelular("");
        profissional.setEspecializacao(Especializacao.Corte);

        String json = objectMapper.writeValueAsString(profissional);

        mockMvc.perform(post("/api/profissionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

    }





}
