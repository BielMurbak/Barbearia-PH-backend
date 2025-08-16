package com.barbearia.ph.controller;


import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.model.ServicoEntity;
import com.barbearia.ph.service.ProfissionalServicoService;
import com.barbearia.ph.service.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Servico")
@RequiredArgsConstructor

public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping("/save")
    public ResponseEntity<ServicoEntity> save(@RequestBody ServicoEntity servicoEntity) {
        try {
            return new ResponseEntity<>(servicoService.save(servicoEntity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
    }

}
