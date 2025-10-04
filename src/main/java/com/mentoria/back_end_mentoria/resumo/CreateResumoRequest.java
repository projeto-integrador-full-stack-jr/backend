package com.mentoria.back_end_mentoria.resumo;

import java.util.UUID;

public class CreateResumoRequest {

    private UUID perfilProfissionalId;

    public UUID getPerfilProfissionalId() {
        return perfilProfissionalId;
    }

    public void setPerfilProfissionalId(UUID perfilProfissionalId) {
        this.perfilProfissionalId = perfilProfissionalId;
    }
}
