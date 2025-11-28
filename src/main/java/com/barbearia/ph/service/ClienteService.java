package com.barbearia.ph.service;


import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.model.Role;
import com.barbearia.ph.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteEntity save(ClienteEntity clienteEntity){
        
        if (clienteEntity.getSenha() != null && !clienteEntity.getSenha().trim().isEmpty()) {
            String senhaCriptografada = passwordEncoder.encode(clienteEntity.getSenha());
            clienteEntity.setSenha(senhaCriptografada);
        }
        
        // Definir role padrão para novos clientes
        if (clienteEntity.getRole() == null) {
            clienteEntity.setRole(Role.ROLE_CLIENTE);
        }
        
        return clienteRepository.save(clienteEntity);
    }

    public List<ClienteEntity> findAll(){
        return clienteRepository.findAll();
    }

    public ClienteEntity findById(Long id){
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public ClienteEntity update(Long id, ClienteEntity clienteEntity){
        ClienteEntity cliente = findById(id);
        cliente.setNome(clienteEntity.getNome());
        cliente.setSobrenome(clienteEntity.getSobrenome());
        cliente.setCelular(clienteEntity.getCelular());
        return clienteRepository.save(cliente);
    }

    public void delete(Long id){
        findById(id);
        clienteRepository.deleteById(id);
    }
    
    public List<ClienteEntity> findByNome(String nome){
        return clienteRepository.findByNomeIgnoreCaseContaining(nome);
    }
    
    public List<ClienteEntity> findByCelular(String celular){
        return clienteRepository.findByCelularContaining(celular);
    }
    
    public List<ClienteEntity> findByNomeCompleto(String nomeCompleto){
        return clienteRepository.findByNomeCompletoContaining(nomeCompleto);
    }
}
