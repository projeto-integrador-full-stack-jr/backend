package com.mentoria.back_end_mentoria.resumo;

import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;

import java.io.Serializable;
import java.util.UUID;

public class ResumoDTO implements Serializable {

    private UUID resumoId;
    private UUID perfilProfissionalId;
    private String titulo;
    private String conteudo;

    public ResumoDTO() {
    }

    public ResumoDTO(Resumo entity) {
        this.resumoId = entity.getResumoId();
        this.perfilProfissionalId = (entity.getPerfilProfissional() != null) ? entity.getPerfilProfissional().getPerfilId() : null;
        this.titulo = entity.getTitulo().getTitulo();
        this.conteudo = entity.getConteudo().getConteudo();
    }

    public ResumoDTO(UUID resumoId, Titulo titulo, Conteudo conteudo) {
        this.resumoId = resumoId;
        this.titulo = titulo.getTitulo();
        this.conteudo = conteudo.getConteudo();
    }

    public UUID getResumoId() {
        return resumoId;
    }

    public void setResumoId(UUID resumoId) {
        this.resumoId = resumoId;
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