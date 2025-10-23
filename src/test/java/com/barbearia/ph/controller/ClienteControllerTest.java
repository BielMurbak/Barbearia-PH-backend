package com.barbearia.ph.controller;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    ClienteService clienteService;

    @InjectMocks
    ClienteController clienteController;

    @Test
    @DisplayName("TESTE DE UNIDADE – Salvar cliente com dados válidos deve retornar 200 OK e o cliente salvo")
    void deveSalvarClienteComSucesso() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setNome("Jorginho");
        cliente.setSobrenome("Scarabelot");
        cliente.setCelular("+55 45 99980-6733");

        when(clienteService.save(cliente)).thenReturn(cliente);

        ResponseEntity<?> response = clienteController.save(cliente);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
    }


    @Test
    @DisplayName("TESTE DE UNIDADE – Salvar cliente com dados inválidos deve retornar BAD_REQUEST com mensagem de erro")
    void deveRetornarBadRequestQuandoClienteInvalido () {
        ClienteEntity clienteInvalido = new ClienteEntity();
        clienteInvalido.setNome("");
        clienteInvalido.setSobrenome("");
        clienteInvalido.setCelular("");

        when(clienteService.save(clienteInvalido))
                .thenThrow(new RuntimeException("Dados inválidos"));

        ResponseEntity<?> response = clienteController.save(clienteInvalido);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar todos os clientes deve retornar lista e status 200 OK")
    void deveRetornarListaClientes() {

        ClienteEntity cliente = new ClienteEntity();

        cliente.setId(1L);
        cliente.setNome("Pedro");
        cliente.setSobrenome("Flamengo");
        cliente.setCelular("45 99980-7777");

        List<ClienteEntity> clientes = new ArrayList<>();
        clientes.add(cliente);

        when(clienteService.findAll()).thenReturn(clientes);

        ResponseEntity<?> response = clienteController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientes, response.getBody());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar todos os clientes lança exceção deve retornar BAD_REQUEST")
    void deveRetornarErroListaClientes() {

        when(clienteService.findAll()).thenThrow(new RuntimeException("Erro simulado"));

        ResponseEntity<?> response = clienteController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());


    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente por ID existente deve retornar cliente e status 200 OK")
    void  deveRetornarClientePorid() {

        ClienteEntity cliente = new ClienteEntity();

        cliente.setId(1L);
        cliente.setNome("Pedro");
        cliente.setSobrenome("Flamengo");
        cliente.setCelular("45 99980-7777");

        when(clienteService.findById(1L)).thenReturn(cliente);

        ResponseEntity<?> response = clienteController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente por ID inexistente deve retornar BAD_REQUEST")
    void  deveRetornarErroClientePorid () {

        ClienteEntity cliente = new ClienteEntity();

        cliente.setId(2L);
        cliente.setNome("Pedro");
        cliente.setSobrenome("Flamengo");
        cliente.setCelular("45 99980-7777");


        when(clienteService.findById(1L)).thenThrow(new RuntimeException(("Erro simulado")));

        ResponseEntity<?> response = clienteController.findById(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Atualizar cliente existente deve retornar cliente atualizado e status 200 OK")
    void deveAtualizarClienteComSucesso() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(2L);
        cliente.setNome("Pedro");
        cliente.setSobrenome("Flamengo");
        cliente.setCelular("45 99980-7777");

        when(clienteService.update(2L, cliente)).thenReturn(cliente);

        ResponseEntity<?> response = clienteController.update(2L, cliente);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Atualizar cliente inexistente deve retornar BAD_REQUEST")
    void deveRetornarBadRequestQuandoAtualizacaoFalha() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(2L);

        when(clienteService.update(2L, cliente)).thenThrow(new RuntimeException("Cliente não encontrado"));


        ResponseEntity<?> response = clienteController.update(2L, cliente);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deletar cliente existente deve retornar status 200 OK")
    void deveRetornarOkQuandoDeletarCliente(){

       Long idCLiente = 2L;

        doNothing().when(clienteService).delete(idCLiente);

        ResponseEntity<?> response = clienteController.delete(idCLiente);

       assertEquals(HttpStatus.OK,response.getStatusCode());

    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deletar cliente inexistente deve retornar BAD_REQUEST")
    void  deveRetornarErroQuandoDeletarCliente(){

        Long idCLiente = 2L;

        doThrow(new RuntimeException("Cliente nao encontrado")).when(clienteService).delete(idCLiente);


        ResponseEntity<?> response = clienteController.delete(idCLiente);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());

    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deletar cliente inexistente deve retornar BAD_REQUEST")
    void deveRetornarOClientePeloNome() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Pedro");
        cliente.setSobrenome("Flamengo");
        cliente.setCelular("45 99980-7777");

        List<ClienteEntity> clienteLista = new ArrayList<>();
        clienteLista.add(cliente);


        when(clienteService.findByNome("Pedro")).thenReturn(clienteLista);


        ResponseEntity<?> response = clienteController.findByNome("Pedro");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteLista, response.getBody());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente pelo nome inexistente deve retornar BAD_REQUEST")
    void  deveRetornarErroProcurarClientePeloNome() {

        when(clienteService.findByNome("Pedro")).thenThrow(new RuntimeException("Cliente nao encontrado pelo nome"));

        ResponseEntity<?> response = clienteController.findByNome("Pedro");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    @DisplayName("Deve retornar o cliente pelo numero")
    void deveRetornarOClientePeloNumeroo() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Pedro");
        cliente.setSobrenome("Flamengo");
        cliente.setCelular("45 99980-7777");

        List<ClienteEntity> clienteLista = new ArrayList<>();
        clienteLista.add(cliente);


        when(clienteService.findByCelular("45 99980-7777")).thenReturn(clienteLista);


        ResponseEntity<?> response = clienteController.findByCelular("45 99980-7777");

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente pelo nome inexistente deve retornar BAD_REQUEST")
    void deveRetornarErroProcurarClientePeloNumero() {

        when(clienteService.findByCelular("45 99980-7777")).thenThrow(new RuntimeException( "Erro ao procurar cliente pelo numero"));
        ResponseEntity<?> response = clienteController.findByCelular("45 99980-7777");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente pelo nome completo deve retornar lista e status 200 OK")
    void deveRetornarClientePeloNomeCompleto() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Pedro");
        cliente.setSobrenome("Flamengo");
        cliente.setCelular("45 99980-7777");

        List<ClienteEntity> clienteLista = new ArrayList<>();
        clienteLista.add(cliente);

        when(clienteService.findByNomeCompleto("Pedro Flamengo")).thenReturn(clienteLista);

        ResponseEntity<?> response = clienteController.findByNomeCompleto("Pedro Flamengo");

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Buscar cliente pelo nome completo inexistente deve retornar BAD_REQUEST")
    void deveRetornarErroClientePeloNomeCompleto() {

        when(clienteService.findByNomeCompleto("Pedro Flamengo")).thenThrow(new RuntimeException("Erro ao retornar cliente pelo nome completo"));

        ResponseEntity<?> response = clienteController.findByNomeCompleto("Pedro Flamengo");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

















}
