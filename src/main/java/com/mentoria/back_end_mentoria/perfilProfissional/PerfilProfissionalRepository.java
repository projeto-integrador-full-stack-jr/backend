package com.mentoria.back_end_mentoria.perfilProfissional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PerfilProfissionalRepository extends JpaRepository<PerfilProfissional, UUID> {
}
