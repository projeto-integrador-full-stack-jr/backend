package com.mentoria.back_end_mentoria.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mentoria.back_end_mentoria.usuario.vo.Email;
import com.mentoria.back_end_mentoria.usuario.vo.Senha;
import com.mentoria.back_end_mentoria.usuario.vo.UserRole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_usuario", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID usuarioId;

    @Embedded
    @Column(unique = true)
    private Email email;

    @Embedded
    private Senha senha;

    private UserRole acesso;

    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant createdAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant updatedAt;

    public Usuario() {
    }

    public Usuario(Email email, Senha senha, UserRole acesso) {
        this.acesso = acesso;
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

    public UserRole getAcesso() {
        return acesso;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setSenha(Senha senha) {
        this.senha = senha;
    }

    public void setAcesso(UserRole acesso) {
        this.acesso = acesso;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.acesso == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() {
        return this.senha.getValor();
    }

    @Override
    public String getUsername() {
        return this.email.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}