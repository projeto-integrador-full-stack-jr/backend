package com.mentoria.back_end_mentoria.perfilProfissional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilProfissionalService {

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    public List<PerfilProfissional> findAll() {
        return perfilProfissionalRepository.findAll();
    }
}
