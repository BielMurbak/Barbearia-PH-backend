package com.barbearia.ph;

import com.barbearia.ph.dto.ProfissionalServicoRequestDTO;
import com.barbearia.ph.model.*;
import com.barbearia.ph.repository.ProfissionalRepository;
import com.barbearia.ph.repository.ServicoRepository;
import com.barbearia.ph.repository.ClienteRepository;
import com.barbearia.ph.service.ClienteService;
import com.barbearia.ph.service.ProfissionalService;
import com.barbearia.ph.service.ProfissionalServicoService;
import com.barbearia.ph.service.ServicoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProfissionalRepository profissionalRepository;
    private final ProfissionalService profissionalService;
    private final ServicoRepository servicoRepository;
    private final ProfissionalServicoService profissionalServicoService;
    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;
    private final ServicoService servicoService;

    private record ServicoSeed(String descricao, int duracao, double preco) {}

    private static final ServicoSeed[] SERVICOS = {
        new ServicoSeed("Cabelo e barba",          60, 60.00),
        new ServicoSeed("Degrade e sobrancelha",   45, 45.00),
        new ServicoSeed("Social e sobrancelha",    40, 40.00),
        new ServicoSeed("Cabelo Social",           35, 35.00),
        new ServicoSeed("Cabelo",                  30, 40.00),
        new ServicoSeed("Acabamento no Cabelo",    20, 20.00),
        new ServicoSeed("Barba",                   25, 35.00),
        new ServicoSeed("Sobrancelha",             15, 10.00)
    };

    @Override
    public void run(String... args) {
        log.info("=== DataLoader iniciando ===");
        try {
            ProfissionalEntity profissional = seedProfissional();
            seedClientes();
            seedServicos(profissional);
        } catch (Exception e) {
            log.error("Erro no DataLoader: {}", e.getMessage(), e);
        }
        log.info("=== DataLoader finalizado ===");
    }

    private ProfissionalEntity seedProfissional() {
        return profissionalRepository.findFirstByOrderByIdAsc().orElseGet(() -> {
            log.info("Criando profissional Patrick...");
            ProfissionalEntity p = new ProfissionalEntity();
            p.setNome("Patrick");
            p.setSobrenome("Henrique");
            p.setCelular("(45) 9857-3445");
            p.setSenha("Patrick123"); // ProfissionalService.save() encoda a senha
            p.setEspecializacao(Especializacao.Corte);
            p.setRole(Role.ROLE_ADMIN);
            ProfissionalEntity saved = profissionalService.save(p);
            log.info("Profissional criado com ID: {}", saved.getId());
            return saved;
        });
    }

    private void seedClientes() {
        if (clienteRepository.count() > 0) {
            log.info("Clientes já existem, pulando criação.");
            return;
        }
        log.info("Criando clientes de teste...");
        criarCliente("Gabriel", "Murbak",     "(45) 99935-5808", "123456");
        criarCliente("Rafael",  "Scarabelot", "(45) 99999-9999", "abcdef");
    }

    private void criarCliente(String nome, String sobrenome, String celular, String senha) {
        ClienteEntity c = new ClienteEntity();
        c.setNome(nome);
        c.setSobrenome(sobrenome);
        c.setCelular(celular);
        c.setSenha(senha); // ClienteService.save() encoda a senha
        ClienteEntity saved = clienteService.save(c);
        log.info("Cliente {} criado com ID: {}", nome, saved.getId());
    }

    private void seedServicos(ProfissionalEntity profissional) {
        if (servicoRepository.count() > 0) {
            log.info("Serviços já existem, pulando criação.");
            return;
        }
        log.info("Criando serviços...");
        for (ServicoSeed seed : SERVICOS) {
            ServicoEntity servico = new ServicoEntity();
            servico.setDescricao(seed.descricao());
            servico.setMinDeDuracao(seed.duracao());
            ServicoEntity savedServico = servicoService.save(servico);

            ProfissionalServicoRequestDTO dto = new ProfissionalServicoRequestDTO();
            dto.setProfissionalId(profissional.getId());
            dto.setServicoId(savedServico.getId());
            dto.setPreco(seed.preco());
            profissionalServicoService.save(dto);

            log.info("Serviço '{}' criado e vinculado ao profissional ID: {}", savedServico.getDescricao(), profissional.getId());
        }
    }
}
