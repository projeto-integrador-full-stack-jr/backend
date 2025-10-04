package com.mentoria.back_end_mentoria.nota;

import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_nota")
public class Nota implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notaId;

    @ManyToOne
    @JoinColumn(name = "perfilProfissional_id", nullable = false)
    private PerfilProfissional perfilProfissional;

    @Embedded
    private Titulo titulo;

    @Embedded
    private Conteudo conteudo;

    public Nota() {
    }

    public Nota(UUID notaId, PerfilProfissional perfilProfissional, Titulo titulo, Conteudo conteudo) {
        this.notaId = notaId;
        this.perfilProfissional = perfilProfissional;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public UUID getNotaId() {
        return notaId;
    }

    public void setNotaId(UUID notaId) {
        this.notaId = notaId;
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