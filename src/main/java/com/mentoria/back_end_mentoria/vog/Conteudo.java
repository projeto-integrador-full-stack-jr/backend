package com.mentoria.back_end_mentoria.vog;

import jakarta.persistence.Column;

public class Conteudo {
    @Column(length = 10000)
    private String conteudo;

    public Conteudo() {
    }

    public Conteudo(String value) {
        this.conteudo = value;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
