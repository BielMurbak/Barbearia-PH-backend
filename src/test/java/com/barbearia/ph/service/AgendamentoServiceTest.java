package com.barbearia.ph.service;

import com.barbearia.ph.model.*;
import com.barbearia.ph.repository.AgendamentoRepository;
import com.barbearia.ph.repository.ClienteRepository;
import com.barbearia.ph.repository.ProfissionalServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ProfissionalServicoRepository profissionalServicoRepository;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private AgendamentoEntity agendamento;
    private ClienteEntity cliente;
    private ProfissionalServicoEntity profServ;
    private ServicoEntity servico;
    private ProfissionalEntity profissional;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ---------- CLIENTE ----------
        cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Gabriel");
        cliente.setSobrenome("Pereira");
        cliente.setCelular("99999-9999");

        // ---------- SERVIÇO ----------
        servico = new ServicoEntity();
        servico.setId(1L);
        servico.setDescricao("Corte de cabelo masculino");
        servico.setMinDeDuracao(30);

        // ---------- PROFISSIONAL ----------
        profissional = new ProfissionalEntity();
        profissional.setId(1L);
        profissional.setNome("João");
        profissional.setSobrenome("Silva");
        profissional.setCelular("88888-8888");
        profissional.setEspecializacao(Especializacao.Corte);

        // ---------- PROFISSIONAL-SERVIÇO ----------
        profServ = new ProfissionalServicoEntity();
        profServ.setId(1L);
        profServ.setPreco(45.0);
        profServ.setServicoEntity(servico);
        profServ.setProfissionalEntity(profissional);

        // ---------- AGENDAMENTO ----------
        agendamento = new AgendamentoEntity();
        agendamento.setId(1L);
        agendamento.setData(LocalDate.now().plusDays(1));
        agendamento.setLocal("Barbearia PH");
        agendamento.setHorario("10:00");
        agendamento.setClienteEntity(cliente);
        agendamento.setProfissionalServicoEntity(profServ);
    }

    // ---------- TESTES DO SAVE ----------

    @Test
    @DisplayName("Deve salvar agendamento com sucesso")
    void deveSalvarAgendamentoComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profissionalServicoRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(agendamentoRepository.findByDataAndProfissionalServicoEntity_ProfissionalEntity_Id(
                any(), anyLong())).thenReturn(List.of());
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        AgendamentoEntity salvo = agendamentoService.save(agendamento);

        assertNotNull(salvo);
        assertEquals("Barbearia PH", salvo.getLocal());
        assertEquals(AgendamentoEntity.StatusAgendamento.PENDENTE, salvo.getStatus());
        verify(agendamentoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve definir status como CONCLUIDO quando agendamento já passou")
    void deveDefinirStatusConcluidoQuandoAgendamentoJaPassou() {
        // Criar agendamento no passado
        AgendamentoEntity agendamentoPassado = new AgendamentoEntity();
        agendamentoPassado.setId(1L);
        agendamentoPassado.setData(LocalDate.now().minusDays(1)); // Data no passado
        agendamentoPassado.setHorario("10:00");
        agendamentoPassado.setLocal("Barbearia PH");
        agendamentoPassado.setClienteEntity(cliente);
        agendamentoPassado.setProfissionalServicoEntity(profServ);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profissionalServicoRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(agendamentoRepository.findByDataAndProfissionalServicoEntity_ProfissionalEntity_Id(
                any(), anyLong())).thenReturn(List.of());
        when(agendamentoRepository.save(any())).thenReturn(agendamentoPassado);

        AgendamentoEntity salvo = agendamentoService.save(agendamentoPassado);

        assertNotNull(salvo);
        assertEquals(AgendamentoEntity.StatusAgendamento.CONCLUIDO, salvo.getStatus());
        verify(agendamentoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve continuar PENDENTE dentro da 1h de carência após o horário marcado")
    void deveContinuarPendenteDentroDaCarenciaDeUmaHora() {
        LocalDateTime agora = LocalDateTime.now();
        AgendamentoEntity agendamentoRecente = new AgendamentoEntity();
        agendamentoRecente.setId(1L);
        agendamentoRecente.setData(agora.toLocalDate());
        // 30 minutos atrás — dentro da carência de 1h
        agendamentoRecente.setHorario(agora.minusMinutes(30).toLocalTime().withSecond(0).withNano(0).toString().substring(0, 5));
        agendamentoRecente.setLocal("Barbearia PH");
        agendamentoRecente.setClienteEntity(cliente);
        agendamentoRecente.setProfissionalServicoEntity(profServ);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profissionalServicoRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(agendamentoRepository.findByDataAndProfissionalServicoEntity_ProfissionalEntity_Id(
                any(), anyLong())).thenReturn(List.of());
        when(agendamentoRepository.save(any())).thenReturn(agendamentoRecente);

        AgendamentoEntity salvo = agendamentoService.save(agendamentoRecente);

        assertEquals(AgendamentoEntity.StatusAgendamento.PENDENTE, salvo.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não existir no save")
    void deveLancarExcecaoQuandoClienteNaoExisteNoSave() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> agendamentoService.save(agendamento));
        assertEquals("Cliente não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando profissional/serviço não existir no save")
    void deveLancarExcecaoQuandoProfissionalServicoNaoExisteNoSave() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profissionalServicoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> agendamentoService.save(agendamento));
        assertEquals("ProfissionalServico não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando horário do agendamento conflita com outro existente")
    void deveLancarExcecaoQuandoHorarioConflita() {
        AgendamentoEntity existente = new AgendamentoEntity();
        existente.setId(2L);
        existente.setData(agendamento.getData());
        existente.setHorario("10:15");
        existente.setClienteEntity(cliente);
        existente.setProfissionalServicoEntity(profServ);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profissionalServicoRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(agendamentoRepository.findByDataAndProfissionalServicoEntity_ProfissionalEntity_Id(
                any(), anyLong())).thenReturn(List.of(existente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> agendamentoService.save(agendamento));
        assertTrue(ex.getMessage().contains("Horário indisponível"));
    }

    @Test
    @DisplayName("Deve salvar novo agendamento (de outro cliente) no mesmo horário de um CANCELADO")
    void deveSalvarQuandoExistenteEstaCancelado() {
        AgendamentoEntity cancelado = new AgendamentoEntity();
        cancelado.setId(2L);
        cancelado.setData(agendamento.getData());
        cancelado.setHorario("10:00"); // MESMO horário do novo agendamento
        cancelado.setStatus(AgendamentoEntity.StatusAgendamento.CANCELADO);
        cancelado.setClienteEntity(cliente);
        cancelado.setProfissionalServicoEntity(profServ);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profissionalServicoRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(agendamentoRepository.findByDataAndProfissionalServicoEntity_ProfissionalEntity_Id(
                any(), anyLong())).thenReturn(List.of(cancelado));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        AgendamentoEntity salvo = agendamentoService.save(agendamento);

        assertNotNull(salvo);
        verify(agendamentoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve salvar agendamento quando não há conflito de horário")
    void deveSalvarAgendamentoQuandoNaoHaConflito() {
        // Criar agendamento existente em horário que não conflita
        AgendamentoEntity existente = new AgendamentoEntity();
        existente.setId(2L);
        existente.setData(agendamento.getData());
        existente.setHorario("08:00"); // Horário que não conflita com 10:00
        existente.setClienteEntity(cliente);
        existente.setProfissionalServicoEntity(profServ);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profissionalServicoRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(agendamentoRepository.findByDataAndProfissionalServicoEntity_ProfissionalEntity_Id(
                any(), anyLong())).thenReturn(List.of(existente));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        AgendamentoEntity salvo = agendamentoService.save(agendamento);

        assertNotNull(salvo);
        verify(agendamentoRepository, times(1)).save(any());
    }

// ---------- TESTE DO FINDBYID ----------

    @Test
    @DisplayName("Deve retornar agendamento quando existir")
    void deveRetornarAgendamentoQuandoExistir() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        AgendamentoEntity encontrado = agendamentoService.findById(1L);

        assertNotNull(encontrado);
        assertEquals("Barbearia PH", encontrado.getLocal());
        assertEquals(cliente.getNome(), encontrado.getClienteEntity().getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando agendamento não for encontrado")
    void deveLancarExcecaoQuandoAgendamentoNaoEncontrado() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> agendamentoService.findById(1L));
        assertEquals("Agendamento não encontrado", ex.getMessage());
    }

// ---------- TESTE DO DELETE ----------

    @Test
    @DisplayName("Deve deletar agendamento com sucesso")
    void deveDeletarAgendamentoComSucesso() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        agendamentoService.delete(1L);

        verify(agendamentoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar agendamento inexistente")
    void deveLancarExcecaoAoDeletarAgendamentoInexistente() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> agendamentoService.delete(1L));
        assertEquals("Agendamento não encontrado", ex.getMessage());
    }

// ---------- TESTE DO UPDATE ----------

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve atualizar agendamento com sucesso")
    void deveAtualizarAgendamentoComSucesso() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profissionalServicoRepository.findById(1L)).thenReturn(Optional.of(profServ));

        AgendamentoEntity atualizado = new AgendamentoEntity();
        atualizado.setData(LocalDate.now().plusDays(2));
        atualizado.setHorario("15:00");
        atualizado.setLocal("PH Premium");
        atualizado.setClienteEntity(cliente);
        atualizado.setProfissionalServicoEntity(profServ);

        AgendamentoEntity resultado = agendamentoService.update(1L, atualizado);

        assertEquals("PH Premium", resultado.getLocal());
        verify(agendamentoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao reagendar para um horário que já conflita com outro agendamento")
    void deveLancarExcecaoAoAtualizarParaHorarioComConflito() {
        AgendamentoEntity outroExistente = new AgendamentoEntity();
        outroExistente.setId(2L);
        outroExistente.setData(LocalDate.now().plusDays(2));
        outroExistente.setHorario("15:00");
        outroExistente.setClienteEntity(cliente);
        outroExistente.setProfissionalServicoEntity(profServ);

        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profissionalServicoRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(agendamentoRepository.findByDataAndProfissionalServicoEntity_ProfissionalEntity_Id(
                any(), anyLong())).thenReturn(List.of(outroExistente));

        AgendamentoEntity atualizado = new AgendamentoEntity();
        atualizado.setData(LocalDate.now().plusDays(2));
        atualizado.setHorario("15:00"); // mesmo horário do outroExistente
        atualizado.setLocal("Barbearia PH");
        atualizado.setClienteEntity(cliente);
        atualizado.setProfissionalServicoEntity(profServ);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> agendamentoService.update(1L, atualizado));
        assertTrue(ex.getMessage().contains("Horário indisponível"));
        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve conflitar consigo mesmo ao reagendar (mesmo id no mesmo horário)")
    void naoDeveConflitarComSigoMesmoAoAtualizar() {
        AgendamentoEntity elesMesmo = new AgendamentoEntity();
        elesMesmo.setId(1L); // MESMO id do agendamento sendo atualizado
        elesMesmo.setData(LocalDate.now().plusDays(2));
        elesMesmo.setHorario("15:00");
        elesMesmo.setClienteEntity(cliente);
        elesMesmo.setProfissionalServicoEntity(profServ);

        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profissionalServicoRepository.findById(1L)).thenReturn(Optional.of(profServ));
        when(agendamentoRepository.findByDataAndProfissionalServicoEntity_ProfissionalEntity_Id(
                any(), anyLong())).thenReturn(List.of(elesMesmo));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        AgendamentoEntity atualizado = new AgendamentoEntity();
        atualizado.setData(LocalDate.now().plusDays(2));
        atualizado.setHorario("15:00");
        atualizado.setLocal("Barbearia PH");
        atualizado.setClienteEntity(cliente);
        atualizado.setProfissionalServicoEntity(profServ);

        AgendamentoEntity resultado = agendamentoService.update(1L, atualizado);

        assertNotNull(resultado);
        verify(agendamentoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve retornar lista de agendamentos")
    void deveRetornarListaDeAgendamentos() {
        when(agendamentoRepository.findAll()).thenReturn(List.of(agendamento));

        List<AgendamentoEntity> resultado = agendamentoService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(agendamentoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve buscar agendamentos por data")
    void deveBuscarAgendamentosPorData() {
        LocalDate data = LocalDate.now().plusDays(1);
        when(agendamentoRepository.findByData(data)).thenReturn(List.of(agendamento));

        List<AgendamentoEntity> resultado = agendamentoService.findByData(data);

        assertEquals(1, resultado.size());
        verify(agendamentoRepository).findByData(data);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve buscar agendamentos por cliente")
    void deveBuscarAgendamentosPorCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(agendamentoRepository.findByClienteEntity(cliente)).thenReturn(List.of(agendamento));

        List<AgendamentoEntity> resultado = agendamentoService.findByCliente(1L);

        assertEquals(1, resultado.size());
        verify(agendamentoRepository).findByClienteEntity(cliente);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve lançar exceção quando cliente não existe no findByCliente")
    void deveLancarExcecaoQuandoClienteNaoExisteNoFindByCliente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, 
            () -> agendamentoService.findByCliente(99L));
        assertEquals("Cliente não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve buscar agendamentos por período")
    void deveBuscarAgendamentosPorPeriodo() {
        LocalDate inicio = LocalDate.now();
        LocalDate fim = LocalDate.now().plusDays(7);
        when(agendamentoRepository.findAgendamentosPorPeriodo(inicio, fim)).thenReturn(List.of(agendamento));

        List<AgendamentoEntity> resultado = agendamentoService.findByPeriodo(inicio, fim);

        assertEquals(1, resultado.size());
        verify(agendamentoRepository).findAgendamentosPorPeriodo(inicio, fim);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve buscar agendamentos com detalhes")
    void deveBuscarAgendamentosComDetalhes() {
        when(agendamentoRepository.findAllWithDetails()).thenReturn(List.of(agendamento));

        List<AgendamentoEntity> resultado = agendamentoService.findAllWithDetails();

        assertEquals(1, resultado.size());
        verify(agendamentoRepository).findAllWithDetails();
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve fazer patch de agendamento com sucesso")
    void deveFazerPatchComSucesso() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        Map<String, Object> updates = Map.of(
            "local", "Nova Localização",
            "horario", "14:00"
        );

        Optional<AgendamentoEntity> resultado = agendamentoService.patch(1L, updates);

        assertTrue(resultado.isPresent());
        verify(agendamentoRepository).save(any());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve fazer patch com status cancelado")
    void deveFazerPatchComStatusCancelado() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        Map<String, Object> updates = Map.of("status", "CANCELADO");

        Optional<AgendamentoEntity> resultado = agendamentoService.patch(1L, updates);

        assertTrue(resultado.isPresent());
        verify(agendamentoRepository).save(any());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve fazer patch com status diferente de cancelado")
    void deveFazerPatchComStatusDiferenteDeCancelado() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        Map<String, Object> updates = Map.of("status", "PENDENTE");

        Optional<AgendamentoEntity> resultado = agendamentoService.patch(1L, updates);

        assertTrue(resultado.isPresent());
        verify(agendamentoRepository).save(any());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve fazer patch com clienteId")
    void deveFazerPatchComClienteId() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(clienteRepository.findById(2L)).thenReturn(Optional.of(cliente));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        Map<String, Object> updates = Map.of("clienteId", 2L);

        Optional<AgendamentoEntity> resultado = agendamentoService.patch(1L, updates);

        assertTrue(resultado.isPresent());
        verify(clienteRepository).findById(2L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve fazer patch com profissionalServicoId")
    void deveFazerPatchComProfissionalServicoId() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(profissionalServicoRepository.findById(2L)).thenReturn(Optional.of(profServ));
        when(agendamentoRepository.save(any())).thenReturn(agendamento);

        Map<String, Object> updates = Map.of("profissionalServicoId", 2L);

        Optional<AgendamentoEntity> resultado = agendamentoService.patch(1L, updates);

        assertTrue(resultado.isPresent());
        verify(profissionalServicoRepository).findById(2L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve retornar empty quando agendamento não existe no patch")
    void deveRetornarEmptyQuandoAgendamentoNaoExisteNoPatch() {
        when(agendamentoRepository.findById(99L)).thenReturn(Optional.empty());

        Map<String, Object> updates = Map.of("local", "Novo Local");
        Optional<AgendamentoEntity> resultado = agendamentoService.patch(99L, updates);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve lançar exceção quando cliente não existe no patch")
    void deveLancarExcecaoQuandoClienteNaoExisteNoPatch() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        Map<String, Object> updates = Map.of("clienteId", 99L);

        RuntimeException ex = assertThrows(RuntimeException.class, 
            () -> agendamentoService.patch(1L, updates));
        assertEquals("Cliente não encontrado", ex.getMessage());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO – Deve lançar exceção quando profissionalServico não existe no patch")
    void deveLancarExcecaoQuandoProfissionalServicoNaoExisteNoPatch() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(profissionalServicoRepository.findById(99L)).thenReturn(Optional.empty());

        Map<String, Object> updates = Map.of("profissionalServicoId", 99L);

        RuntimeException ex = assertThrows(RuntimeException.class, 
            () -> agendamentoService.patch(1L, updates));
        assertEquals("ProfissionalServiço não encontrado", ex.getMessage());
    }

}
