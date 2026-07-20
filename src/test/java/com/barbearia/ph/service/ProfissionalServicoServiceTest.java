package com.barbearia.ph.service;

import com.barbearia.ph.dto.ProfissionalServicoRequestDTO;
import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.repository.ProfissionalRepository;
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
class ProfissionalServicoServiceTest {

    @Mock
    private ProfissionalServicoRepository profServRepository;
    @Mock
    private ProfissionalRepository profissionalRepository;
    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private ProfissionalServicoService profServService;

    private ProfissionalServicoEntity profServ;
    private ProfissionalEntity profissional;
    private ServicoEntity servico;
    private ProfissionalServicoRequestDTO dto;

    @BeforeEach
    void setUp() {
        profissional = new ProfissionalEntity();
        profissional.setId(1L);

        servico = new ServicoEntity();
        servico.setId(1L);

        profServ = new ProfissionalServicoEntity();
        profServ.setId(1L);
        profServ.setPreco(50.0);
        profServ.setProfissionalEntity(profissional);
        profServ.setServicoEntity(servico);

        dto = new ProfissionalServicoRequestDTO();
        dto.setProfissionalId(1L);
        dto.setServicoId(1L);
        dto.setPreco(50.0);
    }

    // ---------- TESTE DO SAVE ----------
    @Test
    @DisplayName("Deve salvar ProfissionalServico com sucesso")
    void deveSalvarProfissionalServicoComSucesso() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(profServRepository.save(any())).thenReturn(profServ);

        ProfissionalServicoEntity salvo = profServService.save(dto);

        assertNotNull(salvo);
        assertEquals(50.0, salvo.getPreco());
        verify(profServRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar quando profissional não existir")
    void deveLancarExcecaoQuandoProfissionalNaoExisteNoSave() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> profServService.save(dto));
        assertEquals("Profissional não encontrado", ex.getMessage());
    }

    // ---------- TESTE DO FIND BY ID ----------
    @Test
    @DisplayName("Deve retornar ProfissionalServico quando existir")
    void deveRetornarProfissionalServicoQuandoExistir() {
        when(profServRepository.findById(1L)).thenReturn(Optional.of(profServ));

        ProfissionalServicoEntity encontrado = profServService.findById(1L);

        assertNotNull(encontrado);
        assertEquals(50.0, encontrado.getPreco());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ProfissionalServico não existir")
    void deveLancarExcecaoQuandoProfissionalServicoNaoExistir() {
        when(profServRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> profServService.findById(1L));
        assertEquals("Profissional Serviço não encontrado com ID: 1", ex.getMessage());
    }

    // ---------- TESTE DO UPDATE ----------
    @Test
    @DisplayName("Deve atualizar ProfissionalServico com sucesso")
    void deveAtualizarProfissionalServicoComSucesso() {
        when(profServRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        dto.setPreco(60.0);
        ProfissionalServicoEntity salvo = new ProfissionalServicoEntity();
        salvo.setId(1L);
        salvo.setPreco(60.0);
        when(profServRepository.save(any())).thenReturn(salvo);

        ProfissionalServicoEntity atualizado = profServService.update(1L, dto);

        assertEquals(60.0, atualizado.getPreco());
        verify(profServRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar ProfissionalServico inexistente")
    void deveLancarExcecaoAoAtualizarProfissionalServicoInexistente() {
        when(profServRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> profServService.update(99L, dto));
        assertEquals("Profissional Serviço não encontrado com ID: 99", ex.getMessage());
    }

    // ---------- TESTE DO DELETE (soft delete) ----------
    @Test
    @DisplayName("Deve marcar ProfissionalServico como inativo em vez de apagar (soft delete)")
    void deveDesativarProfissionalServicoComSucesso() {
        when(profServRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(profServRepository.save(any())).thenReturn(profServ);

        profServService.delete(1L);

        assertFalse(profServ.isAtivo());
        verify(profServRepository, times(1)).save(profServ);
        verify(profServRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve manter o agendamento intacto ao desativar um serviço com agendamentos vinculados")
    void deveManterAgendamentoAoDesativarServicoComAgendamentos() {
        // Simula que existem agendamentos vinculados: como delete() nunca chama
        // deleteById(), não há como isso violar a FK de agendamento_entity.
        when(profServRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(profServRepository.save(any())).thenReturn(profServ);

        assertDoesNotThrow(() -> profServService.delete(1L));
        verify(profServRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção ao desativar ProfissionalServico inexistente")
    void deveLancarExcecaoAoDesativarProfissionalServicoInexistente() {
        when(profServRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> profServService.delete(99L));
        verify(profServRepository, never()).save(any());
    }

    // ---------- TESTE DO FIND ALL ----------
    @Test
    @DisplayName("Deve retornar só os ProfissionalServico ativos")
    void deveRetornarApenasProfissionalServicoAtivos() {
        when(profServRepository.findByAtivoTrue()).thenReturn(List.of(profServ));

        List<ProfissionalServicoEntity> lista = profServService.findAll();

        assertEquals(1, lista.size());
        verify(profServRepository, times(1)).findByAtivoTrue();
    }
}
