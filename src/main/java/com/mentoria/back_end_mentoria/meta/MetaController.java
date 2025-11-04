package com.mentoria.back_end_mentoria.meta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/metas")
public class MetaController {

    @Autowired
    private MetaService metaService;

    @Tag(name = "Metas Admin")
    @GetMapping
    @Operation(summary = "[ADMIN] Lista todas as metas", description = "Retorna uma lista de todas as metas cadastradas no sistema. Requer perfil de ADMIN.")
    public ResponseEntity<List<MetaDTO>> findAll() {
        List<MetaDTO> lista = metaService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @Tag(name = "Metas Admin")
    @GetMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Busca uma meta por ID", description = "Retorna os detalhes de uma meta específica a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<MetaDTO> findById(@PathVariable UUID id) {
        MetaDTO dto = metaService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Metas Admin")
    @PostMapping
    @Operation(summary = "[ADMIN] Cria uma nova meta", description = "Cria uma nova meta associada a um perfil profissional. Requer perfil de ADMIN.")
    public ResponseEntity<MetaDTO> insert(@RequestBody MetaDTO dto) {
        dto = metaService.insert(dto);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Metas Admin")
    @PutMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Atualiza uma meta", description = "Atualiza os dados de uma meta existente a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<MetaDTO> update(@PathVariable UUID id, @RequestBody MetaDTO dto) {
        dto = metaService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Metas Admin")
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Deleta uma meta", description = "Remove uma meta do sistema a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        metaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Meus Endpoints

    @Tag(name = "Metas User")
    @GetMapping(value = "/minhas")
    @Operation(summary = "[USER] Lista minhas metas", description = "Retorna uma lista de todas as metas associadas ao usuário autenticado.")
    public ResponseEntity<List<MetaDTO>> findMyMetas() {
        List<MetaDTO> lista = metaService.findMyMetas();
        return ResponseEntity.ok().body(lista);
    }

    @Tag(name = "Metas User")
    @PostMapping(value = "/minhas")
    @Operation(summary = "[USER] Cria uma nova meta para mim", description = "Cria uma nova meta associada ao perfil do usuário autenticado.")
    public ResponseEntity<MetaDTO> insertMyMeta(@RequestBody MetaDTO dto) {
        dto = metaService.insertMyMeta(dto);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Metas User")
    @PutMapping(value = "/minhas/{id}")
    @Operation(summary = "[USER] Atualiza uma das minhas metas", description = "Atualiza uma meta existente do usuário autenticado a partir do seu ID.")
    public ResponseEntity<MetaDTO> updateMyMeta(@PathVariable UUID id, @RequestBody MetaDTO dto) {
        dto = metaService.updateMyMeta(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Metas User")
    @DeleteMapping(value = "/minhas/{id}")
    @Operation(summary = "[USER] Deleta uma das minhas metas", description = "Remove uma meta existente do usuário autenticado a partir do seu ID.")
    public ResponseEntity<Void> deleteMyMeta(@PathVariable UUID id) {
        metaService.deleteMyMeta(id);
        return ResponseEntity.noContent().build();
    }
}