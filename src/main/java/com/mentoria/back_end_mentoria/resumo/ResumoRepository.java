package com.mentoria.back_end_mentoria.resumo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResumoRepository extends JpaRepository<Resumo, UUID> {
}
