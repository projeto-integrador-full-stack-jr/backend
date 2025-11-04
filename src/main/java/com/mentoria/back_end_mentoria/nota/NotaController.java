package com.mentoria.back_end_mentoria.nota;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @Tag(name = "Notas Admin")
    @GetMapping
    @Operation(summary = "[ADMIN] Lista todas as notas", description = "Retorna uma lista de todas as notas cadastradas no sistema. Requer perfil de ADMIN.")
    public ResponseEntity<List<NotaDTO>> findAll(){
        List<NotaDTO> list = notaService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @Tag(name = "Notas Admin")
    @GetMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Busca uma nota por ID", description = "Retorna os detalhes de uma nota específica a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<NotaDTO> findById(@PathVariable UUID id){
        NotaDTO dto = notaService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Notas Admin")
    @PostMapping
    @Operation(summary = "[ADMIN] Cria uma nova nota", description = "Cria uma nova nota associada a um perfil profissional. Requer perfil de ADMIN.")
    public ResponseEntity<NotaDTO> insert(@RequestBody NotaDTO dto){
        NotaDTO newDto = notaService.insert(dto);
        return ResponseEntity.ok().body(newDto);
    }

    @Tag(name = "Notas Admin")
    @PutMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Atualiza uma nota", description = "Atualiza os dados de uma nota existente a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<NotaDTO> update(@PathVariable UUID id, @RequestBody NotaDTO dto){
        NotaDTO newDto = notaService.update(id, dto);
        return ResponseEntity.ok().body(newDto);
    }

    @Tag(name = "Notas Admin")
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Deleta uma nota", description = "Remove uma nota do sistema a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        notaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Tag(name = "Notas User")
    @GetMapping(value = "/minhas")
    @Operation(summary = "[USER] Lista minhas notas", description = "Retorna uma lista de todas as notas associadas ao usuário autenticado.")
    public ResponseEntity<List<NotaDTO>> findMyNotas(){
        List<NotaDTO> list = notaService.findMyNotas();
        return ResponseEntity.ok().body(list);
    }

    @Tag(name = "Notas User")
    @PostMapping(value = "/minhas")
    @Operation(summary = "[USER] Cria uma nova nota para mim", description = "Cria uma nova nota associada ao perfil do usuário autenticado.")
    public ResponseEntity<NotaDTO> insertMyNota(@RequestBody NotaDTO dto){
        NotaDTO newDto = notaService.insertMyNota(dto);
        return ResponseEntity.ok().body(newDto);
    }

    @Tag(name = "Notas User")
    @PutMapping(value = "/minhas/{id}")
    @Operation(summary = "[USER] Atualiza uma das minhas notas", description = "Atualiza uma nota existente do usuário autenticado a partir do seu ID.")
    public ResponseEntity<NotaDTO> updateMyNota(@PathVariable UUID id, @RequestBody NotaDTO dto){
        NotaDTO newDto = notaService.updateMyNota(id, dto);
        return ResponseEntity.ok().body(newDto);
    }

    @Tag(name = "Notas User")
    @DeleteMapping(value = "/minhas/{id}")
    @Operation(summary = "[USER] Deleta uma das minhas notas", description = "Remove uma nota existente do usuário autenticado a partir do seu ID.")
    public ResponseEntity<Void> deleteMyNota(@PathVariable UUID id){
        notaService.deleteMyNota(id);
        return ResponseEntity.noContent().build();
    }
}