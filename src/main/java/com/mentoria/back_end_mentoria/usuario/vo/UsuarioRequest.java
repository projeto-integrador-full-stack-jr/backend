package com.mentoria.back_end_mentoria.usuario.vo;

import java.util.UUID;

public class UsuarioRequest {

    private String email;
    private String senha;

    public UsuarioRequest() {
    }

    public UsuarioRequest(UUID id, String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
