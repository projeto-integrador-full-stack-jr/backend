package com.mentoria.back_end_mentoria.perfilProfissional;

import com.mentoria.back_end_mentoria.usuario.Usuario;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_perfilProfissional")
public class PerfilProfissional implements Serializable {

    @Id
    private UUID perfilId;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String nomeUsuario;
    private String cargo;
    private String carreira;
    private String experiencia;
    private String objetivoPrincipal;

    public PerfilProfissional() {
    }

    public PerfilProfissional(UUID perfilId, Usuario usuario, String nomeUsuario, String cargo, String carreira, String experiencia, String objetivoPrincipal) {
        this.perfilId = perfilId;
        this.usuario = usuario;
        this.nomeUsuario = nomeUsuario;
        this.cargo = cargo;
        this.carreira = carreira;
        this.experiencia = experiencia;
        this.objetivoPrincipal = objetivoPrincipal;
    }

    public UUID getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(UUID perfilId) {
        this.perfilId = perfilId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCarreira() {
        return carreira;
    }

    public void setCarreira(String carreira) {
        this.carreira = carreira;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getObjetivoPrincipal() {
        return objetivoPrincipal;
    }

    public void setObjetivoPrincipal(String objetivoPrincipal) {
        this.objetivoPrincipal = objetivoPrincipal;
    }
}
