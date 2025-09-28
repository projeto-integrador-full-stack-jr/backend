package com.mentoria.back_end_mentoria.resumo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_resumo")
public class Resumo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID resumoId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "perfilProfissional_id")
    private PerfilProfissional perfilProfissional;

    @Embedded
    private Titulo titulo;

    @Embedded
    private Conteudo conteudo;

    public Resumo() {
    }

    public Resumo(UUID resumoId, PerfilProfissional perfilProfissional, Titulo titulo, Conteudo conteudo) {
        this.resumoId = resumoId;
        this.perfilProfissional = perfilProfissional;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    // Getters and Setters

    public UUID getResumoId() {
        return resumoId;
    }

    public void setResumoId(UUID resumoId) {
        this.resumoId = resumoId;
    }

    public PerfilProfissional getPerfilProfissional() {
        return perfilProfissional;
    }

    public void setPerfilProfissional(PerfilProfissional perfilProfissional) {
        this.perfilProfissional = perfilProfissional;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }

    public void setConteudo(Conteudo conteudo) {
        this.conteudo = conteudo;
    }
}