package com.barbearia.ph.controller;

import com.barbearia.ph.model.Especializacao;
import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.repository.ProfissionalRepository;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import com.barbearia.ph.repository.ServicoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfissionalServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfissionalServicoRepository profissionalServicoRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfissionalEntity profissional;
    private ServicoEntity servico;

    @BeforeEach
    void setUp() {
        profissionalServicoRepository.deleteAll();
        profissionalRepository.deleteAll();
        servicoRepository.deleteAll();

        // Criar profissional
        profissional = new ProfissionalEntity();
        profissional.setNome("João");
        profissional.setSobrenome("Silva");
        profissional.setCelular("45 99999-9999");
        profissional.setEspecializacao(Especializacao.Corte);
        profissional = profissionalRepository.save(profissional);

        // Criar serviço
        servico = new ServicoEntity();
        servico.setDescricao("Corte de Cabelo");
        servico.setMinDeDuracao(30);
        servico = servicoRepository.save(servico);
    }

    @Test
    @DisplayName("Deve salvar profissional-serviço com sucesso")
    void deveSalvarProfissionalServicoComSucesso() throws Exception {
        ProfissionalServicoEntity profissionalServico = new ProfissionalServicoEntity();
        profissionalServico.setProfissionalEntity(profissional);
        profissionalServico.setServicoEntity(servico);
        profissionalServico.setPreco(50.0);

        mockMvc.perform(post("/api/profissionais/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profissionalServico)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.preco", is(50.0)))
                .andExpect(jsonPath("$.profissionalEntity.nome", is("João")))
                .andExpect(jsonPath("$.servicoEntity.descricao", is("Corte de Cabelo")));
    }

    @Test
    @DisplayName("Deve retornar erro ao salvar profissional-serviço com dados inválidos")
    void deveRetornarErroAoSalvarComDadosInvalidos() throws Exception {
        ProfissionalServicoEntity profissionalServico = new ProfissionalServicoEntity();
        // Não definir profissional nem serviço para forçar erro
        profissionalServico.setPreco(50.0);

        mockMvc.perform(post("/api/profissionais/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profissionalServico)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve atualizar profissional-serviço com sucesso")
    void deveAtualizarProfissionalServicoComSucesso() throws Exception {
        // Criar e salvar profissional-serviço
        ProfissionalServicoEntity profissionalServico = new ProfissionalServicoEntity();
        profissionalServico.setProfissionalEntity(profissional);
        profissionalServico.setServicoEntity(servico);
        profissionalServico.setPreco(50.0);
        ProfissionalServicoEntity salvo = profissionalServicoRepository.save(profissionalServico);

        // Atualizar preço
        salvo.setPreco(75.0);

        mockMvc.perform(put("/api/profissionais/servicos/{id}", salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salvo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preco", is(75.0)))
                .andExpect(jsonPath("$.profissionalEntity.nome", is("João")));
    }

    @Test
    @DisplayName("Deve deletar profissional-serviço com sucesso")
    void deveDeletarProfissionalServicoComSucesso() throws Exception {
        // Criar e salvar profissional-serviço
        ProfissionalServicoEntity profissionalServico = new ProfissionalServicoEntity();
        profissionalServico.setProfissionalEntity(profissional);
        profissionalServico.setServicoEntity(servico);
        profissionalServico.setPreco(50.0);
        ProfissionalServicoEntity salvo = profissionalServicoRepository.save(profissionalServico);

        mockMvc.perform(delete("/api/profissionais/servicos/{id}", salvo.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar todos os profissionais-serviços")
    void deveRetornarErroAoBuscarTodos() throws Exception {
        mockMvc.perform(get("/api/profissionais/servicos"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar profissional-serviço por ID")
    void deveRetornarProfissionalServicoPorId() throws Exception {
        // Criar e salvar profissional-serviço
        ProfissionalServicoEntity profissionalServico = new ProfissionalServicoEntity();
        profissionalServico.setProfissionalEntity(profissional);
        profissionalServico.setServicoEntity(servico);
        profissionalServico.setPreco(50.0);
        ProfissionalServicoEntity salvo = profissionalServicoRepository.save(profissionalServico);

        mockMvc.perform(get("/api/profissionais/servicos/{id}", salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preco", is(50.0)))
                .andExpect(jsonPath("$.profissionalEntity.nome", is("João")));
    }

}
