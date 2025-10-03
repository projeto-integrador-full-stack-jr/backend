package com.mentoria.back_end_mentoria.usuario;

import java.util.UUID;

import com.mentoria.back_end_mentoria.usuario.vo.UserRole;

public class UsuarioDTO {

    private UUID id;
    private String email;
    private String senha;
    private UserRole acesso;

    public UsuarioDTO(Usuario entity) {
        this.id = entity.getUsuarioId();
        this.email = entity.getEmail().getEmail();
        this.senha = entity.getPassword();
        this.acesso = entity.getAcesso();
    }

    public UUID getId() {
        return id;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getAcesso() {
        return acesso;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAcesso(UserRole acesso) {
        this.acesso = acesso;
    }
}
