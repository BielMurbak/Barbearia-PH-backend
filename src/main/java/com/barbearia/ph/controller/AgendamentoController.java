package com.barbearia.ph.controller;

import com.barbearia.ph.dto.AgendamentoRequestDTO;
import com.barbearia.ph.dto.AgendamentoResponseDTO;
import com.barbearia.ph.dto.DtoMapper;
import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.security.AuthUtils;
import com.barbearia.ph.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> save(@Valid @RequestBody AgendamentoRequestDTO dto) {
        AgendamentoEntity salvo = agendamentoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toResponse(salvo));
    }

    /**
     * PUT: reagendar (data/horário). Admin e barbeiro podem editar qualquer
     * campo de qualquer agendamento. Cliente só pode mexer no próprio
     * agendamento, só até 3h antes do horário marcado, e não pode alterar
     * preço/serviço/profissional por aqui — só data e horário.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequestDTO dto,
            Authentication user) {
        try {
            boolean isAdmin = AuthUtils.isAdmin(user);
            boolean isBarbeiro = AuthUtils.isBarbeiro(user);

            if (!isAdmin && !isBarbeiro) {
                if (user == null) {
                    return ResponseEntity.status(401).body("Autenticação necessária.");
                }

                AgendamentoEntity atual = agendamentoService.findById(id);
                Long clienteId = agendamentoService.getClienteIdByCelular(AuthUtils.celular(user));
                if (!atual.getClienteEntity().getId().equals(clienteId)) {
                    return ResponseEntity.status(403).body("Você só pode editar seus próprios agendamentos.");
                }

                LocalDateTime inicioAtual = LocalDateTime.of(atual.getData(), LocalTime.parse(atual.getHorario()));
                if (LocalDateTime.now().isAfter(inicioAtual.minusHours(3))) {
                    return ResponseEntity.status(403)
                            .body("Só é possível alterar o agendamento até 3 horas antes do horário marcado.");
                }

                // Cliente só reagenda data/horário — preço, serviço e profissional ficam como estavam
                dto.setPreco(atual.getPreco());
                dto.setObservacoes(atual.getObservacoes());
                dto.setProfissionalServicoId(atual.getProfissionalServicoEntity().getId());
                dto.setClienteId(atual.getClienteEntity().getId());
            }

            AgendamentoEntity resultado = agendamentoService.update(id, dto);
            return ResponseEntity.ok(DtoMapper.toResponse(resultado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    /**
     * PATCH: atualização parcial — inclui cancelamento (status=CANCELADO) e
     * confirmação manual de atendimento (status=CONCLUIDO) pelo barbeiro.
     * Clientes só podem cancelar seus próprios agendamentos — nunca marcar como concluído.
     * Barbeiro só mexe em agendamentos vinculados a ele. Admin pode tudo.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchAgendamento(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates,
            Authentication user) {

        boolean isCancelamento = updates.containsKey("status")
                && "CANCELADO".equalsIgnoreCase(updates.get("status").toString());
        boolean isConclusao = updates.containsKey("status")
                && "CONCLUIDO".equalsIgnoreCase(updates.get("status").toString());

        if (isConclusao) {
            if (user == null) {
                return ResponseEntity.status(401).body("Autenticação necessária.");
            }
            boolean isAdmin = AuthUtils.isAdmin(user);
            boolean isBarbeiro = AuthUtils.isBarbeiro(user);

            if (isBarbeiro) {
                // Barbeiro confirma na hora que quiser, mas só do que é dele
                try {
                    Long profissionalId = agendamentoService.getProfissionalIdByCelular(AuthUtils.celular(user));
                    AgendamentoEntity ag = agendamentoService.findById(id);
                    if (!ag.getProfissionalServicoEntity().getProfissionalEntity().getId().equals(profissionalId)) {
                        return ResponseEntity.status(403)
                                .body("Você só pode confirmar agendamentos vinculados a você.");
                    }
                } catch (Exception e) {
                    return ResponseEntity.status(403).body("Acesso negado.");
                }
            } else if (!isAdmin) {
                // Cliente (ou a checagem automática das telas) só pode "concluir" depois
                // de 1h do horário marcado — é a carência pro barbeiro confirmar ou
                // cancelar um não-comparecimento antes do fechamento automático.
                try {
                    AgendamentoEntity ag = agendamentoService.findById(id);
                    LocalDateTime inicio = LocalDateTime.of(ag.getData(), LocalTime.parse(ag.getHorario()));
                    if (LocalDateTime.now().isBefore(inicio.plusHours(1))) {
                        return ResponseEntity.status(403)
                                .body("Só é possível concluir automaticamente 1 hora após o horário marcado.");
                    }
                } catch (Exception e) {
                    return ResponseEntity.status(403).body("Acesso negado.");
                }
            }
        }

        if (isCancelamento && user != null) {
            boolean isAdmin = AuthUtils.isAdmin(user);
            boolean isBarbeiro = AuthUtils.isBarbeiro(user);

            if (isBarbeiro) {
                // Barbeiro só pode cancelar agendamentos vinculados a ele
                try {
                    Long profissionalId = agendamentoService.getProfissionalIdByCelular(AuthUtils.celular(user));
                    AgendamentoEntity ag = agendamentoService.findById(id);
                    if (!ag.getProfissionalServicoEntity().getProfissionalEntity().getId().equals(profissionalId)) {
                        return ResponseEntity.status(403)
                                .body("Você só pode cancelar agendamentos vinculados a você.");
                    }
                } catch (Exception e) {
                    return ResponseEntity.status(403).body("Acesso negado.");
                }
            } else if (!isAdmin) {
                // Cliente só pode cancelar o próprio agendamento
                try {
                    Long clienteId = agendamentoService.getClienteIdByCelular(AuthUtils.celular(user));
                    AgendamentoEntity ag = agendamentoService.findById(id);
                    if (!ag.getClienteEntity().getId().equals(clienteId)) {
                        return ResponseEntity.status(403)
                                .body("Você só pode cancelar seus próprios agendamentos.");
                    }
                } catch (Exception e) {
                    return ResponseEntity.status(403).body("Acesso negado.");
                }
            }
        }

        return agendamentoService.patch(id, updates)
                .map(DtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> findAll(Authentication user) {

        if (user == null) {
            return ResponseEntity.status(401).body("Autenticação necessária para listar agendamentos.");
        }

        boolean isAdmin = AuthUtils.isAdmin(user);
        boolean isBarbeiro = AuthUtils.isBarbeiro(user);

        List<AgendamentoResponseDTO> result;
        if (isAdmin) {
            result = agendamentoService.findAllWithDetails().stream()
                    .map(DtoMapper::toResponse).toList();
        } else if (isBarbeiro) {
            Long profissionalId = agendamentoService.getProfissionalIdByCelular(AuthUtils.celular(user));
            result = agendamentoService.findAllWithDetails()
                    .stream()
                    .filter(a -> a.getProfissionalServicoEntity().getProfissionalEntity().getId().equals(profissionalId))
                    .map(DtoMapper::toResponse).toList();
        } else {
            Long clienteId = agendamentoService.getClienteIdByCelular(AuthUtils.celular(user));
            result = agendamentoService.findAllWithDetails().stream()
                    .filter(a -> a.getClienteEntity().getId().equals(clienteId))
                    .map(DtoMapper::toResponse).toList();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(DtoMapper.toResponse(agendamentoService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        agendamentoService.delete(id);
        return ResponseEntity.ok("Agendamento com ID " + id + " removido com sucesso.");
    }

    @GetMapping("/data")
    public ResponseEntity<List<AgendamentoResponseDTO>> findByData(@RequestParam String data) {
        List<AgendamentoResponseDTO> result = agendamentoService.findByData(LocalDate.parse(data)).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint público (sem login) usado pelo widget de agendamento para saber
     * quais horários já estão ocupados para um profissional numa data.
     * Retorna só horário + duração — nada de dados de cliente. Ignora CANCELADO,
     * então um horário cancelado libera na hora pra qualquer cliente.
     */
    @GetMapping("/ocupados")
    public ResponseEntity<List<HorarioOcupado>> findOcupados(
            @RequestParam String data,
            @RequestParam Long profissionalId) {
        LocalDate localDate = LocalDate.parse(data);
        List<HorarioOcupado> ocupados = agendamentoService.findOcupadosPorProfissionalEData(localDate, profissionalId)
                .stream()
                .map(a -> new HorarioOcupado(a.getHorario(), a.getProfissionalServicoEntity().getServicoEntity().getMinDeDuracao()))
                .toList();
        return ResponseEntity.ok(ocupados);
    }

    public record HorarioOcupado(String horario, int minDeDuracao) {}

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoResponseDTO>> findByCliente(@PathVariable Long clienteId) {
        List<AgendamentoResponseDTO> result = agendamentoService.findByCliente(clienteId).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoResponseDTO>> findByPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {
        List<AgendamentoResponseDTO> result = agendamentoService.findByPeriodo(
                LocalDate.parse(dataInicio), LocalDate.parse(dataFim)).stream()
                .map(DtoMapper::toResponse).toList();
        return ResponseEntity.ok(result);
    }
}
