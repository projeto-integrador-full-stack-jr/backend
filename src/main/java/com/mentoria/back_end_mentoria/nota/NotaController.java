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
@Tag(name = "Notas", description = "Endpoints para gerenciamento de Notas")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @GetMapping
    @Operation(summary = "Lista todas as notas (ADMIN)", description = "Retorna uma lista de todas as notas cadastradas no sistema. Requer perfil de ADMIN.")
    public ResponseEntity<List<NotaDTO>> findAll(){
        List<NotaDTO> list = notaService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/minhas")
    @Operation(summary = "Lista minhas notas", description = "Retorna uma lista de todas as notas associadas ao usuário autenticado.")
    public ResponseEntity<List<NotaDTO>> findMyNotas(){
        List<NotaDTO> list = notaService.findMyNotas();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Busca uma nota por ID (ADMIN)", description = "Retorna os detalhes de uma nota específica a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<NotaDTO> findById(@PathVariable UUID id){
        NotaDTO dto = notaService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    @Operation(summary = "Cria uma nova nota (ADMIN)", description = "Cria uma nova nota associada a um perfil profissional. Requer perfil de ADMIN.")
    public ResponseEntity<NotaDTO> insert(@RequestBody NotaDTO dto){
        NotaDTO newDto = notaService.insert(dto);
        return ResponseEntity.ok().body(newDto);
    }

    @PostMapping(value = "/minhas")
    @Operation(summary = "Cria uma nova nota para mim", description = "Cria uma nova nota associada ao perfil do usuário autenticado.")
    public ResponseEntity<NotaDTO> insertMyNota(@RequestBody NotaDTO dto){
        NotaDTO newDto = notaService.insertMyNota(dto);
        return ResponseEntity.ok().body(newDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualiza uma nota (ADMIN)", description = "Atualiza os dados de uma nota existente a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<NotaDTO> update(@PathVariable UUID id, @RequestBody NotaDTO dto){
        NotaDTO newDto = notaService.update(id, dto);
        return ResponseEntity.ok().body(newDto);
    }

    @PutMapping(value = "/minhas/{id}")
    @Operation(summary = "Atualiza uma das minhas notas", description = "Atualiza uma nota existente do usuário autenticado a partir do seu ID.")
    public ResponseEntity<NotaDTO> updateMyNota(@PathVariable UUID id, @RequestBody NotaDTO dto){
        NotaDTO newDto = notaService.updateMyNota(id, dto);
        return ResponseEntity.ok().body(newDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deleta uma nota (ADMIN)", description = "Remove uma nota do sistema a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        notaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/minhas/{id}")
    @Operation(summary = "Deleta uma das minhas notas", description = "Remove uma nota existente do usuário autenticado a partir do seu ID.")
    public ResponseEntity<Void> deleteMyNota(@PathVariable UUID id){
        notaService.deleteMyNota(id);
        return ResponseEntity.noContent().build();
    }
}
