package com.mentoria.back_end_mentoria.meta;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.usuario.Usuario;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MetaService {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Transactional(readOnly = true)
    public List<MetaDTO> findAll() {
        return metaRepository.findAll().stream().map(MetaDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MetaDTO findById(UUID id) {
        Meta entity = metaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada"));
        return new MetaDTO(entity);
    }

    @Transactional
    public MetaDTO insert(MetaDTO dto) {
        Meta entity = new Meta();
        copyDtoToEntity(dto, entity);
        entity = metaRepository.save(entity);
        return new MetaDTO(entity);
    }

    @Transactional
    public MetaDTO update(UUID id, MetaDTO dto) {
        try {
            Meta entity = metaRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = metaRepository.save(entity);
            return new MetaDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Meta não encontrada com o id: " + id);
        }
    }

    public void delete(UUID id) {
        if (!metaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meta não encontrada com o id: " + id);
        }
        metaRepository.deleteById(id);
    }

    private Usuario getUsuarioLogado() {    
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void copyDtoToEntity(MetaDTO dto, Meta entity) {
        entity.setTitulo(dto.getTitulo());
        entity.setPrazo(dto.getPrazo());
        entity.setStatusMeta(dto.getStatusMeta());

        if (dto.getPerfilProfissionalId() != null) {
            entity.setPerfilProfissional(perfilProfissionalRepository.findById(dto.getPerfilProfissionalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Perfil Profissional não encontrado para a meta")));
        }
    }
}