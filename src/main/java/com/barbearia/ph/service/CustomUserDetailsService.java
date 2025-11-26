package com.barbearia.ph.service;

import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ClienteService clienteService;
    private final ProfissionalRepository profissionalRepository;

    @Override
    public UserDetails loadUserByUsername(String celular) throws UsernameNotFoundException {

        // Primeiro tenta cliente
        var clientes = clienteService.findByCelular(celular);
        if (!clientes.isEmpty()) return clientes.get(0); // ClienteEntity já implementa UserDetails

        // Depois tenta profissional/admin
        var profOpt = profissionalRepository.findByCelular(celular);
        if (profOpt.isPresent()) {
            var prof = profOpt.get();
            return new User(
                    prof.getCelular(),
                    prof.getSenha(),
                    List.of(new SimpleGrantedAuthority(prof.getRole().name()))
            );
        }

        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}