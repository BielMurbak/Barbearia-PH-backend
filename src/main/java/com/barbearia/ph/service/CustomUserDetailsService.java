package com.barbearia.ph.service;

import com.barbearia.ph.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ClienteService clienteService;
    private final ProfissionalRepository profissionalRepository;

    @Override
    public UserDetails loadUserByUsername(String celular) throws UsernameNotFoundException {

        // Primeiro tenta cliente
        var clientes = clienteService.findByCelular(celular);
        if (!clientes.isEmpty()) return clientes.get(0); // ClienteEntity implementa UserDetails

        // Depois tenta profissional/admin
        var profOpt = profissionalRepository.findByCelular(celular);
        if (profOpt.isPresent()) {
            return profOpt.get(); // ProfissionalEntity implementa UserDetails
        }

        throw new UsernameNotFoundException("Usuário não encontrado com celular: " + celular);
    }
}