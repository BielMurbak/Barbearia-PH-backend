package com.barbearia.ph.controller;

import com.barbearia.ph.model.ClienteEntity;
import com.barbearia.ph.model.ProfissionalEntity;
import com.barbearia.ph.service.ClienteService;
import com.barbearia.ph.service.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profissional")
@RequiredArgsConstructor
public class ProfissionalController {


    private final ProfissionalService profissionalService;

    @PostMapping("/save")
    public ResponseEntity<ProfissionalEntity> save(@RequestBody ProfissionalEntity profissionalEntity) {
        try {
            return new ResponseEntity<>(profissionalService.save(profissionalEntity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
    }

}
