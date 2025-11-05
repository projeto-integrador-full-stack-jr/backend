package com.mentoria.back_end_mentoria.usuario.vo;

import com.mentoria.back_end_mentoria.usuario.Usuario;

import java.util.UUID;

public class UsuarioResponse {

    private UUID usuarioId;
    private String email;
    private UserRole acesso;
    private String imageUrl;

    public UsuarioResponse() {
    }

    public UsuarioResponse(Usuario usuario) {
        this.usuarioId = usuario.getUsuarioId();
        this.email = usuario.getEmail().getEmail();
        this.acesso = usuario.getAcesso();
        this.imageUrl = usuario.getImageUrl();
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getAcesso() {
        return acesso;
    }

    public void setAcesso(UserRole acesso) {
        this.acesso = acesso;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
