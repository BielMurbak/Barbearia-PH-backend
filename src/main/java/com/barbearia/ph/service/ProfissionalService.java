package com.barbearia.ph.service;

import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.repository.ClienteRepository;
import com.barbearia.ph.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfissionalEntity save(ProfissionalEntity profissionalEntity) {
        // Sem isso, a senha ficava salva em texto puro e o login nunca batia
        if (profissionalEntity.getSenha() != null && !profissionalEntity.getSenha().isBlank()) {
            profissionalEntity.setSenha(passwordEncoder.encode(profissionalEntity.getSenha()));
        }
        return profissionalRepository.save(profissionalEntity);
    }

    public List<ProfissionalEntity> findAll(){
        return profissionalRepository.findAll();
    }

    public ProfissionalEntity findById(Long id){
        return profissionalRepository.findById(id).orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
    }

    public ProfissionalEntity update(Long id, ProfissionalEntity profissionalEntity){
        ProfissionalEntity profissional = findById(id);

        // Login é feito pelo celular (cliente e profissional dividem o mesmo
        // namespace), então o novo número não pode colidir com ninguém.
        if (profissionalEntity.getCelular() != null && !profissionalEntity.getCelular().equals(profissional.getCelular())) {
            boolean usadoPorOutroProfissional = profissionalRepository.findByCelular(profissionalEntity.getCelular())
                    .filter(p -> !p.getId().equals(id))
                    .isPresent();
            boolean usadoPorCliente = clienteRepository.findByCelular(profissionalEntity.getCelular()).isPresent();
            if (usadoPorOutroProfissional || usadoPorCliente) {
                throw new RuntimeException("Este número já está cadastrado.");
            }
        }

        profissional.setNome(profissionalEntity.getNome());
        profissional.setSobrenome(profissionalEntity.getSobrenome());
        profissional.setCelular(profissionalEntity.getCelular());
        profissional.setEspecializacao(profissionalEntity.getEspecializacao());
        // Editar dados do barbeiro nunca deve mexer na senha (mesmo padrão do
        // ClienteService.update()) — troca de senha deve ser um fluxo à parte.
        return profissionalRepository.save(profissional);
    }

    // Reset administrativo: o admin edita a equipe e não necessariamente sabe
    // a senha atual do barbeiro (foi exatamente isso que quebrou o login antes),
    // então aqui não se exige a senha antiga — só define a nova.
    public void redefinirSenha(Long id, String novaSenha) {
        ProfissionalEntity profissional = findById(id);
        if (novaSenha == null || novaSenha.length() < 4) {
            throw new RuntimeException("A nova senha deve ter pelo menos 4 caracteres.");
        }
        profissional.setSenha(passwordEncoder.encode(novaSenha));
        profissionalRepository.save(profissional);
    }

    public void delete(Long id){
        findById(id);
        profissionalRepository.deleteById(id);
    }

    public List<ProfissionalEntity> findByNome(String nome){
        return profissionalRepository.findByNomeIgnoreCaseContaining(nome);
    }

    public List<ProfissionalEntity> findByEspecializacao(com.barbearia.ph.model.Especializacao especializacao){
        return profissionalRepository.findByEspecializacao(especializacao);
    }

    public List<ProfissionalEntity> findByEspecializacaoAndNome(com.barbearia.ph.model.Especializacao especializacao, String nome){
        return profissionalRepository.findByEspecializacaoAndNomeContaining(especializacao, nome);
    }

    public ProfissionalEntity findByCelular(String celular){
        return profissionalRepository.findByCelular(celular)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
    }
}