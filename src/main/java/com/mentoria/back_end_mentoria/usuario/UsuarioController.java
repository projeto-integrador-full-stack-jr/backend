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
@Tag(name = "Usuários - Acesso Pessoal", description = "Endpoints para gerenciamento dos dados do próprio usuário.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/listar")
    @Operation(summary = "[ADMIN] Lista todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados no sistema. Requer perfil de ADMIN.", tags = {"Usuários - Admin"})
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<Usuario> lista = usuarioService.findAll();
        List<UsuarioDTO> listaDTO = lista.stream().map(UsuarioDTO::new).toList();
        return ResponseEntity.ok().body(listaDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "[ADMIN] Busca um usuário por ID", description = "Retorna os detalhes de um usuário específico a partir do seu ID. Requer perfil de ADMIN.", tags = {"Usuários - Admin"})
    public ResponseEntity<UsuarioDTO> findById(@PathVariable UUID id) {
        Usuario usuario = usuarioService.findById(id);
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        return ResponseEntity.ok().body(usuarioDTO);
    }

    @PostMapping
    @Operation(summary = "[PÚBLICO] Cria um novo usuário", description = "Cria um novo usuário no sistema. Este endpoint é público.", tags = {"Usuários - Público"})
    public ResponseEntity<UsuarioDTO> save(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.save(usuario);
        UsuarioDTO usuarioDTO = new UsuarioDTO(novoUsuario);
        return ResponseEntity.ok().body(usuarioDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "[ADMIN] Atualiza um usuário", description = "Atualiza os dados de um usuário existente a partir do seu ID. Requer perfil de ADMIN.", tags = {"Usuários - Admin"})
    public ResponseEntity<UsuarioDTO> update(@PathVariable UUID id, @RequestBody UsuarioDTO dto) {
        Usuario usuarioAtualizado = usuarioService.update(id, dto);
        return ResponseEntity.ok(new UsuarioDTO(usuarioAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "[ADMIN] Deleta um usuário", description = "Remove um usuário do sistema a partir do seu ID. Requer perfil de ADMIN.", tags = {"Usuários - Admin"})
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/eu")
    @Operation(summary = "[USER] Busca os dados do meu usuário", description = "Retorna os detalhes do usuário autenticado.")
    public ResponseEntity<UsuarioDTO> getMyUser() {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        return ResponseEntity.ok(new UsuarioDTO(usuarioLogado));
    }

    @PutMapping("/eu")
    @Operation(summary = "[USER] Atualiza os dados do meu usuário", description = "Atualiza os dados do usuário autenticado.")
    public ResponseEntity<UsuarioDTO> updateMyUser(@RequestBody UsuarioDTO dto) {
        Usuario usuarioAtualizado = usuarioService.updateMyUser(dto);
        return ResponseEntity.ok(new UsuarioDTO(usuarioAtualizado));
    }

    @DeleteMapping("/eu")
    @Operation(summary = "[USER] Deleta o meu usuário", description = "Remove o usuário autenticado do sistema.")
    public ResponseEntity<Void> deleteMyUser() {
        usuarioService.deleteMyUser();
        return ResponseEntity.noContent().build();
    }
}
