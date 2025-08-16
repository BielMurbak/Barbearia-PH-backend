package com.barbearia.ph.service;


import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteEntity save(ClienteEntity clienteEntity){
        return clienteRepository.save(clienteEntity);
    }


}
