package com.mentoria.back_end_mentoria.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // CORREÇÃO: Alterado de UserDetails e findByEmailEmail
    Usuario findByEmail_Email(String email);

}