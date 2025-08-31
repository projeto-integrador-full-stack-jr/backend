package com.mentoria.back_end_mentoria.meta;

import com.mentoria.back_end_mentoria.meta.vo.Prazo;
import com.mentoria.back_end_mentoria.meta.vo.StatusMeta;
import com.mentoria.back_end_mentoria.meta.vo.Titulo;
import com.mentoria.back_end_mentoria.usuario.Usuario;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_meta")
public class Meta implements Serializable {

    @Id
    private UUID metaId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Embedded
    private Titulo titulo;

    @Embedded
    private Prazo prazo;

    private Integer statusMeta;

    protected Meta() {
    }

    public Meta(UUID metaId, Usuario usuario, Titulo titulo, Prazo prazo, StatusMeta statusMeta) {
        this.metaId = metaId;
        this.usuario = usuario;
        this.titulo = titulo;
        this.prazo = prazo;
        setStatusMeta(statusMeta);
    }

    public UUID getMetaId() {
        return metaId;
    }

    public void setMetaId(UUID metaId) {
        this.metaId = metaId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo.getTitulo();
    }

    public void setTitulo(String titulo) {
        this.titulo = new Titulo(titulo);
    }

    public Instant getPrazo() {
        return prazo.getPrazo();
    }

    public void setPrazo(Instant prazo) {
        this.prazo = new Prazo(prazo);
    }

    public StatusMeta getStatusMeta() {
        return StatusMeta.valueOf(statusMeta);
    }

    public void setStatusMeta(StatusMeta statusMeta) {
        if(statusMeta != null) {
            this.statusMeta = statusMeta.getCode();
        }
    }
}
