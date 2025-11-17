package com.mentoria.back_end_mentoria.meta;

import com.mentoria.back_end_mentoria.meta.vo.MetaRequest;
import com.mentoria.back_end_mentoria.meta.vo.MetaResponse;
import com.mentoria.back_end_mentoria.meta.vo.StatusMeta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<MetaResponse>> findAll() {
        List<MetaResponse> lista = metaService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @Tag(name = "Metas Admin")
    @GetMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Busca uma meta por ID", description = "Retorna os detalhes de uma meta específica a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<MetaResponse> findById(@PathVariable UUID id) {
        MetaResponse dto = metaService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Metas Admin")
    @PostMapping
    @Operation(summary = "[ADMIN] Cria uma nova meta", description = "Cria uma nova meta associada a um perfil profissional. Requer perfil de ADMIN.")
    public ResponseEntity<MetaResponse> insert(@Valid @RequestBody MetaRequest dto) {
        MetaResponse response = metaService.insert(dto);
        return ResponseEntity.ok().body(response);
    }

    @Tag(name = "Metas Admin")
    @PutMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Atualiza uma meta", description = "Atualiza os dados de uma meta existente a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<MetaResponse> update(@PathVariable UUID id, @Valid @RequestBody MetaRequest dto) {
        MetaResponse response = metaService.update(id, dto);
        return ResponseEntity.ok().body(response);
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
    public ResponseEntity<List<MetaResponse>> findMyMetas() {
        List<MetaResponse> lista = metaService.findMyMetas();
        return ResponseEntity.ok().body(lista);
    }

    @Tag(name = "Metas User")
    @GetMapping(value = "/minhas/{id}")
    @Operation(summary = "[USER] Retorna uma meta por ID", description = "Retorna uma meta por ID da lista de metas do usuário autenticado.")
    public ResponseEntity<MetaResponse> findMyMetaPerID(@PathVariable UUID id) {
        MetaResponse response = metaService.findMyMetaPerID(id);
        return ResponseEntity.ok().body(response);
    }

    @Tag(name = "Metas User")
    @PostMapping(value = "/minhas")
    @Operation(summary = "[USER] Cria uma nova meta para mim", description = "Cria uma nova meta associada ao perfil do usuário autenticado.")
    public ResponseEntity<MetaResponse> insertMyMeta(@Valid @RequestBody MetaRequest dto) {
        MetaResponse response = metaService.insertMyMeta(dto);
        return ResponseEntity.ok().body(response);
    }

    @Tag(name = "Metas User")
    @PatchMapping (value = "/minhas/{id}")
    @Operation(summary = "[USER] Troca o Status de uma meta", description = "Troca o status de uma meta do usuário.")
    public ResponseEntity<MetaResponse> changeStatusMyMeta(@PathVariable UUID id,@RequestBody StatusMeta status) {
        MetaResponse response = metaService.changeStatus(id, status);
        return ResponseEntity.ok().body(response);
    }

    @Tag(name = "Metas User")
    @PutMapping(value = "/minhas/{id}")
    @Operation(summary = "[USER] Atualiza uma das minhas metas", description = "Atualiza uma meta existente do usuário autenticado a partir do seu ID.")
    public ResponseEntity<MetaResponse> updateMyMeta(@PathVariable UUID id,@Valid @RequestBody MetaRequest dto) {
        MetaResponse response = metaService.updateMyMeta(id, dto);
        return ResponseEntity.ok().body(response);
    }

    @Tag(name = "Metas User")
    @DeleteMapping(value = "/minhas/{id}")
    @Operation(summary = "[USER] Deleta uma das minhas metas", description = "Remove uma meta existente do usuário autenticado a partir do seu ID.")
    public ResponseEntity<Void> deleteMyMeta(@PathVariable UUID id) {
        metaService.deleteMyMeta(id);
        return ResponseEntity.noContent().build();
    }
}