package com.barbearia.ph.service;


import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteEntity save(ClienteEntity clienteEntity){
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
}
