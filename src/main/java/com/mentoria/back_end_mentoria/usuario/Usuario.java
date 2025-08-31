package com.mentoria.back_end_mentoria.usuario;

import com.mentoria.back_end_mentoria.usuario.vo.Email;
import com.mentoria.back_end_mentoria.usuario.vo.Senha;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    private UUID usuarioId;

    @Embedded
    private Email email;

    @Embedded
    private Senha senha;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public Usuario(UUID usuarioId, Email email, Senha senha) {
        this.usuarioId = usuarioId;
        this.email = email;
        this.senha = senha;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public Email getEmail() {
        return email;
    }

    public Senha getSenha() {
        return senha;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
