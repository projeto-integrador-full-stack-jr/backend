package com.mentoria.back_end_mentoria.meta;

import com.mentoria.back_end_mentoria.meta.vo.StatusMeta;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class MetaDTO implements Serializable {

    private UUID metaId;
    private UUID usuarioId;
    private String titulo;
    private Instant prazo;
    private StatusMeta statusMeta;

    public MetaDTO() {
    }

    public MetaDTO(Meta entity) {
        this.metaId = entity.getMetaId();
        this.usuarioId = entity.getUsuario().getUsuarioId();
        this.titulo = entity.getTitulo();
        this.prazo = entity.getPrazo();
        this.statusMeta = entity.getStatusMeta();
    }



    public UUID getMetaId() {
        return metaId;
    }

    public void setMetaId(UUID metaId) {
        this.metaId = metaId;
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

    public Instant getPrazo() {
        return prazo;
    }

    public void setPrazo(Instant prazo) {
        this.prazo = prazo;
    }

    public StatusMeta getStatusMeta() {
        return statusMeta;
    }

    public void setStatusMeta(StatusMeta statusMeta) {
        this.statusMeta = statusMeta;
    }
}