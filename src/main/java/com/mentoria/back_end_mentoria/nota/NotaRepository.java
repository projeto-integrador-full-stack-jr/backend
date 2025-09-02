package com.mentoria.back_end_mentoria.nota;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotaRepository extends JpaRepository<Nota, UUID> {
}
