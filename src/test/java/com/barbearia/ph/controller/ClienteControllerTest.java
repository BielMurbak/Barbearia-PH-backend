package com.barbearia.ph.controller;

import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class ClienteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    ClienteService clienteService;

    @InjectMocks
    ClienteController clienteController;


    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve salvar cliente e retornar 200 OK via MockMvc")
    void deveSalvarClienteComMockMvc() throws Exception {
        String json = """
                    {
                        "nome": "Jorginho",
                        "sobrenome": "Scarabelot",
                        "celular": "+55 45 99980-6733"
                    }
                """;

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve salvar cliente e retornar BAD_REQUEST via MockMvc")
    void deveDarErroClienteComMockMvc() throws Exception {
        String json = """
                    {
                        "nome": "",
                        "sobrenome": "",
                        "celular": ""
                    }
                """;

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Deve retornar uma lista de clientes e status 200 OK via Mock")
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
        verify(clienteService, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar um erro quando for listar clientes via Mock ")
    void deveRetornarErroListaClientes() {

        when(clienteService.findAll()).thenThrow(new RuntimeException("Erro simulado"));

        ResponseEntity<?> response = clienteController.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());


    }

    @Test
    @DisplayName("Deve retornar um cliente com id que deseja via Mock")
    void deveRetornarClientePorid() {

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
    @DisplayName("Deve retornar um erro ao procurar cliente com id que deseja via Mock")
    void deveRetornarErroClientePorid() {

        ClienteEntity cliente = new ClienteEntity();

        cliente.setId(2L);
        cliente.setNome("Pedro");
        cliente.setSobrenome("Flamengo");
        cliente.setCelular("45 99980-7777");


        when(clienteService.findById(1l)).thenThrow(new RuntimeException(("Erro simulado")));

        ResponseEntity<?> response = clienteController.findById(1l);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

   // @Test
  //  @DisplayName("Deve retornar OK ao atualizar um cliente via MockMVC")
   // void deveAtualizarClienteComSucesso() throws Exception {
    //    String json = """
      //      {
      //          "nome": "Rafael",
        //        "sobrenome": "",
      //          "celular": ""
        //    }
      //  """;

      // // ClienteEntity clienteAtualizado = new ClienteEntity();
      //  clienteAtualizado.setId(1L);
      //  clienteAtualizado.setNome("Rafael");
      // // clienteAtualizado.setSobrenome("");
       // clienteAtualizado.setCelular("");

        // Mocka o serviço para retornar o cliente atualizado
   ///     when(clienteService.update(eq(1L), any(ClienteEntity.class)))
       ///         .thenReturn(clienteAtualizado);

      //  mockMvc.perform(put("/api/clientes/1")
     //                   .contentType(MediaType.APPLICATION_JSON)
       ///                 .content(json))
        //        .andExpect(status().isOk())
         //       .andExpect(content().contentType(MediaType.APPLICATION_JSON));
   // }









}
