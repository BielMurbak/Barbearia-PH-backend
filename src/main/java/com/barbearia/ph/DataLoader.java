package com.barbearia.ph;

import com.barbearia.ph.model.*;
import com.barbearia.ph.repository.*;
import com.barbearia.ph.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ProfissionalServicoRepository profissionalServicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Override
    public void run(String... args) {
        System.out.println("=== DataLoader iniciando ===");

        // --- Profissional ---
        try {
            ProfissionalEntity profissional = profissionalRepository.findFirstByOrderByIdAsc()
                    .orElseGet(() -> {
                        System.out.println("Criando profissional Patrick...");
                        ProfissionalEntity p = new ProfissionalEntity();
                        p.setNome("Patrick");
                        p.setSobrenome("Henrique");
                        p.setCelular("(45) 9857-3445");
                        p.setSenha(passwordEncoder.encode("Patrick123"));
                        p.setEspecializacao(Especializacao.Corte);
                        p.setRole(Role.ROLE_ADMIN);
                        ProfissionalEntity saved = profissionalService.save(p);
                        System.out.println("Profissional criado com ID: " + saved.getId());
                        return saved;
                    });
            System.out.println("Profissional existente ID: " + profissional.getId());

            // --- Clientes ---
            ClienteEntity gabriel = null;
            if (clienteRepository.count() == 0) {
                System.out.println("Criando clientes de teste...");

                ClienteEntity cliente1 = new ClienteEntity();
                cliente1.setNome("Gabriel");
                cliente1.setSobrenome("Murbak");
                cliente1.setCelular("(45) 99935-5808");
                cliente1.setSenha(passwordEncoder.encode("123456"));
                cliente1.setRole(Role.ROLE_CLIENTE);
                gabriel = clienteRepository.save(cliente1);
                System.out.println("Cliente Gabriel criado com ID: " + gabriel.getId());

                ClienteEntity cliente2 = new ClienteEntity();
                cliente2.setNome("Rafael");
                cliente2.setSobrenome("Scarabelot");
                cliente2.setCelular("(45) 99999-9999");
                cliente2.setSenha(passwordEncoder.encode("abcdef"));
                cliente2.setRole(Role.ROLE_CLIENTE);
                clienteRepository.save(cliente2);
                System.out.println("Cliente Rafael criado com ID: " + cliente2.getId());
            } else {
                System.out.println("Clientes já existem, pulando criação.");
                gabriel = clienteRepository.findAll().stream()
                        .filter(c -> c.getCelular() != null && c.getCelular().replaceAll("\\D", "").equals("45999355808"))
                        .findFirst()
                        .orElse(null);
                if (gabriel == null) {
                    System.out.println("AVISO: Gabriel não encontrado pelo celular, tentando pelo nome...");
                    gabriel = clienteRepository.findAll().stream()
                            .filter(c -> "Gabriel".equalsIgnoreCase(c.getNome().trim()))
                            .findFirst()
                            .orElse(null);
                }
                if (gabriel != null) {
                    System.out.println("Gabriel encontrado: ID=" + gabriel.getId() + " | " + gabriel.getNome() + " " + gabriel.getSobrenome());
                } else {
                    System.out.println("ERRO: Gabriel não encontrado no banco!");
                }
            }

            // --- Serviços ---
            if (servicoRepository.count() == 0) {
                System.out.println("Criando serviços...");
                String[][] servicos = {
                        {"Cabelo e barba",          "60", "60.00"},
                        {"Degrade e sobrancelha",   "45", "45.00"},
                        {"Social e sobrancelha",    "40", "40.00"},
                        {"Cabelo Social",            "35", "35.00"},
                        {"Cabelo",                  "30", "40.00"},
                        {"Acabamento no Cabelo",    "20", "20.00"},
                        {"Barba",                   "25", "35.00"},
                        {"Sobrancelha",             "15", "10.00"}
                };

                for (String[] s : servicos) {
                    ServicoEntity servico = new ServicoEntity();
                    servico.setDescricao(s[0]);
                    servico.setMinDeDuracao(Integer.parseInt(s[1]));
                    servico = servicoRepository.save(servico);
                    System.out.println("Serviço criado: " + servico.getDescricao() + " ID: " + servico.getId());

                    ProfissionalServicoEntity profServ = new ProfissionalServicoEntity();
                    profServ.setProfissionalEntity(profissional);
                    profServ.setServicoEntity(servico);
                    profServ.setPreco(Double.parseDouble(s[2]));
                    profissionalServicoRepository.save(profServ);
                    System.out.println("Vinculado profissional -> serviço: " + profissional.getId() + " -> " + servico.getId());
                }
            } else {
                System.out.println("Serviços já existem, pulando criação.");
            }

            // --- Agendamentos de teste ---
            if (gabriel != null) {
                System.out.println("Criando agendamentos de teste...");

                List<ProfissionalServicoEntity> profServicos = profissionalServicoRepository.findAll();

                // Helper: busca ProfissionalServico pelo nome do serviço
                ProfissionalServicoEntity svCabeloBarba    = getPS(profServicos, "Cabelo e barba");
                ProfissionalServicoEntity svDegrade        = getPS(profServicos, "Degrade e sobrancelha");
                ProfissionalServicoEntity svCabelo         = getPS(profServicos, "Cabelo");
                ProfissionalServicoEntity svBarba          = getPS(profServicos, "Barba");
                ProfissionalServicoEntity svSobrancelha    = getPS(profServicos, "Sobrancelha");
                ProfissionalServicoEntity svSocial         = getPS(profServicos, "Social e sobrancelha");
                ProfissionalServicoEntity svAcabamento     = getPS(profServicos, "Acabamento no Cabelo");

                // ── Dias anteriores desta semana (seg a seg) ─────────────────
                criarAgendamento(gabriel, svCabeloBarba,  LocalDate.of(2026, 7, 7), "09:00", "Barbearia PH", "Cabelo e barba", 60.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svDegrade,      LocalDate.of(2026, 7, 7), "10:00", "Barbearia PH", "Degrade e sobrancelha", 45.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svBarba,        LocalDate.of(2026, 7, 7), "14:00", "Barbearia PH", "Barba", 35.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svCabelo,       LocalDate.of(2026, 7, 6), "09:30", "Barbearia PH", "Cabelo", 40.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svSobrancelha,  LocalDate.of(2026, 7, 6), "11:00", "Barbearia PH", "Sobrancelha", 10.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svDegrade,      LocalDate.of(2026, 7, 4), "09:00", "Barbearia PH", "Degrade e sobrancelha", 45.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svBarba,        LocalDate.of(2026, 7, 4), "11:00", "Barbearia PH", "Barba", 35.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svCabelo,       LocalDate.of(2026, 7, 4), "15:00", "Barbearia PH", "Cabelo", 40.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svSocial,       LocalDate.of(2026, 7, 3), "10:00", "Barbearia PH", "Social e sobrancelha", 40.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svSobrancelha,  LocalDate.of(2026, 7, 3), "13:00", "Barbearia PH", "Sobrancelha", 10.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svCabeloBarba,  LocalDate.of(2026, 7, 2), "09:00", "Barbearia PH", "Cabelo e barba", 60.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svAcabamento,   LocalDate.of(2026, 7, 2), "11:30", "Barbearia PH", "Acabamento no Cabelo", 20.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svDegrade,      LocalDate.of(2026, 7, 1), "08:30", "Barbearia PH", "Degrade e sobrancelha", 45.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svBarba,        LocalDate.of(2026, 7, 1), "10:00", "Barbearia PH", "Barba", 35.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svCabelo,       LocalDate.of(2026, 6, 28), "09:00", "Barbearia PH", "Cabelo", 40.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svSobrancelha,  LocalDate.of(2026, 6, 28), "10:00", "Barbearia PH", "Sobrancelha", 10.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svCabeloBarba,  LocalDate.of(2026, 6, 25), "09:00", "Barbearia PH", "Cabelo e barba", 60.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svBarba,        LocalDate.of(2026, 6, 25), "11:00", "Barbearia PH", "Barba", 35.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svSocial,       LocalDate.of(2026, 6, 20), "14:00", "Barbearia PH", "Social e sobrancelha", 40.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svDegrade,      LocalDate.of(2026, 6, 15), "09:30", "Barbearia PH", "Degrade e sobrancelha", 45.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svAcabamento,   LocalDate.of(2026, 6, 15), "11:00", "Barbearia PH", "Acabamento no Cabelo", 20.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svCabeloBarba,  LocalDate.of(2026, 6, 10), "08:00", "Barbearia PH", "Cabelo e barba", 60.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svCabeloBarba,  LocalDate.of(2026, 7, 8), "09:00", "Barbearia PH", "Cabelo e barba", 60.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svDegrade,      LocalDate.of(2026, 7, 8), "11:00", "Barbearia PH", "Degrade e sobrancelha", 45.00, AgendamentoEntity.StatusAgendamento.CONCLUIDO);
                criarAgendamento(gabriel, svBarba,        LocalDate.of(2026, 7, 9), "14:00", "Barbearia PH", "Barba", 35.00, AgendamentoEntity.StatusAgendamento.PENDENTE);
                criarAgendamento(gabriel, svSobrancelha,  LocalDate.of(2026, 7, 9), "16:00", "Barbearia PH", "Sobrancelha", 10.00, AgendamentoEntity.StatusAgendamento.PENDENTE);

                System.out.println("Agendamentos de teste criados com sucesso!");
            } else {
                System.out.println("Gabriel não encontrado, pulando criação de agendamentos.");
            }

        } catch (Exception e) {
            System.err.println("Erro no DataLoader: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== DataLoader finalizado ===");
    }

    private ProfissionalServicoEntity getPS(List<ProfissionalServicoEntity> lista, String nomeServico) {
        return lista.stream()
                .filter(ps -> ps.getServicoEntity().getDescricao().equals(nomeServico))
                .findFirst()
                .orElse(null);
    }

    private void criarAgendamento(
            ClienteEntity cliente,
            ProfissionalServicoEntity profServico,
            LocalDate data,
            String horario,
            String local,
            String observacoes,
            Double preco,
            AgendamentoEntity.StatusAgendamento status) {

        if (profServico == null) {
            System.out.println("Serviço não encontrado para agendamento, pulando...");
            return;
        }

        AgendamentoEntity ag = new AgendamentoEntity();
        ag.setClienteEntity(cliente);
        ag.setProfissionalServicoEntity(profServico);
        ag.setData(data);
        ag.setHorario(horario);
        ag.setLocal(local);
        ag.setObservacoes(observacoes);
        ag.setPreco(preco);
        ag.setStatus(status);
        agendamentoRepository.save(ag);

        System.out.println("Agendamento criado: " + data + " " + horario + " - " + observacoes + " (R$ " + preco + ") [" + status + "]");
    }
}