package com.mentoria.back_end_mentoria.perfilProfissional;

import com.mentoria.back_end_mentoria.meta.MetaDTO;
import com.mentoria.back_end_mentoria.nota.NotaDTO;
import com.mentoria.back_end_mentoria.resumo.ResumoDTO;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PerfilProfissionalDTO implements Serializable {

    private UUID perfilId;
    private UUID usuarioId;
    private String nomeUsuario;
    private String cargo;
    private String carreira;
    private String experiencia;
    private String objetivoPrincipal;
    private List<ResumoDTO> resumos;
    private List<MetaDTO> metas;
    private List<NotaDTO> notas;

    public PerfilProfissionalDTO() {
    }

    public PerfilProfissionalDTO(PerfilProfissional entity) {
        this.perfilId = entity.getPerfilId();
        this.usuarioId = (entity.getUsuario() != null) ? entity.getUsuario().getUsuarioId() : null;
        this.nomeUsuario = entity.getNomeUsuario();
        this.cargo = entity.getCargo();
        this.carreira = entity.getCarreira();
        this.experiencia = entity.getExperiencia();
        this.objetivoPrincipal = entity.getObjetivoPrincipal();
        this.resumos = entity.getResumos().stream().map(ResumoDTO::new).collect(Collectors.toList());
        this.notas = entity.getNotas().stream().map(NotaDTO::new).collect(Collectors.toList());
        this.metas = entity.getMetas().stream().map(MetaDTO::new).collect(Collectors.toList());
    }

    // Getters e Setters para todos os campos

    public UUID getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(UUID perfilId) {
        this.perfilId = perfilId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
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

    public List<ResumoDTO> getResumos() {
        return resumos;
    }

    public void setResumos(List<ResumoDTO> resumos) {
        this.resumos = resumos;
    }

    public List<MetaDTO> getMetas() {
        return metas;
    }

    public void setMetas(List<MetaDTO> metas) {
        this.metas = metas;
    }

    public List<NotaDTO> getNotas() {
        return notas;
    }

    public void setNotas(List<NotaDTO> notas) {
        this.notas = notas;
    }
}