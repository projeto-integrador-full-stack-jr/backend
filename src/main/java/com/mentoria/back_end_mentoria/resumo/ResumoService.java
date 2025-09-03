package com.mentoria.back_end_mentoria.resumo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumoService {

    @Autowired
    private ResumoRepository  resumoRepository;

    public List<Resumo> findAll() {
        return resumoRepository.findAll();
    }
}
