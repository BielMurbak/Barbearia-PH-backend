package com.barbearia.ph;

import com.barbearia.ph.model.Especializacao;
import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== DataLoader executando ===");
        if (profissionalRepository.count() == 0) {
            System.out.println("Criando profissional Patrick...");
            ProfissionalEntity profissional = new ProfissionalEntity();
            profissional.setNome("Patrick");
            profissional.setSobrenome("Henrique");
            profissional.setCelular("+55 (45) 9857-3445");
            profissional.setEspecializacao(Especializacao.Corte);
            profissionalRepository.save(profissional);
            System.out.println("Profissional criado!");
        } else {
            System.out.println("Profissional já existe!");
        }
    }

}
