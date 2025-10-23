package com.barbearia.ph.controller;

import com.barbearia.ph.model.*;
import com.barbearia.ph.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ProfissionalServicoRepository profServRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    private ClienteEntity cliente;
    private ProfissionalEntity profissional;
    private ServicoEntity servico;
    private ProfissionalServicoEntity profServico;
    private AgendamentoEntity agendamento;


    @BeforeEach
    void setUp() {
        // limpar tudo
        agendamentoRepository.deleteAll();
        profServRepository.deleteAll();
        servicoRepository.deleteAll();
        profissionalRepository.deleteAll();
        clienteRepository.deleteAll();

        // criar cliente
        cliente = new ClienteEntity();
        cliente.setNome("Gabriel");
        cliente.setSobrenome("Murbak");
        cliente.setCelular("45 99935-5808");
        clienteRepository.save(cliente);

        // criar profissional
        profissional = new ProfissionalEntity();
        profissional.setNome("Carlos");
        profissional.setSobrenome("Silva");
        profissional.setCelular("45 98888-7777");
        profissional.setEspecializacao(Especializacao.Corte);
        profissionalRepository.save(profissional);

        // criar serviço
        servico = new ServicoEntity();
        servico.setDescricao("Corte simples");
        servico.setMinDeDuracao(30);
        servicoRepository.save(servico);

        // criar profissional-serviço
        profServico = new ProfissionalServicoEntity();
        profServico.setProfissionalEntity(profissional);
        profServico.setServicoEntity(servico);
        profServico.setPreco(50.0);
        profServRepository.save(profServico);

        // criar agendamento
        agendamento = new AgendamentoEntity();
        agendamento.setClienteEntity(cliente);
        agendamento.setProfissionalServicoEntity(profServico);
        agendamento.setData(LocalDate.now().plusDays(1));
        agendamento.setHorario("10:00");
        agendamento.setLocal("Barbearia PH");
    }

    // ---------- TESTE SAVE ----------
    @Test
    @DisplayName("Deve salvar agendamento com sucesso")
    void deveSalvarAgendamentoComSucesso() throws Exception {
        mockMvc.perform(post("/api/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendamento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.local", is("Barbearia PH")))
                .andExpect(jsonPath("$.clienteEntity.nome", is("Gabriel")));
    }

    @Test
    @DisplayName("Deve lançar erro ao salvar agendamento com conflito de horário")
    void deveLancarErroConflitoHorario() throws Exception {
        agendamentoRepository.save(agendamento); // salva um existente no mesmo horário

        AgendamentoEntity novoAgendamento = new AgendamentoEntity();
        novoAgendamento.setClienteEntity(cliente);
        novoAgendamento.setProfissionalServicoEntity(profServico);
        novoAgendamento.setData(agendamento.getData());
        novoAgendamento.setHorario("10:15"); // conflito parcial
        novoAgendamento.setLocal("Barbearia PH");

        mockMvc.perform(post("/api/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAgendamento)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Horário indisponível")));
    }

    // ---------- TESTE FIND ALL ----------
    @Test
    @DisplayName("Deve retornar todos os agendamentos")
    void deveRetornarTodosAgendamentos() throws Exception {
        agendamentoRepository.save(agendamento);

        mockMvc.perform(get("/api/agendamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].local", is("Barbearia PH")));
    }

    // ---------- TESTE FIND BY ID ----------
    @Test
    @DisplayName("Deve retornar agendamento por ID")
    void deveRetornarAgendamentoPorId() throws Exception {
        AgendamentoEntity salvo = agendamentoRepository.save(agendamento);

        mockMvc.perform(get("/api/agendamentos/{id}", salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.local", is("Barbearia PH")));
    }

    // ---------- TESTE DELETE ----------
    @Test
    @DisplayName("Deve deletar agendamento com sucesso")
    void deveDeletarAgendamentoComSucesso() throws Exception {
        AgendamentoEntity salvo = agendamentoRepository.save(agendamento);

        mockMvc.perform(delete("/api/agendamentos/{id}", salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("removido com sucesso")));
    }

    // ---------- TESTE UPDATE ----------
    @Test
    @DisplayName("Deve atualizar agendamento com sucesso")
    void deveAtualizarAgendamentoComSucesso() throws Exception {
        AgendamentoEntity salvo = agendamentoRepository.save(agendamento);

        salvo.setLocal("PH Premium");
        salvo.setHorario("15:00");

        mockMvc.perform(put("/api/agendamentos/{id}", salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salvo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.local", is("PH Premium")))
                .andExpect(jsonPath("$.horario", is("15:00")));
    }
}