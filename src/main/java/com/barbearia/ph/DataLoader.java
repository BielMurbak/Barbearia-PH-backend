package com.barbearia.ph;

import com.barbearia.ph.model.*;
import com.barbearia.ph.repository.*;
import com.barbearia.ph.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

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
            if (clienteRepository.count() == 0) {
                System.out.println("Criando clientes de teste...");
                ClienteEntity cliente1 = new ClienteEntity();
                cliente1.setNome("Gabriel");
                cliente1.setSobrenome("Murbak");
                cliente1.setCelular("(45) 99935-5808");
                cliente1.setSenha(passwordEncoder.encode("123456"));
                cliente1.setRole(Role.ROLE_CLIENTE);
                clienteRepository.save(cliente1);
                System.out.println("Cliente Gabriel criado com ID: " + cliente1.getId());

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
            }

            // --- Serviços ---
            if (servicoRepository.count() == 0) {
                System.out.println("Criando serviços...");
                String[][] servicos = {
                        {"Cabelo e barba", "60", "60.00"},
                        {"Degrade e sobrancelha", "45", "45.00"},
                        {"Social e sobrancelha", "40", "40.00"},
                        {"Cabelo Social", "35", "35.00"},
                        {"Cabelo", "30", "40.00"},
                        {"Acabamento no Cabelo", "20", "20.00"},
                        {"Barba", "25", "35.00"},
                        {"Sobrancelha", "15", "10.00"}
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

        } catch (Exception e) {
            System.err.println("Erro no DataLoader: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== DataLoader finalizado ===");
    }
}
