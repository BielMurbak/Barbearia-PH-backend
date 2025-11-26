package com.barbearia.ph;

import com.barbearia.ph.model.*;
import com.barbearia.ph.repository.*;
import com.barbearia.ph.service.*;
import com.barbearia.ph.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;


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
    public void run(String... args) throws Exception {
        System.out.println("=== DataLoader executando ===");

        // Criar profissional
        ProfissionalEntity profissional;
        if (profissionalRepository.count() == 0) {
            System.out.println("Criando profissional Patrick...");
            profissional = new ProfissionalEntity();
            profissional.setNome("Patrick");
            profissional.setSobrenome("Henrique");
            profissional.setCelular("(45) 9857-3445");
            profissional.setSenha(passwordEncoder.encode("Patrick123"));
            profissional.setEspecializacao(Especializacao.Corte);
            profissional.setRole(Role.ROLE_ADMIN);
            profissional = profissionalService.save(profissional);
            System.out.println("Profissional criado!");
        } else {
            profissional = profissionalRepository.findAll().get(0);
            System.out.println("Profissional já existe!");
        }

        // Criar clientes de teste
        if (clienteRepository.count() == 0) {
            System.out.println("Criando clientes de teste...");
            
            ClienteEntity cliente1 = new ClienteEntity();
            cliente1.setNome("Gabriel");
            cliente1.setSobrenome("Murbak");
            cliente1.setCelular("(45) 99935-5808");
            cliente1.setSenha(passwordEncoder.encode("123456"));
            cliente1.setRole(Role.ROLE_CLIENTE);
            clienteRepository.save(cliente1);
            
            ClienteEntity cliente2 = new ClienteEntity();
            cliente2.setNome("Rafael");
            cliente2.setSobrenome("Scarabelot");
            cliente2.setCelular("(45) 99999-9999");
            cliente2.setSenha(passwordEncoder.encode("abcdef"));
            cliente2.setRole(Role.ROLE_CLIENTE);
            clienteRepository.save(cliente2);
            
            System.out.println("Clientes criados!");
        }

        // Criar serviços
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

            for (String[] servicoData : servicos) {
                ServicoEntity servico = new ServicoEntity();
                servico.setDescricao(servicoData[0]);
                servico.setMinDeDuracao(Integer.parseInt(servicoData[1]));
                servico = servicoRepository.save(servico);

                ProfissionalServicoEntity profServ = new ProfissionalServicoEntity();
                profServ.setProfissionalEntity(profissional);
                profServ.setServicoEntity(servico);
                profServ.setPreco(Double.parseDouble(servicoData[2]));
                profissionalServicoRepository.save(profServ);
            }

            System.out.println("Serviços e profissional-serviços criados!");
        }
    }
}
