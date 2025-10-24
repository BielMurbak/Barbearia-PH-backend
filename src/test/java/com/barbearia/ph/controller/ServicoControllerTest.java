package com.barbearia.ph.controller;

import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.service.ServicoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoControllerTest {

    @Mock
    ServicoService servicoService;

    @InjectMocks
    ServicoController servicoController;

    @Test
    @DisplayName("TESTE DE UNIDADE – Salvar serviço com dados válidos deve retornar 201 CREATED")
    void deveSalvarServicoComSucesso() {
        ServicoEntity servico = new ServicoEntity();
        servico.setDescricao("Corte de Cabelo");
        servico.setMinDeDuracao(30);

        when(servicoService.save(servico)).thenReturn(servico);

        ResponseEntity<?> response = servicoController.save(servico);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(servico, response.getBody());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Salvar serviço com dados inválidos deve retornar BAD_REQUEST")
    void deveRetornarBadRequestQuandoServicoInvalido() {
        ServicoEntity servicoInvalido = new ServicoEntity();

        when(servicoService.save(servicoInvalido))
                .thenThrow(new RuntimeException("Dados inválidos"));

        ResponseEntity<?> response = servicoController.save(servicoInvalido);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar todos os serviços deve retornar lista e status 200 OK")
    void deveRetornarListaServicos() {
        ServicoEntity servico = new ServicoEntity();
        servico.setId(1L);
        servico.setDescricao("Corte");
        servico.setMinDeDuracao(30);

        List<ServicoEntity> servicos = new ArrayList<>();
        servicos.add(servico);

        when(servicoService.findAll()).thenReturn(servicos);

        ResponseEntity<?> response = servicoController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(servicos, response.getBody());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar serviço por ID existente deve retornar serviço e status 200 OK")
    void deveRetornarServicoPorId() {
        ServicoEntity servico = new ServicoEntity();
        servico.setId(1L);
        servico.setDescricao("Corte");

        when(servicoService.findById(1L)).thenReturn(servico);

        ResponseEntity<?> response = servicoController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Atualizar serviço existente deve retornar serviço atualizado e status 200 OK")
    void deveAtualizarServicoComSucesso() {
        ServicoEntity servico = new ServicoEntity();
        servico.setId(1L);
        servico.setDescricao("Corte Atualizado");

        when(servicoService.update(1L, servico)).thenReturn(servico);

        ResponseEntity<?> response = servicoController.update(1L, servico);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deletar serviço existente deve retornar status 204 NO_CONTENT")
    void deveDeletarServicoComSucesso() {
        Long idServico = 1L;

        doNothing().when(servicoService).delete(idServico);

        ResponseEntity<?> response = servicoController.delete(idServico);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar serviços por descrição deve retornar lista e status 200 OK")
    void deveRetornarServicosPorDescricao() {
        String descricao = "Corte";
        ServicoEntity servico = new ServicoEntity();
        servico.setId(1L);
        servico.setDescricao("Corte de Cabelo");
        servico.setMinDeDuracao(30);

        List<ServicoEntity> servicos = new ArrayList<>();
        servicos.add(servico);

        when(servicoService.findByDescricao(descricao)).thenReturn(servicos);

        ResponseEntity<List<ServicoEntity>> response = servicoController.findByDescricao(descricao);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(servicos, response.getBody());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar serviços por duração máxima deve retornar lista e status 200 OK")
    void deveRetornarServicosPorDuracaoMaxima() {
        int duracaoMaxima = 45;
        ServicoEntity servico = new ServicoEntity();
        servico.setId(1L);
        servico.setDescricao("Corte Rápido");
        servico.setMinDeDuracao(30);

        List<ServicoEntity> servicos = new ArrayList<>();
        servicos.add(servico);

        when(servicoService.findByDuracaoMaxima(duracaoMaxima)).thenReturn(servicos);

        ResponseEntity<List<ServicoEntity>> response = servicoController.findByDuracaoMaxima(duracaoMaxima);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(servicos, response.getBody());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar serviços por faixa de duração deve retornar lista e status 200 OK")
    void deveRetornarServicosPorDuracaoRange() {
        int duracaoMin = 20;
        int duracaoMax = 60;
        ServicoEntity servico = new ServicoEntity();
        servico.setId(1L);
        servico.setDescricao("Corte Padrão");
        servico.setMinDeDuracao(45);

        List<ServicoEntity> servicos = new ArrayList<>();
        servicos.add(servico);

        when(servicoService.findByDuracaoRange(duracaoMin, duracaoMax)).thenReturn(servicos);

        ResponseEntity<List<ServicoEntity>> response = servicoController.findByDuracaoRange(duracaoMin, duracaoMax);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(servicos, response.getBody());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar serviço por ID inexistente deve retornar BAD_REQUEST")
    void deveRetornarBadRequestQuandoServicoNaoEncontrado() {
        Long idInexistente = 999L;

        when(servicoService.findById(idInexistente))
                .thenThrow(new RuntimeException("Serviço não encontrado"));

        ResponseEntity<?> response = servicoController.findById(idInexistente);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Atualizar serviço inexistente deve retornar BAD_REQUEST")
    void deveRetornarBadRequestQuandoAtualizarServicoInexistente() {
        Long idInexistente = 999L;
        ServicoEntity servico = new ServicoEntity();
        servico.setDescricao("Serviço Teste");
        servico.setMinDeDuracao(30);

        when(servicoService.update(idInexistente, servico))
                .thenThrow(new RuntimeException("Serviço não encontrado para atualização"));

        ResponseEntity<?> response = servicoController.update(idInexistente, servico);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deletar serviço inexistente deve retornar BAD_REQUEST")
    void deveRetornarBadRequestQuandoDeletarServicoInexistente() {
        Long idInexistente = 999L;

        doThrow(new RuntimeException("Serviço não encontrado para exclusão"))
                .when(servicoService).delete(idInexistente);

        ResponseEntity<?> response = servicoController.delete(idInexistente);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Listar serviços com erro deve retornar BAD_REQUEST")
    void deveRetornarBadRequestQuandoErroListarServicos() {
        when(servicoService.findAll())
                .thenThrow(new RuntimeException("Erro ao acessar banco de dados"));

        ResponseEntity<?> response = servicoController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}