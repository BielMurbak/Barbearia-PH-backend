package com.barbearia.ph.controller;

import com.barbearia.ph.dto.ServicoRequestDTO;
import com.barbearia.ph.dto.ServicoResponseDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoControllerTest {

    @Mock
    ServicoService servicoService;

    @InjectMocks
    ServicoController servicoController;

    private ServicoRequestDTO dto(String descricao, int duracao) {
        ServicoRequestDTO dto = new ServicoRequestDTO();
        dto.setDescricao(descricao);
        dto.setMinDeDuracao(duracao);
        return dto;
    }

    private ServicoEntity entidade(Long id, String descricao, int duracao) {
        ServicoEntity servico = new ServicoEntity();
        servico.setId(id);
        servico.setDescricao(descricao);
        servico.setMinDeDuracao(duracao);
        return servico;
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Salvar serviço com dados válidos deve retornar 201 CREATED")
    void deveSalvarServicoComSucesso() {
        ServicoEntity servico = entidade(1L, "Corte de Cabelo", 30);
        when(servicoService.save(any())).thenReturn(servico);

        ResponseEntity<ServicoResponseDTO> response = servicoController.save(dto("Corte de Cabelo", 30));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Corte de Cabelo", response.getBody().getDescricao());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Salvar serviço com dados inválidos deve propagar a exceção")
    void deveLancarExcecaoQuandoServicoInvalido() {
        when(servicoService.save(any())).thenThrow(new RuntimeException("Dados inválidos"));

        assertThrows(RuntimeException.class, () -> servicoController.save(dto("", 0)));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar todos os serviços deve retornar lista e status 200 OK")
    void deveRetornarListaServicos() {
        List<ServicoEntity> servicos = new ArrayList<>();
        servicos.add(entidade(1L, "Corte", 30));

        when(servicoService.findAll()).thenReturn(servicos);

        ResponseEntity<List<ServicoResponseDTO>> response = servicoController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar serviço por ID existente deve retornar serviço e status 200 OK")
    void deveRetornarServicoPorId() {
        when(servicoService.findById(1L)).thenReturn(entidade(1L, "Corte", 30));

        ResponseEntity<ServicoResponseDTO> response = servicoController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar serviço por ID inexistente deve propagar a exceção")
    void deveLancarExcecaoQuandoServicoNaoEncontrado() {
        when(servicoService.findById(999L)).thenThrow(new RuntimeException("Serviço não encontrado"));

        assertThrows(RuntimeException.class, () -> servicoController.findById(999L));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Atualizar serviço existente deve retornar serviço atualizado e status 200 OK")
    void deveAtualizarServicoComSucesso() {
        when(servicoService.update(eq(1L), any())).thenReturn(entidade(1L, "Corte Atualizado", 30));

        ResponseEntity<ServicoResponseDTO> response = servicoController.update(1L, dto("Corte Atualizado", 30));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Atualizar serviço inexistente deve propagar a exceção")
    void deveLancarExcecaoQuandoAtualizarServicoInexistente() {
        when(servicoService.update(eq(999L), any())).thenThrow(new RuntimeException("Serviço não encontrado para atualização"));

        assertThrows(RuntimeException.class, () -> servicoController.update(999L, dto("Serviço Teste", 30)));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deletar serviço existente deve retornar status 204 NO_CONTENT")
    void deveDeletarServicoComSucesso() {
        Long idServico = 1L;

        doNothing().when(servicoService).delete(idServico);

        ResponseEntity<Void> response = servicoController.delete(idServico);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deletar serviço inexistente deve propagar a exceção")
    void deveLancarExcecaoQuandoDeletarServicoInexistente() {
        Long idInexistente = 999L;

        doThrow(new RuntimeException("Serviço não encontrado para exclusão"))
                .when(servicoService).delete(idInexistente);

        assertThrows(RuntimeException.class, () -> servicoController.delete(idInexistente));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar serviços por descrição deve retornar lista e status 200 OK")
    void deveRetornarServicosPorDescricao() {
        String descricao = "Corte";
        List<ServicoEntity> servicos = new ArrayList<>();
        servicos.add(entidade(1L, "Corte de Cabelo", 30));

        when(servicoService.findByDescricao(descricao)).thenReturn(servicos);

        ResponseEntity<List<ServicoResponseDTO>> response = servicoController.findByDescricao(descricao);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar serviços por duração máxima deve retornar lista e status 200 OK")
    void deveRetornarServicosPorDuracaoMaxima() {
        int duracaoMaxima = 45;
        List<ServicoEntity> servicos = new ArrayList<>();
        servicos.add(entidade(1L, "Corte Rápido", 30));

        when(servicoService.findByDuracaoMaxima(duracaoMaxima)).thenReturn(servicos);

        ResponseEntity<List<ServicoResponseDTO>> response = servicoController.findByDuracaoMaxima(duracaoMaxima);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar serviços por faixa de duração deve retornar lista e status 200 OK")
    void deveRetornarServicosPorDuracaoRange() {
        int duracaoMin = 20;
        int duracaoMax = 60;
        List<ServicoEntity> servicos = new ArrayList<>();
        servicos.add(entidade(1L, "Corte Padrão", 45));

        when(servicoService.findByDuracaoRange(duracaoMin, duracaoMax)).thenReturn(servicos);

        ResponseEntity<List<ServicoResponseDTO>> response = servicoController.findByDuracaoRange(duracaoMin, duracaoMax);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Listar serviços com erro deve propagar a exceção")
    void deveLancarExcecaoQuandoErroListarServicos() {
        when(servicoService.findAll())
                .thenThrow(new RuntimeException("Erro ao acessar banco de dados"));

        assertThrows(RuntimeException.class, () -> servicoController.findAll());
    }
}
