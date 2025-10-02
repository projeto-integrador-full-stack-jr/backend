package com.mentoria.back_end_mentoria.meta;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.usuario.Usuario;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

    @Transactional
    public MetaDTO updateMyMeta(UUID metaId, MetaDTO dto) {
        Usuario usuarioLogado = getUsuarioLogado();
        
        Meta entity = metaRepository.findById(metaId)
                .orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada com o id: " + metaId));
        
        UUID idDonoDaMeta = entity.getPerfilProfissional().getUsuario().getUsuarioId();

        if (!usuarioLogado.getUsuarioId().equals(idDonoDaMeta)) {
            throw new AccessDeniedException("Acesso negado. Você só pode alterar suas próprias metas.");
        }

        copyDtoToEntity(dto, entity);
        entity = metaRepository.save(entity);
        return new MetaDTO(entity);
    }

    public void delete(UUID id) {
        if (!metaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meta não encontrada com o id: " + id);
        }
        metaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MetaDTO> findMyMetas() {
        Usuario usuarioLogado = getUsuarioLogado();
        
        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil profissional não encontrado para o usuário logado."));
        
        List<Meta> lista = metaRepository.findByPerfilProfissionalPerfilId(perfil.getPerfilId());
        
        return lista.stream().map(MetaDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public MetaDTO insertMyMeta(MetaDTO dto) {
        Usuario usuarioLogado = getUsuarioLogado();
        
        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Para criar uma meta, primeiro crie seu perfil profissional."));

        Meta entity = new Meta();
        
        entity.setPerfilProfissional(perfil);
        
        entity.setTitulo(dto.getTitulo());
        entity.setPrazo(dto.getPrazo());
        entity.setStatusMeta(dto.getStatusMeta());

        // Salva a nova meta
        entity = metaRepository.save(entity);
        return new MetaDTO(entity);
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