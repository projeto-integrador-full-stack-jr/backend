package com.mentoria.back_end_mentoria.usuario;

import java.util.UUID;

public class UsuarioDTO {

    private UUID id;
    private String email;

    public UsuarioDTO(Usuario entity) {
        this.id = entity.getUsuarioId();
        this.email = entity.getEmail().getEmail();
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
