package com.mentoria.back_end_mentoria.usuario.vo;

public class Email {

    private String email;

    public Email(String email) {
        if (isEmailValido(email)) {
            this.email = email;
        }
    }

    public String getEmail() {
        return email;
    }

    private boolean isEmailValido(String email) {
        if (email == null) return false;
        try {
            return email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}