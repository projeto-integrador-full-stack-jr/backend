package com.mentoria.back_end_mentoria.resumo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
