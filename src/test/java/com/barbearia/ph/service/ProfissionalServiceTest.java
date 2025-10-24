package com.barbearia.ph.service;

import com.barbearia.ph.model.Especializacao;
import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.repository.ProfissionalRepository;
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
class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private ProfissionalService profissionalService;

    private ProfissionalEntity profissional;

    @BeforeEach
    void setUp() {
        profissional = new ProfissionalEntity();
        profissional.setId(1L);
        profissional.setNome("Carlos");
        profissional.setSobrenome("Silva");
        profissional.setCelular("45 99999-8888");
        profissional.setEspecializacao(Especializacao.Corte);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve salvar profissional com sucesso")
    void deveSalvarProfissionalComSucesso() {
        when(profissionalRepository.save(any(ProfissionalEntity.class))).thenReturn(profissional);

        ProfissionalEntity salvo = profissionalService.save(profissional);

        assertNotNull(salvo);
        assertEquals("Carlos", salvo.getNome());
        verify(profissionalRepository, times(1)).save(profissional);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve retornar lista de profissionais")
    void deveRetornarListaDeProfissionais() {
        when(profissionalRepository.findAll()).thenReturn(List.of(profissional));

        List<ProfissionalEntity> resultado = profissionalService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(profissionalRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve retornar profissional quando ID existir")
    void deveRetornarProfissionalQuandoIdExistir() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

        ProfissionalEntity resultado = profissionalService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(profissionalRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve lançar exceção quando ID não existir")
    void deveLancarExcecaoQuandoIdNaoExistir() {
        when(profissionalRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> profissionalService.findById(99L));

        assertEquals("Profissional não encontrado", ex.getMessage());
        verify(profissionalRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve atualizar profissional existente com sucesso")
    void deveAtualizarProfissionalComSucesso() {
        ProfissionalEntity novo = new ProfissionalEntity();
        novo.setNome("João");
        novo.setSobrenome("Santos");
        novo.setCelular("44 98888-9999");
        novo.setEspecializacao(Especializacao.Barba);

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(ProfissionalEntity.class))).thenReturn(profissional);

        ProfissionalEntity atualizado = profissionalService.update(1L, novo);

        assertNotNull(atualizado);
        assertEquals("João", atualizado.getNome());
        assertEquals("Santos", atualizado.getSobrenome());
        assertEquals("44 98888-9999", atualizado.getCelular());
        assertEquals(Especializacao.Barba, atualizado.getEspecializacao());
        verify(profissionalRepository).findById(1L);
        verify(profissionalRepository).save(profissional);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve deletar profissional existente")
    void deveDeletarProfissionalComSucesso() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

        profissionalService.delete(1L);

        verify(profissionalRepository).deleteById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve buscar profissionais pelo nome")
    void deveBuscarPorNome() {
        when(profissionalRepository.findByNomeIgnoreCaseContaining("Carlos"))
                .thenReturn(List.of(profissional));

        List<ProfissionalEntity> resultado = profissionalService.findByNome("Carlos");

        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNome());
        verify(profissionalRepository).findByNomeIgnoreCaseContaining("Carlos");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve buscar profissionais por especialização")
    void deveBuscarPorEspecializacao() {
        when(profissionalRepository.findByEspecializacao(Especializacao.Corte))
                .thenReturn(List.of(profissional));

        List<ProfissionalEntity> resultado = profissionalService.findByEspecializacao(Especializacao.Corte);

        assertEquals(1, resultado.size());
        assertEquals(Especializacao.Corte, resultado.get(0).getEspecializacao());
        verify(profissionalRepository).findByEspecializacao(Especializacao.Corte);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve buscar profissionais por especialização e nome")
    void deveBuscarPorEspecializacaoENome() {
        when(profissionalRepository.findByEspecializacaoAndNomeContaining(Especializacao.Corte, "Carlos"))
                .thenReturn(List.of(profissional));

        List<ProfissionalEntity> resultado = profissionalService.findByEspecializacaoAndNome(Especializacao.Corte, "Carlos");

        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNome());
        assertEquals(Especializacao.Corte, resultado.get(0).getEspecializacao());
        verify(profissionalRepository).findByEspecializacaoAndNomeContaining(Especializacao.Corte, "Carlos");
    }
}