package com.mentoria.back_end_mentoria.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/metas")
public class MetaController {

    @Autowired
    private MetaService metaService;

    @GetMapping
    public ResponseEntity<List<MetaDTO>> findAll() {
        List<MetaDTO> lista = metaService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MetaDTO> findById(@PathVariable UUID id) {
        MetaDTO dto = metaService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<MetaDTO> insert(@RequestBody MetaDTO dto) {
        dto = metaService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getMetaId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MetaDTO> update(@PathVariable UUID id, @RequestBody MetaDTO dto) {
        dto = metaService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        metaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<MetaDTO>> findMyMetas() {
        List<MetaDTO> lista = metaService.findMyMetas();
        return ResponseEntity.ok().body(lista);
    }

    @PostMapping("/minhas")
    public ResponseEntity<MetaDTO> insertMyMeta(@RequestBody MetaDTO dto) {
        MetaDTO novaMeta = metaService.insertMyMeta(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/metas/{id}")
                .buildAndExpand(novaMeta.getMetaId()).toUri();
        return ResponseEntity.created(uri).body(novaMeta);
    }
}