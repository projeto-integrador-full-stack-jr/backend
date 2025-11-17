package com.mentoria.back_end_mentoria.meta;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.meta.vo.MetaRequest;
import com.mentoria.back_end_mentoria.meta.vo.MetaResponse;
import com.mentoria.back_end_mentoria.meta.vo.StatusMeta;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.usuario.Usuario;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MetaService {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Transactional(readOnly = true)
    public List<MetaResponse> findAll() {
        return metaRepository.findAll().stream().map(MetaResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MetaResponse findById(UUID id) {
        Meta entity = metaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada"));
        return new MetaResponse(entity);
    }

    @Transactional
    public MetaResponse insert(MetaRequest dto) {
        Meta entity = new Meta();
        copyDtoToEntity(dto, entity);
        entity.setStatusMeta(StatusMeta.AGUARDANDO);
        entity = metaRepository.save(entity);
        return new MetaResponse(entity);
    }

    @Transactional
    public MetaResponse update(UUID id, MetaRequest dto) {
        if (Objects.isNull(id) || Objects.isNull(dto)) throw new AccessDeniedException("Os campos estão vazios!");
        try {
            Meta entity = metaRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = metaRepository.save(entity);
            return new MetaResponse(entity);
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

    @Transactional
    public MetaResponse updateMyMeta(UUID metaId, MetaRequest dto) {
        Usuario usuarioLogado = getUsuarioLogado();

        Meta entity = metaRepository.findById(metaId)
                .orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada com o id: " + metaId));

        UUID idDonoDaMeta = entity.getPerfilProfissional().getUsuario().getUsuarioId();

        if (!usuarioLogado.getUsuarioId().equals(idDonoDaMeta)) {
            throw new AccessDeniedException("Acesso negado. Você só pode alterar suas próprias metas.");
        }

        copyDtoToEntity(dto, entity);
        entity = metaRepository.save(entity);
        return new MetaResponse(entity);
    }

    @Transactional
    public void deleteMyMeta(UUID metaId) {
        Usuario usuarioLogado = getUsuarioLogado();

        Meta entity = metaRepository.findById(metaId)
                .orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada com o id: " + metaId));

        UUID idDonoDaMeta = entity.getPerfilProfissional().getUsuario().getUsuarioId();

        if (!usuarioLogado.getUsuarioId().equals(idDonoDaMeta)) {
            throw new AccessDeniedException("Acesso negado. Você só pode deletar suas próprias metas.");
        }

        metaRepository.deleteById(metaId);
    }

    @Transactional
    public List<MetaResponse> findMyMetas() {
        Usuario usuarioLogado = getUsuarioLogado();

        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil profissional não encontrado para o usuário logado."));

        List<Meta> lista = metaRepository.findByPerfilProfissionalPerfilId(perfil.getPerfilId());

        lista.forEach(meta -> {
            if (meta.getPrazo().isBefore(Instant.now())
                    && meta.getStatusMeta() != StatusMeta.EXPIRADA
                    && meta.getStatusMeta() != StatusMeta.CONCLUIDO) {
                meta.setStatusMeta(StatusMeta.EXPIRADA);
                metaRepository.save(meta);
            }
        });

        return lista.stream().map(MetaResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public MetaResponse findMyMetaPerID(UUID id) {
        Usuario usuarioLogado = getUsuarioLogado();
        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil profissional não encontrado para o usuário logado."));

        List<Meta> lista = metaRepository.findByPerfilProfissionalPerfilId(perfil.getPerfilId());

        lista.forEach(meta -> {
            if (meta.getPrazo().isBefore(Instant.now()) && meta.getStatusMeta() != StatusMeta.EXPIRADA) {
                meta.setStatusMeta(StatusMeta.EXPIRADA);
                metaRepository.save(meta);
            }
        });

        return new MetaResponse(Objects.requireNonNull(lista.stream().filter(m -> m.getMetaId()
                        .equals(id))
                .findFirst()
                .orElse(null)));
    }

    @Transactional
    public MetaResponse insertMyMeta(MetaRequest dto) {
        Usuario usuarioLogado = getUsuarioLogado();

        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Para criar uma meta, primeiro crie seu perfil profissional."));

        Meta entity = new Meta();

        entity.setPerfilProfissional(perfil);
        entity.setTitulo(dto.titulo());
        entity.setPrazo(dto.prazo());
        entity.setStatusMeta(StatusMeta.AGUARDANDO);

        entity = metaRepository.save(entity);
        return new MetaResponse(entity);
    }

    @Transactional
    public MetaResponse changeStatus(UUID id, StatusMeta status) {
        Usuario usuarioLogado = getUsuarioLogado();

        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Para criar uma meta, primeiro crie seu perfil profissional."));

        if (status == StatusMeta.AGUARDANDO || status == StatusMeta.EXPIRADA) {
            throw new RuntimeException("Não é permitido alterar para os status AGUARDANDO ou EXPIRADA.");
        }

        List<Meta> lista = metaRepository.findByPerfilProfissionalPerfilId(perfil.getPerfilId());

        Meta meta = lista.stream()
                .filter(m -> m.getMetaId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada para o usuário."));
        meta.setStatusMeta(status);
        metaRepository.save(meta);

        return new MetaResponse(meta);
    }

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void copyDtoToEntity(MetaRequest dto, Meta entity) {
        entity.setTitulo(dto.titulo());
        entity.setPrazo(dto.prazo());
    }


}