package com.mentoria.back_end_mentoria.resumo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/resumos")
@Tag(name = "Resumos", description = "Endpoints para gerenciamento de Resumos de Carreira")
public class ResumoController {

    @Autowired
    private ResumoService resumoService;

    @GetMapping
    @Operation(summary = "Lista todos os resumos (ADMIN)", description = "Retorna uma lista de todos os resumos gerados. Requer perfil de ADMIN.")
    public ResponseEntity<List<ResumoDTO>> findAll() {
        List<ResumoDTO> lista = resumoService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Busca um resumo por ID (ADMIN)", description = "Retorna os detalhes de um resumo específico a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<ResumoDTO> findById(@PathVariable UUID id) {
        ResumoDTO dto = resumoService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    @Operation(summary = "Gera um novo resumo para um perfil (ADMIN)", description = "Gera, via IA, um novo resumo de carreira para um perfil profissional específico. Requer perfil de ADMIN.")
    public ResponseEntity<ResumoDTO> insert(@RequestBody CreateResumoRequest request) {
        ResumoDTO dto = resumoService.insert(request.getPerfilProfissionalId());
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deleta um resumo (ADMIN)", description = "Remove um resumo do sistema a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        resumoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Meus Endpoints

    @GetMapping(value = "/meus")
    @Operation(summary = "Lista meus resumos", description = "Retorna uma lista de todos os resumos associados ao usuário autenticado.")
    public ResponseEntity<List<ResumoDTO>> findMyResumos() {
        List<ResumoDTO> lista = resumoService.findMyResumos();
        return ResponseEntity.ok().body(lista);
    }

    @PostMapping(value = "/meus")
    @Operation(summary = "Gera um novo resumo para mim", description = "Gera um novo resumo de carreira via IA para o usuário autenticado.")
    public ResponseEntity<ResumoDTO> insertMyResumo() {
        ResumoDTO newDto = resumoService.insertMyResumo();
        return ResponseEntity.ok().body(newDto);
    }

    @DeleteMapping(value = "/meus/{id}")
    @Operation(summary = "Deleta um dos meus resumos", description = "Remove um resumo existente do usuário autenticado a partir do seu ID.")
    public ResponseEntity<Void> deleteMyResumo(@PathVariable UUID id) {
        resumoService.deleteMyResumo(id);
        return ResponseEntity.noContent().build();
    }
}
