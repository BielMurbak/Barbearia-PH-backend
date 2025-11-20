package com.barbearia.ph;

import com.barbearia.ph.model.*;
import com.barbearia.ph.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProfissionalRepository profissionalRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private ProfissionalServicoRepository profissionalServicoRepository;

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
            profissional.setCelular("+55 (45) 9857-3445");
            profissional.setSenha("Patrick123");
            profissional.setEspecializacao(Especializacao.Corte);
            profissional = profissionalRepository.save(profissional);
            System.out.println("Profissional criado!");
        } else {
            profissional = profissionalRepository.findAll().get(0);
            System.out.println("Profissional já existe!");
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
                // Criar serviço
                ServicoEntity servico = new ServicoEntity();
                servico.setDescricao(servicoData[0]);
                servico.setMinDeDuracao(Integer.parseInt(servicoData[1]));
                servico = servicoRepository.save(servico);
                
                // Criar profissional-serviço
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
