package com.mentoria.back_end_mentoria.meta.vo;

import com.mentoria.back_end_mentoria.meta.Meta;

import java.time.Instant;
import java.util.UUID;

public record MetaResponse(
        UUID metaId,
        String titulo,
        Instant prazo,
        StatusMeta statusMeta
) {

    public MetaResponse(Meta entity) {
        this(
                entity.getMetaId(),
                entity.getTitulo(),
                entity.getPrazo(),
                entity.getStatusMeta()
        );
    }
}
