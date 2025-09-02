package com.mentoria.back_end_mentoria.meta.vo;

import java.time.Instant;

public class Prazo {

    private Instant prazo;

    public Prazo() {
    }

    public Prazo(Instant prazo) {
        if (prazo == null) {
            throw new IllegalArgumentException("O prazo não pode ser nulo.");
        }
        if (prazo.isBefore(Instant.now())) {
            throw new IllegalArgumentException("O prazo não pode estar no passado.");
        }
        this.prazo = prazo;
    }

    public Instant getPrazo() {
        return prazo;
    }
}
