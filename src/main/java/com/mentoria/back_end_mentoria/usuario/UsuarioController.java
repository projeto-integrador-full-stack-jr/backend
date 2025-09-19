package com.mentoria.back_end_mentoria.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(value = "/listar")
    public ResponseEntity<List<UsuarioDTO>> findAll() {

        List<Usuario> lista = usuarioService.findAll();
        
        List<UsuarioDTO> listaDTO = lista.stream().map(UsuarioDTO::new).toList();
        
        return ResponseEntity.ok().body(listaDTO);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> save(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.save(usuario);

        UsuarioDTO usuarioDTO = new UsuarioDTO(novoUsuario);

        return ResponseEntity.ok().body(usuarioDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable UUID id) {
        Usuario usuario = usuarioService.findById(id);

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);

        return ResponseEntity.ok().body(usuarioDTO);
    }
    
    @GetMapping("/eu")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        return ResponseEntity.ok(new UsuarioDTO(usuarioLogado));
    }
}
