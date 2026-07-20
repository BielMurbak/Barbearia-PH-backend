package com.barbearia.ph.controller;

import com.barbearia.ph.dto.ClienteRequestDTO;
import com.barbearia.ph.dto.ClienteResponseDTO;
import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.service.ClienteService;
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
class ClienteControllerTest {

    @Mock
    ClienteService clienteService;

    @InjectMocks
    ClienteController clienteController;

    private ClienteRequestDTO dto() {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNome("Pedro");
        dto.setSobrenome("Flamengo");
        dto.setCelular("45 99980-7777");
        dto.setSenha("senha123");
        return dto;
    }

    private ClienteEntity entidade() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Pedro");
        cliente.setSobrenome("Flamengo");
        cliente.setCelular("45 99980-7777");
        return cliente;
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Salvar cliente com dados válidos deve retornar 201 CREATED e o cliente salvo")
    void deveSalvarClienteComSucesso() {
        ClienteEntity salvo = entidade();
        when(clienteService.save(any())).thenReturn(salvo);

        ResponseEntity<ClienteResponseDTO> response = clienteController.save(dto());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(salvo.getNome(), response.getBody().getNome());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Salvar cliente com dados inválidos deve propagar a exceção")
    void deveLancarExcecaoQuandoClienteInvalido() {
        when(clienteService.save(any())).thenThrow(new RuntimeException("Dados inválidos"));

        assertThrows(RuntimeException.class, () -> clienteController.save(dto()));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar todos os clientes deve retornar lista e status 200 OK")
    void deveRetornarListaClientes() {
        List<ClienteEntity> clientes = new ArrayList<>();
        clientes.add(entidade());

        when(clienteService.findAll()).thenReturn(clientes);

        ResponseEntity<List<ClienteResponseDTO>> response = clienteController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente por ID existente deve retornar cliente e status 200 OK")
    void deveRetornarClientePorId() {
        when(clienteService.findById(1L)).thenReturn(entidade());

        ResponseEntity<ClienteResponseDTO> response = clienteController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pedro", response.getBody().getNome());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente por ID inexistente deve propagar a exceção")
    void deveLancarExcecaoClientePorIdInexistente() {
        when(clienteService.findById(99L)).thenThrow(new RuntimeException("Cliente não encontrado"));

        assertThrows(RuntimeException.class, () -> clienteController.findById(99L));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Atualizar cliente existente deve retornar cliente atualizado e status 200 OK")
    void deveAtualizarClienteComSucesso() {
        ClienteEntity atualizado = entidade();
        when(clienteService.update(eq(1L), any())).thenReturn(atualizado);

        ResponseEntity<ClienteResponseDTO> response = clienteController.update(1L, dto());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(atualizado.getNome(), response.getBody().getNome());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Atualizar cliente inexistente deve propagar a exceção")
    void deveLancarExcecaoQuandoAtualizacaoFalha() {
        when(clienteService.update(eq(99L), any())).thenThrow(new RuntimeException("Cliente não encontrado"));

        assertThrows(RuntimeException.class, () -> clienteController.update(99L, dto()));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deletar cliente existente deve retornar status 200 OK")
    void deveRetornarOkQuandoDeletarCliente() {
        Long idCliente = 2L;

        doNothing().when(clienteService).delete(idCliente);

        ResponseEntity<String> response = clienteController.delete(idCliente);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deletar cliente inexistente deve propagar a exceção")
    void deveLancarExcecaoQuandoDeletarCliente() {
        Long idCliente = 2L;

        doThrow(new RuntimeException("Cliente não encontrado")).when(clienteService).delete(idCliente);

        assertThrows(RuntimeException.class, () -> clienteController.delete(idCliente));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente pelo nome deve retornar lista e status 200 OK")
    void deveRetornarClientePeloNome() {
        List<ClienteEntity> clienteLista = new ArrayList<>();
        clienteLista.add(entidade());

        when(clienteService.findByNome("Pedro")).thenReturn(clienteLista);

        ResponseEntity<List<ClienteResponseDTO>> response = clienteController.findByNome("Pedro");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente pelo nome inexistente deve propagar a exceção")
    void deveLancarExcecaoProcurarClientePeloNome() {
        when(clienteService.findByNome("Ninguem")).thenThrow(new RuntimeException("Cliente não encontrado pelo nome"));

        assertThrows(RuntimeException.class, () -> clienteController.findByNome("Ninguem"));
    }

    @Test
    @DisplayName("Deve retornar o cliente pelo número")
    void deveRetornarClientePeloNumero() {
        List<ClienteEntity> clienteLista = new ArrayList<>();
        clienteLista.add(entidade());

        when(clienteService.findByCelular("45 99980-7777")).thenReturn(clienteLista);

        ResponseEntity<List<ClienteResponseDTO>> response = clienteController.findByCelular("45 99980-7777");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente pelo número inexistente deve propagar a exceção")
    void deveLancarExcecaoProcurarClientePeloNumero() {
        when(clienteService.findByCelular("00 0000-0000")).thenThrow(new RuntimeException("Erro ao procurar cliente pelo número"));

        assertThrows(RuntimeException.class, () -> clienteController.findByCelular("00 0000-0000"));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente pelo nome completo deve retornar lista e status 200 OK")
    void deveRetornarClientePeloNomeCompleto() {
        List<ClienteEntity> clienteLista = new ArrayList<>();
        clienteLista.add(entidade());

        when(clienteService.findByNomeCompleto("Pedro Flamengo")).thenReturn(clienteLista);

        ResponseEntity<List<ClienteResponseDTO>> response = clienteController.findByNomeCompleto("Pedro Flamengo");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente pelo nome completo inexistente deve propagar a exceção")
    void deveLancarExcecaoClientePeloNomeCompleto() {
        when(clienteService.findByNomeCompleto("Ninguem Aqui")).thenThrow(new RuntimeException("Erro ao retornar cliente pelo nome completo"));

        assertThrows(RuntimeException.class, () -> clienteController.findByNomeCompleto("Ninguem Aqui"));
    }
}
