package com.mentoria.back_end_mentoria.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails; // Import não é mais necessário para o método

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // CORREÇÃO: Mude o tipo de retorno de UserDetails para Usuario
    Usuario findByEmailEmail(String email);

}