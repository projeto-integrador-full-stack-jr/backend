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
    public ResponseEntity<List<ResumoDTO>> findAll() {
        List<ResumoDTO> lista = resumoService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResumoDTO> findById(@PathVariable UUID id) {
        ResumoDTO dto = resumoService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ResumoDTO> insert(@RequestBody ResumoDTO dto) {
        dto = resumoService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getResumoId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PostMapping(value = "/gerar-resumo-ia")
    public ResponseEntity<ResumoDTO> criarResumoIA(@RequestParam UUID perfilId) {
        ResumoDTO novoResumoDto = resumoService.criarResumoIA(perfilId);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/resumos/{id}")
                .buildAndExpand(novoResumoDto.getResumoId()).toUri();
        return ResponseEntity.created(uri).body(novoResumoDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ResumoDTO> update(@PathVariable UUID id, @RequestBody ResumoDTO dto) {
        dto = resumoService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        resumoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}