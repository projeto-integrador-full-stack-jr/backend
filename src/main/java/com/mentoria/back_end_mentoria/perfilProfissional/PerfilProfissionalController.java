package com.mentoria.back_end_mentoria.perfilProfissional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/perfis")
public class PerfilProfissionalController {

    @Autowired
    private PerfilProfissionalService perfilProfissionalService;

    @GetMapping
    public ResponseEntity<List<PerfilProfissional>> findAll() {
        List<PerfilProfissional> lista = perfilProfissionalService.findAll();
        return ResponseEntity.ok().body(lista);
    }
}
