package com.mentoria.back_end_mentoria.nota;

import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_nota")
public class Nota implements Serializable {

    @Id
    private UUID notaId;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Embedded
    private Titulo titulo;

    @Embedded
    private Conteudo conteudo;

    public Nota() {
    }

    public Nota(UUID notaId, Usuario usuarioId, Titulo titulo, Conteudo conteudo) {
        this.notaId = notaId;
        this.usuario = usuarioId;
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public UUID getNotaId() {
        return notaId;
    }

    public void setNotaId(UUID notaId) {
        this.notaId = notaId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
