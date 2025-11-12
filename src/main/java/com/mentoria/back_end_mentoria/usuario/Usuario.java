package com.mentoria.back_end_mentoria.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mentoria.back_end_mentoria.usuario.vo.Email;
import com.mentoria.back_end_mentoria.usuario.vo.Senha;
import com.mentoria.back_end_mentoria.usuario.vo.UserRole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User; // NOVO IMPORT

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "tb_usuario", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario implements Serializable, UserDetails, OAuth2User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID usuarioId;

    @Embedded
    @Column(unique = true)
    private Email email;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "senha", nullable = true))
    private Senha senha;

    private UserRole acesso;

    private String imageUrl;

    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant createdAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant updatedAt;

    // NOVO CAMPO: Atributos do OAuth2
    @Transient // Não persistir no banco
    private Map<String, Object> attributes;

    public Usuario() {
    }

    public Usuario(Email email, Senha senha, UserRole acesso) {
        this.acesso = acesso;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(Email email, Senha senha, UserRole acesso, String imageUrl) {
        this.acesso = acesso;
        this.email = email;
        this.senha = senha;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        // MODIFICADO: Retorna null se a senha não existir (caso do OAuth2)
        return (this.senha != null) ? this.senha.getValor() : null;
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

    // --- NOVOS MÉTODOS OAuth2User ---

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        // O Spring usa isso como ID do usuário OAuth. O e-mail é um bom candidato.
        return this.email.getEmail();
    }
}