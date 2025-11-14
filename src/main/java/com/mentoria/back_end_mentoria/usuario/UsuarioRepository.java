package com.mentoria.back_end_mentoria.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // O nome correto para consultar o campo 'email' dentro do @Embedded 'Email'
    Usuario findByEmail_Email(String email);

}