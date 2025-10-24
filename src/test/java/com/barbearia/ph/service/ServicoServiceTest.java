package com.barbearia.ph.service;

import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.repository.ServicoRepository;
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
class ServicoServiceTest {

    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private ServicoService servicoService;

    private ServicoEntity servico;

    @BeforeEach
    void setUp() {
        servico = new ServicoEntity();
        servico.setId(1L);
        servico.setDescricao("Corte de Cabelo");
        servico.setMinDeDuracao(30);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve salvar serviço com sucesso")
    void deveSalvarServicoComSucesso() {
        when(servicoRepository.save(any(ServicoEntity.class))).thenReturn(servico);

        ServicoEntity salvo = servicoService.save(servico);

        assertNotNull(salvo);
        assertEquals("Corte de Cabelo", salvo.getDescricao());
        verify(servicoRepository, times(1)).save(servico);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve retornar lista de serviços")
    void deveRetornarListaDeServicos() {
        when(servicoRepository.findAll()).thenReturn(List.of(servico));

        List<ServicoEntity> resultado = servicoService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(servicoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve retornar serviço quando ID existir")
    void deveRetornarServicoQuandoIdExistir() {
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));

        ServicoEntity resultado = servicoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(servicoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve lançar exceção quando ID não existir")
    void deveLancarExcecaoQuandoIdNaoExistir() {
        when(servicoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> servicoService.findById(99L));

        assertEquals("Serviço não encontrado", ex.getMessage());
        verify(servicoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve atualizar serviço existente com sucesso")
    void deveAtualizarServicoComSucesso() {
        ServicoEntity novo = new ServicoEntity();
        novo.setDescricao("Barba");
        novo.setMinDeDuracao(20);

        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(servicoRepository.save(any(ServicoEntity.class))).thenReturn(servico);

        ServicoEntity atualizado = servicoService.update(1L, novo);

        assertNotNull(atualizado);
        assertEquals("Barba", atualizado.getDescricao());
        assertEquals(20, atualizado.getMinDeDuracao());
        verify(servicoRepository).findById(1L);
        verify(servicoRepository).save(servico);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve deletar serviço existente")
    void deveDeletarServicoComSucesso() {
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));

        servicoService.delete(1L);

        verify(servicoRepository).deleteById(1L);
    }
}