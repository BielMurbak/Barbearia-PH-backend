package com.barbearia.ph.service;

import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
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

    @Mock
    private ProfissionalServicoRepository profissionalServicoRepository;

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
    @DisplayName("TESTE DE UNIDADE – Deve retornar só os serviços ativos")
    void deveRetornarListaDeServicos() {
        when(servicoRepository.findByAtivoTrue()).thenReturn(List.of(servico));

        List<ServicoEntity> resultado = servicoService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(servicoRepository, times(1)).findByAtivoTrue();
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
    @DisplayName("TESTE DE UNIDADE – Deve marcar serviço como inativo em vez de apagar (soft delete)")
    void deveDeletarServicoComSucesso() {
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(servicoRepository.save(any())).thenReturn(servico);
        when(profissionalServicoRepository.findByServicoEntity(servico)).thenReturn(List.of());

        servicoService.delete(1L);

        assertFalse(servico.isAtivo());
        verify(servicoRepository).save(servico);
        verify(servicoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve desativar também os vínculos de barbeiro ao desativar o serviço")
    void deveDesativarVinculosDeBarbeiroAoDeletarServico() {
        ProfissionalServicoEntity vinculo1 = new ProfissionalServicoEntity();
        vinculo1.setId(10L);
        ProfissionalServicoEntity vinculo2 = new ProfissionalServicoEntity();
        vinculo2.setId(11L);

        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(servicoRepository.save(any())).thenReturn(servico);
        when(profissionalServicoRepository.findByServicoEntity(servico)).thenReturn(List.of(vinculo1, vinculo2));

        servicoService.delete(1L);

        assertFalse(vinculo1.isAtivo());
        assertFalse(vinculo2.isAtivo());
        verify(profissionalServicoRepository).saveAll(List.of(vinculo1, vinculo2));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve buscar serviços por descrição")
    void deveBuscarServicosPorDescricao() {
        when(servicoRepository.findByDescricaoIgnoreCaseContaining("Corte")).thenReturn(List.of(servico));

        List<ServicoEntity> resultado = servicoService.findByDescricao("Corte");

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Corte de Cabelo", resultado.get(0).getDescricao());
        verify(servicoRepository, times(1)).findByDescricaoIgnoreCaseContaining("Corte");
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve buscar serviços por duração máxima")
    void deveBuscarServicosPorDuracaoMaxima() {
        when(servicoRepository.findByMinDeDuracaoLessThanEqual(45)).thenReturn(List.of(servico));

        List<ServicoEntity> resultado = servicoService.findByDuracaoMaxima(45);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(30, resultado.get(0).getMinDeDuracao());
        verify(servicoRepository, times(1)).findByMinDeDuracaoLessThanEqual(45);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve buscar serviços por faixa de duração")
    void deveBuscarServicosPorFaixaDuracao() {
        when(servicoRepository.findByDuracaoRange(15, 45)).thenReturn(List.of(servico));

        List<ServicoEntity> resultado = servicoService.findByDuracaoRange(15, 45);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(30, resultado.get(0).getMinDeDuracao());
        verify(servicoRepository, times(1)).findByDuracaoRange(15, 45);
    }
}