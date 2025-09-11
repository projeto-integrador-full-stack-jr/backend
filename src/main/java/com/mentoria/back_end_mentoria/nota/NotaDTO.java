package com.mentoria.back_end_mentoria.nota;

import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;

import java.io.Serializable;
import java.util.UUID;

public class NotaDTO implements Serializable {

    private UUID notaId;
    private UUID usuarioId;
    private String titulo;
    private String conteudo;

    public NotaDTO() {
    }

    public NotaDTO(Nota entity) {
        this.notaId = entity.getNotaId();
        this.usuarioId = (entity.getUsuario() != null) ? entity.getUsuario().getUsuarioId() : null;
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

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
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