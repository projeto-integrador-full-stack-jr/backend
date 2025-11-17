package com.mentoria.back_end_mentoria.meta.vo;

import com.mentoria.back_end_mentoria.meta.Meta;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record MetaRequest(
        @NotBlank(message = "O título é obrigatório.")
        String titulo,

        @NotNull(message = "O prazo é obrigatório.")
        @Future(message = "O prazo deve estar no futuro.")
        Instant prazo
) {
    public MetaRequest(Meta entity) {
        this(
                entity.getTitulo(),
                entity.getPrazo()
        );
    }
}
