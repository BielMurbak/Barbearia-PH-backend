package com.barbearia.ph.service;

import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteEntity cliente;

    @BeforeEach
    void setUp() {
        cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Gabriel");
        cliente.setSobrenome("Murbak");
        cliente.setCelular("45 99935-5808");
    }

    // ---------- TESTES SAVE ----------

    @Test
    @DisplayName("Deve salvar cliente com sucesso")
    void deveSalvarClienteComSucesso() {
        when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(cliente);

        ClienteEntity salvo = clienteService.save(cliente);

        assertNotNull(salvo);
        assertEquals("Gabriel", salvo.getNome());
        verify(clienteRepository, times(1)).save(cliente);
    }

    // ---------- TESTES FIND ALL ----------

    @Test
    @DisplayName("Deve retornar lista de clientes")
    void deveRetornarListaDeClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<ClienteEntity> resultado = clienteService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver clientes")
    void deveRetornarListaVaziaQuandoNaoHouverClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of());

        List<ClienteEntity> resultado = clienteService.findAll();

        assertTrue(resultado.isEmpty());
        verify(clienteRepository, times(1)).findAll();
    }

    // ---------- TESTES FIND BY ID ----------

    @Test
    @DisplayName("Deve retornar cliente quando ID existir")
    void deveRetornarClienteQuandoIdExistir() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        ClienteEntity resultado = clienteService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID não existir")
    void deveLancarExcecaoQuandoIdNaoExistir() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> clienteService.findById(99L));

        assertEquals("Cliente não encontrado", ex.getMessage());
        verify(clienteRepository, times(1)).findById(99L);
    }

    // ---------- TESTES UPDATE ----------

    @Test
    @DisplayName("Deve atualizar cliente existente com sucesso")
    void deveAtualizarClienteComSucesso() {
        ClienteEntity novo = new ClienteEntity();
        novo.setNome("João");
        novo.setSobrenome("Silva");
        novo.setCelular("44 98888-9999");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(cliente);

        ClienteEntity atualizado = clienteService.update(1L, novo);

        assertNotNull(atualizado);
        assertEquals("João", atualizado.getNome());
        assertEquals("Silva", atualizado.getSobrenome());
        assertEquals("44 98888-9999", atualizado.getCelular());
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar cliente inexistente")
    void deveLancarExcecaoAoAtualizarClienteInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> clienteService.update(99L, cliente));

        assertEquals("Cliente não encontrado", ex.getMessage());
        verify(clienteRepository, times(1)).findById(99L);
    }

    // ---------- TESTES DELETE ----------

    @Test
    @DisplayName("Deve deletar cliente existente")
    void deveDeletarClienteComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.delete(1L);

        verify(clienteRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar cliente inexistente")
    void deveLancarExcecaoAoDeletarClienteInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> clienteService.delete(99L));

        assertEquals("Cliente não encontrado", ex.getMessage());
        verify(clienteRepository, times(1)).findById(99L);
        verify(clienteRepository, never()).deleteById(anyLong());
    }

    // ---------- TESTES FIND BY NOME ----------

    @Test
    @DisplayName("Deve buscar clientes pelo nome")
    void deveBuscarPorNome() {
        when(clienteRepository.findByNomeIgnoreCaseContaining("Gabriel"))
                .thenReturn(List.of(cliente));

        List<ClienteEntity> resultado = clienteService.findByNome("Gabriel");

        assertEquals(1, resultado.size());
        assertEquals("Gabriel", resultado.get(0).getNome());
        verify(clienteRepository).findByNomeIgnoreCaseContaining("Gabriel");
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar nome inexistente")
    void deveRetornarListaVaziaAoBuscarNomeInexistente() {
        when(clienteRepository.findByNomeIgnoreCaseContaining("Inexistente"))
                .thenReturn(List.of());

        List<ClienteEntity> resultado = clienteService.findByNome("Inexistente");

        assertTrue(resultado.isEmpty());
        verify(clienteRepository).findByNomeIgnoreCaseContaining("Inexistente");
    }

    // ---------- TESTES FIND BY CELULAR ----------

    @Test
    @DisplayName("Deve buscar clientes pelo celular")
    void deveBuscarPorCelular() {
        when(clienteRepository.findByCelularContaining("999"))
                .thenReturn(List.of(cliente));

        List<ClienteEntity> resultado = clienteService.findByCelular("999");

        assertEquals(1, resultado.size());
        assertEquals("45 99935-5808", resultado.get(0).getCelular());
        verify(clienteRepository).findByCelularContaining("999");
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar celular inexistente")
    void deveRetornarListaVaziaAoBuscarCelularInexistente() {
        when(clienteRepository.findByCelularContaining("000"))
                .thenReturn(List.of());

        List<ClienteEntity> resultado = clienteService.findByCelular("000");

        assertTrue(resultado.isEmpty());
        verify(clienteRepository).findByCelularContaining("000");
    }

    // ---------- TESTES FIND BY NOME COMPLETO ----------

    @Test
    @DisplayName("Deve buscar clientes pelo nome completo")
    void deveBuscarPorNomeCompleto() {
        when(clienteRepository.findByNomeCompletoContaining("Gabriel Murbak"))
                .thenReturn(List.of(cliente));

        List<ClienteEntity> resultado = clienteService.findByNomeCompleto("Gabriel Murbak");

        assertEquals(1, resultado.size());
        verify(clienteRepository).findByNomeCompletoContaining("Gabriel Murbak");
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar nome completo inexistente")
    void deveRetornarListaVaziaAoBuscarNomeCompletoInexistente() {
        when(clienteRepository.findByNomeCompletoContaining("Fulano de Tal"))
                .thenReturn(List.of());

        List<ClienteEntity> resultado = clienteService.findByNomeCompleto("Fulano de Tal");

        assertTrue(resultado.isEmpty());
        verify(clienteRepository).findByNomeCompletoContaining("Fulano de Tal");
    }
}
