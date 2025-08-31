package com.mentoria.back_end_mentoria.meta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MetaRepository extends JpaRepository<Meta, UUID> {
}
