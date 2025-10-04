package com.mentoria.back_end_mentoria.meta;

import com.mentoria.back_end_mentoria.meta.vo.Prazo;
import com.mentoria.back_end_mentoria.meta.vo.StatusMeta;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.vog.Titulo;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_meta")
public class Meta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID metaId;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "perfilProfissional_id", nullable = false)
    private PerfilProfissional perfilProfissional;

    @Embedded
    private Titulo titulo;

    @Embedded
    private Prazo prazo;

    private Integer statusMeta;

    protected Meta() {
    }

    public Meta(PerfilProfissional perfilProfissional, Titulo titulo, Prazo prazo, StatusMeta statusMeta) {
        this.perfilProfissional = perfilProfissional;
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

    public PerfilProfissional getPerfilProfissional() {
        return perfilProfissional;
    }

    public void setPerfilProfissional(PerfilProfissional perfilProfissional) {
        this.perfilProfissional = perfilProfissional;
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