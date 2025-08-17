package com.barbearia.ph.controller;


import com.barbearia.ph.model.ProfissionalServicoEntity;
import com.barbearia.ph.service.ProfissionalServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profissionais/servicos")
@RequiredArgsConstructor
public class ProfissionalServicoController {

    private final ProfissionalServicoService profissionalServicoService;

    @PostMapping("/save")
    public ResponseEntity<ProfissionalServicoEntity> save(@RequestBody ProfissionalServicoEntity profissionalServicoEntity) {
        try {
            return new ResponseEntity<>(profissionalServicoService.save(profissionalServicoEntity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
    }
}
