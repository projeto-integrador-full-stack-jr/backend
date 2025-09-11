package com.mentoria.back_end_mentoria.nota;

import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;

import java.io.Serializable;
import java.util.UUID;

public class NotaDTO implements Serializable {

    private UUID notaId;
    private UUID perfilProfissionalId;
    private String titulo;
    private String conteudo;

    public NotaDTO() {
    }

    public NotaDTO(Nota entity) {
        this.notaId = entity.getNotaId();
        this.perfilProfissionalId = (entity.getPerfilProfissional() != null) ? entity.getPerfilProfissional().getPerfilId() : null;
        this.titulo = entity.getTitulo().getTitulo();
        this.conteudo = entity.getConteudo().getConteudo();
    }

    public NotaDTO(UUID notaId, Titulo titulo, Conteudo conteudo) {
        this.notaId = notaId;
        this.titulo = titulo.getTitulo();
        this.conteudo = conteudo.getConteudo();
    }

    public UUID getNotaId() {
        return notaId;
    }

    public void setNotaId(UUID notaId) {
        this.notaId = notaId;
    }

    public UUID getPerfilProfissionalId() {
        return perfilProfissionalId;
    }

    public void setPerfilProfissionalId(UUID perfilProfissionalId) {
        this.perfilProfissionalId = perfilProfissionalId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}