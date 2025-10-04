package com.mentoria.back_end_mentoria.usuario.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public class Email {

    private String email;

    public Email() {
    }

    public Email(String email) {
        if (isEmailValido(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("O email deve ter um dominio valido");
        }
    }

    public String getEmail() {
        return email;
    }

    private boolean isEmailValido(String email) {
        if (email == null) return false;
        return email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
}