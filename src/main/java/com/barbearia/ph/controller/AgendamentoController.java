package com.barbearia.ph.controller;

import com.barbearia.ph.model.AgendamentoEntity;
import com.barbearia.ph.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

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
    public ResponseEntity<AgendamentoEntity> save(@Valid @RequestBody AgendamentoEntity agendamento) {
        return ResponseEntity.ok(agendamentoService.save(agendamento));
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
            @RequestBody AgendamentoEntity agendamentoEntity,
            @AuthenticationPrincipal UserDetails user) {
        try {
            boolean isAdmin = user != null && user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isBarbeiro = user != null && user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_BARBEIRO"));

            if (!isAdmin && !isBarbeiro) {
                if (user == null) {
                    return ResponseEntity.status(401).body("Autenticação necessária.");
                }

                AgendamentoEntity atual = agendamentoService.findById(id);
                Long clienteId = agendamentoService.getClienteIdByCelular(user.getUsername());
                if (!atual.getClienteEntity().getId().equals(clienteId)) {
                    return ResponseEntity.status(403).body("Você só pode editar seus próprios agendamentos.");
                }

                LocalDateTime inicioAtual = LocalDateTime.of(atual.getData(), LocalTime.parse(atual.getHorario()));
                if (LocalDateTime.now().isAfter(inicioAtual.minusHours(3))) {
                    return ResponseEntity.status(403)
                            .body("Só é possível alterar o agendamento até 3 horas antes do horário marcado.");
                }

                // Cliente só reagenda data/horário — preço, serviço e profissional ficam como estavam
                agendamentoEntity.setPreco(atual.getPreco());
                agendamentoEntity.setObservacoes(atual.getObservacoes());
                agendamentoEntity.setProfissionalServicoEntity(atual.getProfissionalServicoEntity());
            }

            AgendamentoEntity resultado = agendamentoService.update(id, agendamentoEntity);
            return ResponseEntity.ok(resultado);
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
            @AuthenticationPrincipal UserDetails user) {

        boolean isCancelamento = updates.containsKey("status")
                && "CANCELADO".equalsIgnoreCase(updates.get("status").toString());
        boolean isConclusao = updates.containsKey("status")
                && "CONCLUIDO".equalsIgnoreCase(updates.get("status").toString());

        if (isConclusao) {
            if (user == null) {
                return ResponseEntity.status(401).body("Autenticação necessária.");
            }
            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isBarbeiro = user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_BARBEIRO"));

            if (isBarbeiro) {
                // Barbeiro confirma na hora que quiser, mas só do que é dele
                try {
                    Long profissionalId = agendamentoService.getProfissionalIdByCelular(user.getUsername());
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
            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isBarbeiro = user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_BARBEIRO"));

            if (isBarbeiro) {
                // Barbeiro só pode cancelar agendamentos vinculados a ele
                try {
                    Long profissionalId = agendamentoService.getProfissionalIdByCelular(user.getUsername());
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
                    Long clienteId = agendamentoService.getClienteIdByCelular(user.getUsername());
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
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @AuthenticationPrincipal UserDetails user) {

        if (user == null) {
            return ResponseEntity.status(401).body("Autenticação necessária para listar agendamentos.");
        }

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isBarbeiro = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_BARBEIRO"));

        if (isAdmin) {
            return ResponseEntity.ok(agendamentoService.findAllWithDetails());
        } else if (isBarbeiro) {
            Long profissionalId = agendamentoService.getProfissionalIdByCelular(user.getUsername());
            return ResponseEntity.ok(agendamentoService.findAllWithDetails()
                    .stream()
                    .filter(a -> a.getProfissionalServicoEntity().getProfissionalEntity().getId().equals(profissionalId))
                    .toList());
        } else {
            Long clienteId = agendamentoService.getClienteIdByCelular(user.getUsername());
            return ResponseEntity.ok(agendamentoService.findAllWithDetails()
                    .stream()
                    .filter(a -> a.getClienteEntity().getId().equals(clienteId))
                    .toList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoEntity> findById(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.findById(id));
    }

    // DELETE restrito a ADMIN — para exclusão definitiva quando necessário
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        agendamentoService.delete(id);
        return ResponseEntity.ok("Agendamento com ID " + id + " removido com sucesso.");
    }

    @GetMapping("/data")
    public ResponseEntity<List<AgendamentoEntity>> findByData(@RequestParam String data) {
        LocalDate localDate = LocalDate.parse(data);
        return ResponseEntity.ok(agendamentoService.findByData(localDate));
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
    public ResponseEntity<List<AgendamentoEntity>> findByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(agendamentoService.findByCliente(clienteId));
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoEntity>> findByPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {
        LocalDate inicio = LocalDate.parse(dataInicio);
        LocalDate fim = LocalDate.parse(dataFim);
        return ResponseEntity.ok(agendamentoService.findByPeriodo(inicio, fim));
    }
}