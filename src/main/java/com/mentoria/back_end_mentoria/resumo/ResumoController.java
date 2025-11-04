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
@Tag(name = "Resumos - Acesso Pessoal", description = "Endpoints para gerenciamento dos resumos de carreira do próprio usuário.")
public class ResumoController {

    @Autowired
    private ResumoService resumoService;

    @GetMapping
    @Operation(summary = "[ADMIN] Lista todos os resumos", description = "Retorna uma lista de todos os resumos gerados. Requer perfil de ADMIN.", tags = {"Resumos - Admin"})
    public ResponseEntity<List<ResumoDTO>> findAll() {
        List<ResumoDTO> lista = resumoService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Busca um resumo por ID", description = "Retorna os detalhes de um resumo específico a partir do seu ID. Requer perfil de ADMIN.", tags = {"Resumos - Admin"})
    public ResponseEntity<ResumoDTO> findById(@PathVariable UUID id) {
        ResumoDTO dto = resumoService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    @Operation(summary = "[ADMIN] Gera um novo resumo para um perfil", description = "Gera, via IA, um novo resumo de carreira para um perfil profissional específico. Requer perfil de ADMIN.", tags = {"Resumos - Admin"})
    public ResponseEntity<ResumoDTO> insert(@RequestBody CreateResumoRequest request) {
        ResumoDTO dto = resumoService.insert(request.getPerfilProfissionalId());
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Deleta um resumo", description = "Remove um resumo do sistema a partir do seu ID. Requer perfil de ADMIN.", tags = {"Resumos - Admin"})
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        resumoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Meus Endpoints

    @GetMapping(value = "/meus")
    @Operation(summary = "[USER] Lista meus resumos", description = "Retorna uma lista de todos os resumos associados ao usuário autenticado.")
    public ResponseEntity<List<ResumoDTO>> findMyResumos() {
        List<ResumoDTO> lista = resumoService.findMyResumos();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/meus/{id}")
    @Operation(summary = "[USER] Resumo por ID", description = "Retorna um resumo com base no UUID passado por paramentro, muito parecido com o delete, porém muda o verbo HTTP")
    public ResponseEntity<ResumoDTO> findMyResumo(@PathVariable UUID id) {
        ResumoDTO dto = resumoService.findMyResumoPerID(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping(value = "/meus")
    @Operation(summary = "[USER] Gera um novo resumo para mim", description = "Gera um novo resumo de carreira via IA para o usuário autenticado.")
    public ResponseEntity<ResumoDTO> insertMyResumo() {
        ResumoDTO newDto = resumoService.insertMyResumo();
        return ResponseEntity.ok().body(newDto);
    }

    @DeleteMapping(value = "/meus/{id}")
    @Operation(summary = "[USER] Deleta um dos meus resumos", description = "Remove um resumo existente do usuário autenticado a partir do seu ID.")
    public ResponseEntity<Void> deleteMyResumo(@PathVariable UUID id) {
        resumoService.deleteMyResumo(id);
        return ResponseEntity.noContent().build();
    }
}