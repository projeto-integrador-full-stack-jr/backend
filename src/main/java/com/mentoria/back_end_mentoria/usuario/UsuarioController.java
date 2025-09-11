package com.mentoria.back_end_mentoria.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {

        List<Usuario> lista = usuarioService.findAll();
        
        List<UsuarioDTO> listaDTO = lista.stream().map(UsuarioDTO::new).toList();
        
        return ResponseEntity.ok().body(listaDTO);
    }

    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.save(usuario);
        return ResponseEntity.ok().body(novoUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable UUID id) {
        Usuario usuario = usuarioService.findById(id);

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);

        return ResponseEntity.ok().body(usuarioDTO);
    }    
}
