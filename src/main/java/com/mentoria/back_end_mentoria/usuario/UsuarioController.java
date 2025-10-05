package com.mentoria.back_end_mentoria.usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de Usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/listar")
    @Operation(summary = "Lista todos os usuários (ADMIN)", description = "Retorna uma lista de todos os usuários cadastrados no sistema. Requer perfil de ADMIN.")
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<Usuario> lista = usuarioService.findAll();
        List<UsuarioDTO> listaDTO = lista.stream().map(UsuarioDTO::new).toList();
        return ResponseEntity.ok().body(listaDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário por ID (ADMIN)", description = "Retorna os detalhes de um usuário específico a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable UUID id) {
        Usuario usuario = usuarioService.findById(id);
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        return ResponseEntity.ok().body(usuarioDTO);
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário (Público)", description = "Cria um novo usuário no sistema. Este endpoint é público.")
    public ResponseEntity<UsuarioDTO> save(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.save(usuario);
        UsuarioDTO usuarioDTO = new UsuarioDTO(novoUsuario);
        return ResponseEntity.ok().body(usuarioDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um usuário (ADMIN)", description = "Atualiza os dados de um usuário existente a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<UsuarioDTO> update(@PathVariable UUID id, @RequestBody UsuarioDTO dto) {
        Usuario usuarioAtualizado = usuarioService.update(id, dto);
        return ResponseEntity.ok(new UsuarioDTO(usuarioAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um usuário (ADMIN)", description = "Remove um usuário do sistema a partir do seu ID. Requer perfil de ADMIN.")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/eu")
    @Operation(summary = "Busca os dados do meu usuário", description = "Retorna os detalhes do usuário autenticado.")
    public ResponseEntity<UsuarioDTO> getMyUser() {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        return ResponseEntity.ok(new UsuarioDTO(usuarioLogado));
    }

    @PutMapping("/eu")
    @Operation(summary = "Atualiza os dados do meu usuário", description = "Atualiza os dados do usuário autenticado.")
    public ResponseEntity<UsuarioDTO> updateMyUser(@RequestBody UsuarioDTO dto) {
        Usuario usuarioAtualizado = usuarioService.updateMyUser(dto);
        return ResponseEntity.ok(new UsuarioDTO(usuarioAtualizado));
    }

    @DeleteMapping("/eu")
    @Operation(summary = "Deleta o meu usuário", description = "Remove o usuário autenticado do sistema.")
    public ResponseEntity<Void> deleteMyUser() {
        usuarioService.deleteMyUser();
        return ResponseEntity.noContent().build();
    }
}