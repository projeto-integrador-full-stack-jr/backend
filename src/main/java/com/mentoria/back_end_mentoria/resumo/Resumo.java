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
    private UUID resumoId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "perfilProfissional_id")
    private PerfilProfissional perfilId;

    @Embedded
    private Titulo titulo;

    @Embedded
    private Conteudo conteudo;

    public Resumo() {
    }

    public Resumo(UUID resumoId, PerfilProfissional perfilId, Titulo titulo, Conteudo conteudo) {
        this.resumoId = resumoId;
        this.perfilId = perfilId;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public UUID getResumoId() {
        return resumoId;
    }

    public void setResumoId(UUID resumoId) {
        this.resumoId = resumoId;
    }

    public PerfilProfissional getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(PerfilProfissional perfilId) {
        this.perfilId = perfilId;
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
