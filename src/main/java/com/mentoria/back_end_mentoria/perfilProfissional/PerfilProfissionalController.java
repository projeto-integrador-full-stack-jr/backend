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
@Tag(name = "Perfis Profissionais - Acesso Pessoal", description = "Endpoints para gerenciamento do perfil profissional do próprio usuário.")
public class PerfilProfissionalController {

    @Autowired
    private PerfilProfissionalService perfilProfissionalService;

    @GetMapping
    @Operation(summary = "[ADMIN] Lista todos os perfis", description = "Retorna uma lista de todos os perfis profissionais cadastrados. Requer perfil de ADMIN.", tags = {"Perfis Profissionais - Admin"})
    public ResponseEntity<List<PerfilProfissionalDTO>> findAll() {
        List<PerfilProfissionalDTO> lista = perfilProfissionalService.findAll();
        return ResponseEntity.ok().body(lista);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Busca um perfil por ID", description = "Retorna os detalhes de um perfil profissional a partir do seu ID. Requer perfil de ADMIN.", tags = {"Perfis Profissionais - Admin"})
    public ResponseEntity<PerfilProfissionalDTO> findById(@PathVariable UUID id) {
        PerfilProfissionalDTO dto = perfilProfissionalService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    @Operation(summary = "[ADMIN] Cria um novo perfil", description = "Cria um novo perfil profissional associado a um usuário. Requer perfil de ADMIN.", tags = {"Perfis Profissionais - Admin"})
    public ResponseEntity<PerfilProfissionalDTO> insert(@RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.insert(dto);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Atualiza um perfil", description = "Atualiza os dados de um perfil profissional a partir do seu ID. Requer perfil de ADMIN.", tags = {"Perfis Profissionais - Admin"})
    public ResponseEntity<PerfilProfissionalDTO> update(@PathVariable UUID id, @RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "[ADMIN] Deleta um perfil", description = "Remove um perfil profissional do sistema a partir do seu ID. Requer perfil de ADMIN.", tags = {"Perfis Profissionais - Admin"})
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        perfilProfissionalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Meus Endpoints

    @GetMapping(value = "/meu")
    @Operation(summary = "[USER] Busca o meu perfil", description = "Retorna o perfil profissional completo associado ao usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil profissional retornado com sucesso.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PerfilProfissionalDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de Perfil Profissional",
                                    value = """
                                            {
                                              "perfilId": "c1d2e3f4-g5h6-7890-1234-567890abcdef",
                                              "usuarioId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
                                              "nomeUsuario": "Nome do Usuário de Exemplo",
                                              "cargo": "Desenvolvedor de Software Pleno",
                                              "carreira": "Engenharia de Software",
                                              "experiencia": "5 anos de experiência com desenvolvimento back-end utilizando Java e Spring.",
                                              "objetivoPrincipal": "Tornar-se um Arquiteto de Software em 3 anos.",
                                              "resumos": [],
                                              "metas": [],
                                              "notas": []
                                            }
                                            """
                            ))
            }),
            @ApiResponse(responseCode = "403", description = "Acesso negado. O usuário não está autenticado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Perfil profissional não encontrado para o usuário.", content = @Content)
    })
    public ResponseEntity<PerfilProfissionalDTO> findMyProfile() {
        PerfilProfissionalDTO dto = perfilProfissionalService.findMyProfile();
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping(value = "/meu")
    @Operation(summary = "[USER] Cria ou atualiza o meu perfil",
            description = "Cria um novo perfil profissional para o usuário autenticado, ou atualiza o perfil existente com os dados fornecidos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do perfil para criar ou atualizar.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PerfilProfissionalDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de Requisição",
                                    value = """
                                            {
                                              "perfilId": "c1d2e3f4-g5h6-7890-1234-567890abcdef",
                                              "usuarioId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
                                              "nomeUsuario": "Nome do Usuário de Exemplo",
                                              "cargo": "Desenvolvedor de Software Sênior",
                                              "carreira": "Engenharia de Software",
                                              "experiencia": "6 anos de experiência com desenvolvimento back-end utilizando Java e Spring, e 1 ano com liderança técnica.",
                                              "objetivoPrincipal": "Tornar-se um Arquiteto de Software em 2 anos.",
                                              "resumos": [],
                                              "metas": [],
                                              "notas": []
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil criado ou atualizado com sucesso.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PerfilProfissionalDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de Resposta",
                                    value = """
                                            {
                                              "perfilId": "c1d2e3f4-g5h6-7890-1234-567890abcdef",
                                              "usuarioId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
                                              "nomeUsuario": "Nome do Usuário de Exemplo",
                                              "cargo": "Desenvolvedor de Software Sênior",
                                              "carreira": "Engenharia de Software",
                                              "experiencia": "6 anos de experiência com desenvolvimento back-end utilizando Java e Spring, e 1 ano com liderança técnica.",
                                              "objetivoPrincipal": "Tornar-se um Arquiteto de Software em 2 anos.",
                                              "resumos": [],
                                              "metas": [],
                                              "notas": []
                                            }
                                            """
                            ))
            }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para o perfil.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    public ResponseEntity<PerfilProfissionalDTO> createOrUpdateMyProfile(@RequestBody PerfilProfissionalDTO dto) {
        dto = perfilProfissionalService.createOrUpdateMyProfile(dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/meu")
    @Operation(summary = "[USER] Deleta o meu perfil", description = "Remove o perfil profissional associado ao usuário autenticado. Esta ação não pode ser desfeita.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Perfil deletado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado para deleção.", content = @Content)
    })
    public ResponseEntity<Void> deleteMyProfile() {
        perfilProfissionalService.deleteMyProfile();
        return ResponseEntity.noContent().build();
    }
}
