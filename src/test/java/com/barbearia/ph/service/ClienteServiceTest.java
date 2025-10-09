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
        // Não use @Autowired aqui — apenas instancia o objeto de teste
        cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Gabriel");
        cliente.setSobrenome("Murbak");
        cliente.setCelular("45 99935-5808");
    }

    @Test
    @DisplayName("Deve retornar cliente quando id existir")
    void deveRetornarClienteQuandoIdExistir() {
        // Arrange: mock correto
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        ClienteEntity resultado = clienteService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(clienteRepository, times(1)).findById(1L);
    }
}
