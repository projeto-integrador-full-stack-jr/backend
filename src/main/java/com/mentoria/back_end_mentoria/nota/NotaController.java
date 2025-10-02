package com.mentoria.back_end_mentoria.nota;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @GetMapping
    public ResponseEntity<List<NotaDTO>> findAll() {
        List<NotaDTO> lista = notaService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<NotaDTO> findById(@PathVariable UUID id) {
        NotaDTO dto = notaService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<NotaDTO> insert(@RequestBody NotaDTO dto) {
        dto = notaService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getNotaId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<NotaDTO> update(@PathVariable UUID id, @RequestBody NotaDTO dto) {
        dto = notaService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        notaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<NotaDTO>> findMyNotas() {
        List<NotaDTO> lista = notaService.findMyNotas();
        return ResponseEntity.ok().body(lista);
    }

    @PostMapping("/minhas")
    public ResponseEntity<NotaDTO> insertMyNota(@RequestBody NotaDTO dto) {
        NotaDTO novaNota = notaService.insertMyNota(dto);
        // A URI aponta para o recurso genérico, que poderá ser acessado pelo dono ou por um admin
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/notas/{id}")
                .buildAndExpand(novaNota.getNotaId()).toUri();
        return ResponseEntity.created(uri).body(novaNota);
    }

    @PutMapping("/minhas/{id}")
    public ResponseEntity<NotaDTO> updateMyNota(@PathVariable UUID id, @RequestBody NotaDTO dto) {
        NotaDTO notaAtualizada = notaService.updateMyNota(id, dto);
        return ResponseEntity.ok().body(notaAtualizada);
    }
}