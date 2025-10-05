package com.mentoria.back_end_mentoria.perfilProfissional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/perfis")
@Tag(name = "Perfis Profissionais", description = "Endpoints para gerenciamento de Perfis Profissionais")
public class PerfilProfissionalController {

    @Autowired
    private PerfilProfissionalService perfilProfissionalService;

    @GetMapping
    @Operation(summary = "Lista todos os perfis (ADMIN)", description = "Retorna uma lista de todos os perfis profissionais cadastrados. Requer perfil de ADMIN.")
    public ResponseEntity<List<PerfilProfissionalDTO>> findAll() {
        List<PerfilProfissionalDTO> lista = perfilProfissionalService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Busca um perfil por ID (ADMIN)", description = "Retorna os detalhes de um perfil profissional a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<PerfilProfissionalDTO> findById(@PathVariable UUID id) {
        PerfilProfissionalDTO dto = perfilProfissionalService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    @Operation(summary = "Cria um novo perfil (ADMIN)", description = "Cria um novo perfil profissional associado a um usuário. Requer perfil de ADMIN.")
    public ResponseEntity<PerfilProfissionalDTO> insert(@RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.insert(dto);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualiza um perfil (ADMIN)", description = "Atualiza os dados de um perfil profissional a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<PerfilProfissionalDTO> update(@PathVariable UUID id, @RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deleta um perfil (ADMIN)", description = "Remove um perfil profissional do sistema a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        perfilProfissionalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Meus Endpoints

    @GetMapping(value = "/meu")
    @Operation(summary = "Busca o meu perfil", description = "Retorna o perfil profissional associado ao usuário autenticado.")
    public ResponseEntity<PerfilProfissionalDTO> findMyProfile() {
        PerfilProfissionalDTO dto = perfilProfissionalService.findMyProfile();
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping(value = "/meu")
    @Operation(summary = "Cria ou atualiza o meu perfil", description = "Cria um novo perfil profissional para o usuário autenticado, ou atualiza o perfil existente.")
    public ResponseEntity<PerfilProfissionalDTO> createOrUpdateMyProfile(@RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.createOrUpdateMyProfile(dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/meu")
    @Operation(summary = "Deleta o meu perfil", description = "Remove o perfil profissional associado ao usuário autenticado.")
    public ResponseEntity<Void> deleteMyProfile() {
        perfilProfissionalService.deleteMyProfile();
        return ResponseEntity.noContent().build();
    }
}
