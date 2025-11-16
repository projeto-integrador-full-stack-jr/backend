package com.mentoria.back_end_mentoria.meta;

import com.mentoria.back_end_mentoria.meta.vo.StatusMeta;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class MetaDTO implements Serializable {

    private UUID metaId;
    private UUID perfilProfissionalId;
    private String titulo;
    private Instant prazo;
    private StatusMeta statusMeta;

    public MetaDTO() {
    }

    public MetaDTO(Meta entity) {
        this.metaId = entity.getMetaId();
        this.perfilProfissionalId = (entity.getPerfilProfissional() != null) ? entity.getPerfilProfissional().getPerfilId() : null;
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