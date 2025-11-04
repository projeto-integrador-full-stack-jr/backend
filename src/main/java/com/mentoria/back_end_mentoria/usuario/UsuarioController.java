package com.mentoria.back_end_mentoria.usuario;

import com.mentoria.back_end_mentoria.security.TokenService;
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
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @Tag(name = "Usuários Admin")
    @GetMapping(value = "/listar")
    @Operation(summary = "[ADMIN] Lista todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados no sistema. Requer perfil de ADMIN.")
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<Usuario> lista = usuarioService.findAll();
        List<UsuarioDTO> listaDTO = lista.stream().map(UsuarioDTO::new).toList();
        return ResponseEntity.ok().body(listaDTO);
    }

    @Tag(name = "Usuários Admin")
    @GetMapping("/{id}")
    @Operation(summary = "[ADMIN] Busca um usuário por ID", description = "Retorna os detalhes de um usuário específico a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable UUID id) {
        Usuario usuario = usuarioService.findById(id);
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        return ResponseEntity.ok().body(usuarioDTO);
    }

    @Tag(name = "Acesso")
    @PostMapping
    @Operation(summary = "[PÚBLICO] Cria um novo usuário", description = "Cria um novo usuário no sistema. Este endpoint é público.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemplo de criação de usuário",
                                    value = "{\n    \"email\": \"string\",\n    \"senha\": \"string\"\n}"
                            )
                    )
            )
    )
    public ResponseEntity<UsuarioDTO> save(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.save(usuario);
        UsuarioDTO usuarioDTO = new UsuarioDTO(novoUsuario);
        return ResponseEntity.ok().body(usuarioDTO);
    }

    @Tag(name = "Usuários Admin")
    @PutMapping("/{id}")
    @Operation(summary = "[ADMIN] Atualiza um usuário", description = "Atualiza os dados de um usuário existente a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<UsuarioDTO> update(@PathVariable UUID id, @RequestBody UsuarioDTO dto) {
        Usuario usuarioAtualizado = usuarioService.update(id, dto);
        return ResponseEntity.ok(new UsuarioDTO(usuarioAtualizado));
    }

    @Tag(name = "Usuários Admin")
    @DeleteMapping("/{id}")
    @Operation(summary = "[ADMIN] Deleta um usuário", description = "Remove um usuário do sistema a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Tag(name = "Usuários User")
    @GetMapping("/eu")
    @Operation(summary = "[USER] Busca os dados do meu usuário", description = "Retorna os detalhes completos do usuário que está autenticado na sessão atual.")
    public ResponseEntity<UsuarioDTO> getMyUser() {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        return ResponseEntity.ok(new UsuarioDTO(usuarioLogado));
    }

    @Tag(name = "Usuários User")
    @PutMapping("/eu")
    @Operation(summary = "[USER] Atualiza os dados do meu usuário", description = "Atualiza os dados do usuário autenticado. O ID é obtido pelo token de autenticação.")
    public ResponseEntity<AuthResponseDTO> updateMyUser(@RequestBody UsuarioDTO dto) {
        Usuario usuarioAtualizado = usuarioService.updateMyUser(dto);
        String novoToken = tokenService.gerarToken(usuarioAtualizado);
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioAtualizado);

        AuthResponseDTO response = new AuthResponseDTO(usuarioDTO, novoToken);

        return ResponseEntity.ok(response);
    }

    @Tag(name = "Usuários User")
    @DeleteMapping("/eu")
    @Operation(summary = "[USER] Deleta o meu usuário", description = "Remove permanentemente o usuário autenticado do sistema. Esta ação não pode ser desfeita.")
    public ResponseEntity<Void> deleteMyUser() {
        usuarioService.deleteMyUser();
        return ResponseEntity.noContent().build();
    }

    public record AuthResponseDTO(UsuarioDTO usuario, String token) {}
}