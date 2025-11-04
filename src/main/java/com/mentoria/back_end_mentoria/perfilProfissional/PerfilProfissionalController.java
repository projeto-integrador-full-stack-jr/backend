package com.mentoria.back_end_mentoria.perfilProfissional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/perfis")
public class PerfilProfissionalController {

    @Autowired
    private PerfilProfissionalService perfilProfissionalService;

    @Tag(name = "Perfil Admin")
    @GetMapping
    @Operation(summary = "[ADMIN] Lista todos os perfis", description = "Retorna uma lista de todos os perfis profissionais cadastrados. Requer perfil de ADMIN.")
    public ResponseEntity<List<PerfilProfissionalDTO>> findAll() {
        List<PerfilProfissionalDTO> lista = perfilProfissionalService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @Tag(name = "Perfil Admin")
    @GetMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Busca um perfil por ID", description = "Retorna os detalhes de um perfil profissional a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<PerfilProfissionalDTO> findById(@PathVariable UUID id) {
        PerfilProfissionalDTO dto = perfilProfissionalService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Perfil Admin")
    @PostMapping
    @Operation(summary = "[ADMIN] Cria um novo perfil", description = "Cria um novo perfil profissional associado a um usuário. Requer perfil de ADMIN.")
    public ResponseEntity<PerfilProfissionalDTO> insert(@RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.insert(dto);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Perfil Admin")
    @PutMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Atualiza um perfil", description = "Atualiza os dados de um perfil profissional a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<PerfilProfissionalDTO> update(@PathVariable UUID id, @RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Perfil Admin")
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Deleta um perfil", description = "Remove um perfil profissional do sistema a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        perfilProfissionalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Meus Endpoints

    @Tag(name = "Perfil User")
    @GetMapping(value = "/meu")
    @Operation(summary = "[USER] Busca o meu perfil", description = "Retorna o perfil profissional completo associado ao usuário autenticado.")
    public ResponseEntity<PerfilProfissionalDTO> findMyProfile() {
        PerfilProfissionalDTO dto = perfilProfissionalService.findMyProfile();
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Perfil User")
    @PutMapping(value = "/meu")
    @Operation(summary = "[USER] Cria ou atualiza o meu perfil", description = "Cria (se não existir) ou atualiza o perfil profissional associado ao usuário autenticado.")
    public ResponseEntity<PerfilProfissionalDTO> createOrUpdateMyProfile(@RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.createOrUpdateMyProfile(dto);
        return ResponseEntity.ok().body(dto);
    }

    @Tag(name = "Perfil User")
    @DeleteMapping(value = "/meu")
    @Operation(summary = "[USER] Deleta o meu perfil", description = "Remove o perfil profissional associado ao usuário autenticado. Esta ação não pode ser desfeita.")
    public ResponseEntity<Void> deleteMyProfile() {
        perfilProfissionalService.deleteMyProfile();
        return ResponseEntity.noContent().build();
    }
}
