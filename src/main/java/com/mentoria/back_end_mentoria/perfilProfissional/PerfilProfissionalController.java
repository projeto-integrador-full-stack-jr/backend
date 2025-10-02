package com.mentoria.back_end_mentoria.perfilProfissional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/perfis")
public class PerfilProfissionalController {

    @Autowired
    private PerfilProfissionalService perfilProfissionalService;

    @GetMapping
    public ResponseEntity<List<PerfilProfissionalDTO>> findAll() {
        List<PerfilProfissionalDTO> lista = perfilProfissionalService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PerfilProfissionalDTO> findById(@PathVariable UUID id) {
        PerfilProfissionalDTO dto = perfilProfissionalService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<PerfilProfissionalDTO> insert(@RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getPerfilId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PerfilProfissionalDTO> update(@PathVariable UUID id, @RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        perfilProfissionalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/meu")
    public ResponseEntity<PerfilProfissionalDTO> findMyProfile() {
        PerfilProfissionalDTO dto = perfilProfissionalService.findMyProfile();
        return ResponseEntity.ok().body(dto);
    }
    
}