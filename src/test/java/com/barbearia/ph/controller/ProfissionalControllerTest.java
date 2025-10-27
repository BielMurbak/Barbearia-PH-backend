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


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("INTEGRAÇÃO – Deve retornar erro ao salvar profissional com dados inválidos")
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
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("INTEGRAÇÃO – Deve listar profissional com id informado")
    void deveRetornarProfissionaisComoidSucesso() throws Exception {
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("RAFAEL");
        profissional.setSobrenome("carlos");
        profissional.setCelular("45 999806733");
        profissional.setEspecializacao(Especializacao.Corte);


        profissional = profissionalRepository.save(profissional);


        mockMvc.perform(get("/api/profissionais/{id}",profissional.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    @DisplayName("INTEGRAÇÃO – Deve atualizar um Profissional já cadastrado com sucesso")
    void deveMudarAlgoEmProfissionalRetorneSucesso() throws Exception {

        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("Rafael");
        profissional.setSobrenome("Carlos");
        profissional.setCelular("45 999806733");
        profissional.setEspecializacao(Especializacao.Corte);

        profissional = profissionalRepository.save(profissional);


        ProfissionalEntity profissionalAtualizado = new ProfissionalEntity();
        profissionalAtualizado.setId(profissional.getId());
        profissionalAtualizado.setNome("Rafael Atualizado");
        profissionalAtualizado.setSobrenome("Silva");
        profissionalAtualizado.setCelular("45 988888888");
        profissionalAtualizado.setEspecializacao(Especializacao.Barba);

        String json = objectMapper.writeValueAsString(profissionalAtualizado);


        mockMvc.perform(put("/api/profissionais/{id}", profissional.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Rafael Atualizado"))
                .andExpect(jsonPath("$.sobrenome").value("Silva"))
                .andExpect(jsonPath("$.celular").value("45 988888888"));
    }

    @Test
    @DisplayName("INTEGRAÇÃO – Deve deletar profissional com ID informado")
    void deveDeletarProfissionalComIdSucesso() throws Exception {

        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("Rafael");
        profissional.setSobrenome("Carlos");
        profissional.setCelular("45 999806733");
        profissional.setEspecializacao(Especializacao.Corte);

        profissional = profissionalRepository.save(profissional);


        mockMvc.perform(delete("/api/profissionais/{id}", profissional.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        assert profissionalRepository.findById(profissional.getId()).isEmpty();
    }


    @Test
    @DisplayName("INTEGRAÇÃO – Deve Retornar profissional com nome")
    void deveRetornarProfissionalComNome() throws Exception {
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("RAFAEL");
        profissional.setSobrenome("carlos");
        profissional.setCelular("45 999806733");
        profissional.setEspecializacao(Especializacao.Corte);


        profissional = profissionalRepository.save(profissional);


        mockMvc.perform(get("/api/profissionais/nome")
                        .param("nome", profissional.getNome())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("INTEGRAÇÃO – Deve Retornar profissional com especializacao")
    void deveRetornarProfissionalComEspecializacao() throws Exception {
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("RAFAEL");
        profissional.setSobrenome("carlos");
        profissional.setCelular("45 999806733");
        profissional.setEspecializacao(Especializacao.Corte);

        profissional = profissionalRepository.save(profissional);

        mockMvc.perform(get("/api/profissionais/especializacao")
                        .param("especializacao", profissional.getEspecializacao().name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].especializacao").value("Corte"));
    }

    // ---------- TESTES DE EXCEÇÃO ----------
    @Test
    @DisplayName("Deve retornar 400 ao buscar profissional com ID inexistente")
    void deveRetornar400BuscarProfissionalInexistente() throws Exception {
        mockMvc.perform(get("/api/profissionais/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Erro ao buscar profissional")));
    }

    @Test
    @DisplayName("Deve retornar 400 ao atualizar profissional inexistente")
    void deveRetornar400AtualizarProfissionalInexistente() throws Exception {
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setNome("Teste");
        profissional.setSobrenome("Silva");
        profissional.setCelular("45 99999-9999");
        profissional.setEspecializacao(Especializacao.Corte);

        String json = objectMapper.writeValueAsString(profissional);

        mockMvc.perform(put("/api/profissionais/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Erro ao atualizar profissional")));
    }

    @Test
    @DisplayName("Deve retornar 400 ao deletar profissional inexistente")
    void deveRetornar400DeletarProfissionalInexistente() throws Exception {
        mockMvc.perform(delete("/api/profissionais/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Erro ao deletar profissional")));
    }

    @Test
    @DisplayName("Deve retornar 400 com especialização inválida")
    void deveRetornar400EspecializacaoInvalida() throws Exception {
        mockMvc.perform(get("/api/profissionais/especializacao")
                        .param("especializacao", "INVALIDA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Erro ao buscar profissionais por especialização")));
    }

    @Test
    @DisplayName("Deve retornar 400 ao salvar profissional com JSON inválido")
    void deveRetornar400SalvarProfissionalJsonInvalido() throws Exception {
        // JSON malformado para forçar exceção no parsing
        String jsonInvalido = "{\"nome\": \"João\", \"sobrenome\": \"Silva\", \"celular\": \"45 99999-9999\", \"especializacao\": \"INVALIDA\"}";

        mockMvc.perform(post("/api/profissionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Erro de negócio")));
    }

}
