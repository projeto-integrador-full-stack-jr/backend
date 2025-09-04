package com.mentoria.back_end_mentoria.resumo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/resumos")
public class ResumoController {

    @Autowired
    private ResumoService resumoService;

    @GetMapping
    public ResponseEntity<List<Resumo>> findAll() {
        List<Resumo> lista = resumoService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @PostMapping(value = "/novoResumo")
    public ResponseEntity<Resumo> insert(@RequestParam UUID perfilID) {
        Resumo resumo = resumoService.criarResumoIA(perfilID);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(resumo.getResumoId()).toUri();
        return ResponseEntity.created(uri).body(resumo);
    }


}
