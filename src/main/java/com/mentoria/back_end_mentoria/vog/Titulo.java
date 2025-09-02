package com.mentoria.back_end_mentoria.vog;

public class Titulo {

    private String titulo;

    public Titulo() {
    }

    public Titulo(String titulo) {
        if (!isTituloValido(titulo)) {
            throw new IllegalArgumentException("O título deve ter pelo menos 1 caractere.");
        }
        this.titulo = titulo;
    }

    private boolean isTituloValido(String titulo) {
        if (titulo == null) return false;
        return titulo.trim().length() > 0;
    }

    public String getTitulo() {
        return titulo;
    }
}
