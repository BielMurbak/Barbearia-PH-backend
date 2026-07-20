package com.barbearia.ph.dto;

import com.barbearia.ph.model.*;

public class DtoMapper {

    // ── Cliente ──────────────────────────────────────────────────────────────

    public static ClienteResponseDTO toResponse(ClienteEntity e) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(e.getId());
        dto.setNome(e.getNome());
        dto.setSobrenome(e.getSobrenome());
        dto.setCelular(e.getCelular());
        dto.setNomeCompleto(e.getNomeCompleto());
        return dto;
    }

    public static ClienteEntity toEntity(ClienteRequestDTO dto) {
        ClienteEntity e = new ClienteEntity();
        e.setNome(dto.getNome());
        e.setSobrenome(dto.getSobrenome());
        e.setCelular(dto.getCelular());
        e.setSenha(dto.getSenha());
        return e;
    }

    // ── Profissional ─────────────────────────────────────────────────────────

    public static ProfissionalResponseDTO toResponse(ProfissionalEntity e) {
        ProfissionalResponseDTO dto = new ProfissionalResponseDTO();
        dto.setId(e.getId());
        dto.setNome(e.getNome());
        dto.setSobrenome(e.getSobrenome());
        dto.setCelular(e.getCelular());
        dto.setNomeCompleto(e.getNomeCompleto());
        dto.setEspecializacao(e.getEspecializacao());
        return dto;
    }

    public static ProfissionalEntity toEntity(ProfissionalRequestDTO dto) {
        ProfissionalEntity e = new ProfissionalEntity();
        e.setNome(dto.getNome());
        e.setSobrenome(dto.getSobrenome());
        e.setCelular(dto.getCelular());
        e.setSenha(dto.getSenha());
        e.setEspecializacao(dto.getEspecializacao());
        return e;
    }

    // ── Servico ──────────────────────────────────────────────────────────────

    public static ServicoResponseDTO toResponse(ServicoEntity e) {
        ServicoResponseDTO dto = new ServicoResponseDTO();
        dto.setId(e.getId());
        dto.setDescricao(e.getDescricao());
        dto.setMinDeDuracao(e.getMinDeDuracao());
        return dto;
    }

    public static ServicoEntity toEntity(ServicoRequestDTO dto) {
        ServicoEntity e = new ServicoEntity();
        e.setDescricao(dto.getDescricao());
        e.setMinDeDuracao(dto.getMinDeDuracao());
        return e;
    }

    // ── ProfissionalServico ───────────────────────────────────────────────────

    public static ProfissionalServicoResponseDTO toResponse(ProfissionalServicoEntity e) {
        ProfissionalServicoResponseDTO dto = new ProfissionalServicoResponseDTO();
        dto.setId(e.getId());
        dto.setPreco(e.getPreco());
        dto.setProfissional(toResponse(e.getProfissionalEntity()));
        dto.setServico(toResponse(e.getServicoEntity()));
        return dto;
    }

    // ── Agendamento ───────────────────────────────────────────────────────────

    public static AgendamentoResponseDTO toResponse(AgendamentoEntity e) {
        AgendamentoResponseDTO dto = new AgendamentoResponseDTO();
        dto.setId(e.getId());
        dto.setData(e.getData());
        dto.setLocal(e.getLocal());
        dto.setHorario(e.getHorario());
        dto.setStatus(e.getStatus());
        dto.setObservacoes(e.getObservacoes());
        dto.setPreco(e.getPreco());
        dto.setCliente(toResponse(e.getClienteEntity()));
        dto.setProfissionalServico(toResponse(e.getProfissionalServicoEntity()));
        return dto;
    }
}
