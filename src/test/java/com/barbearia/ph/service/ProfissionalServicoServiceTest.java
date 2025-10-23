package com.barbearia.ph.service;

import com.barbearia.ph.model.ProfissionalServicoEntity;
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
    private com.barbearia.ph.repository.ProfissionalServicoRepository profServRepository;

    @InjectMocks
    private ProfissionalServicoService profServService;

    private ProfissionalServicoEntity profServ;

    @BeforeEach
    void setUp() {
        profServ = new ProfissionalServicoEntity();
        profServ.setId(1L);
        profServ.setPreco(50.0);
        // não precisa setar profissional e serviço aqui, depende do seu setup real
    }

    // ---------- TESTE DO SAVE ----------
    @Test
    @DisplayName("Deve salvar ProfissionalServico com sucesso")
    void deveSalvarProfissionalServicoComSucesso() {
        when(profServRepository.save(any())).thenReturn(profServ);

        ProfissionalServicoEntity salvo = profServService.save(profServ);

        assertNotNull(salvo);
        assertEquals(50.0, salvo.getPreco());
        verify(profServRepository, times(1)).save(any());
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
        profServ.setPreco(60.0);
        when(profServRepository.save(any())).thenReturn(profServ);

        ProfissionalServicoEntity atualizado = profServService.update(profServ);

        assertEquals(60.0, atualizado.getPreco());
        verify(profServRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar sem ID")
    void deveLancarExcecaoAoAtualizarSemId() {
        ProfissionalServicoEntity novo = new ProfissionalServicoEntity(); // id null

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> profServService.update(novo));
        assertEquals("ID do profissional serviço é obrigatório para update", ex.getMessage());
    }

    // ---------- TESTE DO DELETE ----------
    @Test
    @DisplayName("Deve deletar ProfissionalServico com sucesso")
    void deveDeletarProfissionalServicoComSucesso() {
        doNothing().when(profServRepository).deleteById(1L);

        profServService.delete(1L);

        verify(profServRepository, times(1)).deleteById(1L);
    }

    // ---------- TESTE DO FIND ALL ----------
    @Test
    @DisplayName("Deve retornar todos ProfissionalServico")
    void deveRetornarTodosProfissionalServico() {
        when(profServRepository.findAll()).thenReturn(List.of(profServ));

        List<ProfissionalServicoEntity> lista = profServService.findAll();

        assertEquals(1, lista.size());
        verify(profServRepository, times(1)).findAll();
    }
}