package com.mentoria.back_end_mentoria.perfilProfissional;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class PerfilProfissionalService {

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Adicione este repositório para associar o usuário

    @Transactional(readOnly = true)
    public List<PerfilProfissionalDTO> findAll() {
        List<PerfilProfissional> lista = perfilProfissionalRepository.findAll();
        return lista.stream().map(PerfilProfissionalDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PerfilProfissionalDTO findById(UUID id) {
        PerfilProfissional entity = perfilProfissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new PerfilProfissionalDTO(entity);
    }

    @Transactional
    public PerfilProfissionalDTO insert(PerfilProfissionalDTO dto) {
        PerfilProfissional entity = new PerfilProfissional();
        copyDtoToEntity(dto, entity);
        entity = perfilProfissionalRepository.save(entity);
        return new PerfilProfissionalDTO(entity);
    }

    @Transactional
    public PerfilProfissionalDTO update(UUID id, PerfilProfissionalDTO dto) {
        try {
            PerfilProfissional entity = perfilProfissionalRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = perfilProfissionalRepository.save(entity);
            return new PerfilProfissionalDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado com o id: " + id);
        }
    }

    public void delete(UUID id) {
        if (!perfilProfissionalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado com o id: " + id);
        }
        perfilProfissionalRepository.deleteById(id);
    }

    private void copyDtoToEntity(PerfilProfissionalDTO dto, PerfilProfissional entity) {
        entity.setNomeUsuario(dto.getNomeUsuario());
        entity.setCargo(dto.getCargo());
        entity.setCarreira(dto.getCarreira());
        entity.setExperiencia(dto.getExperiencia());
        entity.setObjetivoPrincipal(dto.getObjetivoPrincipal());

        // Associa o usuário ao perfil
        if (dto.getUsuarioId() != null) {
            entity.setUsuario(usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para o perfil")));
        }

        // REMOVA a linha abaixo, ela causa o erro
        // entity.setResumosIds(dto.getResumosIds());
    }
}