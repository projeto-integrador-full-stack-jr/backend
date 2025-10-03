package com.mentoria.back_end_mentoria.nota;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
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
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Transactional(readOnly = true)
    public List<NotaDTO> findAll(){
        return notaRepository.findAll().stream().map(NotaDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotaDTO> findMyNotas() {
        Usuario usuarioLogado = getUsuarioLogado(); 
        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil profissional não encontrado para o usuário logado."));
        
        List<Nota> lista = notaRepository.findByPerfilProfissionalPerfilId(perfil.getPerfilId());
        return lista.stream().map(NotaDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotaDTO findById(UUID id) {
        Nota entity = notaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nota não encontrada"));
        return new NotaDTO(entity);
    }

    @Transactional
    public NotaDTO insert(NotaDTO dto) {
        Nota entity = new Nota();

        copyDtoToEntity(dto, entity);
        entity = notaRepository.save(entity);
        return new NotaDTO(entity);
    }

    @Transactional
    public NotaDTO insertMyNota(NotaDTO dto) {
        Usuario usuarioLogado = getUsuarioLogado();
        
        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Para criar uma nota, primeiro crie seu perfil profissional."));

        Nota entity = new Nota();
        
        entity.setPerfilProfissional(perfil);
        
        copyDtoToEntity(dto, entity);

        entity = notaRepository.save(entity);
        return new NotaDTO(entity);
    }

    @Transactional
    public NotaDTO update(UUID id, NotaDTO dto) {
        try {
            Nota entity = notaRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = notaRepository.save(entity);
            return new NotaDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Nota não encontrada com o id: " + id);
        }
    }

    @Transactional
    public NotaDTO updateMyNota(UUID notaId, NotaDTO dto) {
        Usuario usuarioLogado = getUsuarioLogado();

        Nota entity = notaRepository.findById(notaId)
                .orElseThrow(() -> new ResourceNotFoundException("Nota não encontrada com o id: " + notaId));

        UUID idDonoDaNota = entity.getPerfilProfissional().getUsuario().getUsuarioId();

        if (!usuarioLogado.getUsuarioId().equals(idDonoDaNota)) {
            throw new AccessDeniedException("Acesso negado. Você só pode alterar suas próprias notas.");
        }

        copyDtoToEntity(dto, entity);
        entity = notaRepository.save(entity);
        return new NotaDTO(entity);
    }

    public void delete(UUID id) {
        if (!notaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Nota não encontrada com o id: " + id);
        }
        notaRepository.deleteById(id);
    }

    @Transactional
    public void deleteMyNota(UUID notaId) {
        Usuario usuarioLogado = getUsuarioLogado();

        Nota entity = notaRepository.findById(notaId)
                .orElseThrow(() -> new ResourceNotFoundException("Nota não encontrada com o id: " + notaId));

        UUID idDonoDaNota = entity.getPerfilProfissional().getUsuario().getUsuarioId();

        if (!usuarioLogado.getUsuarioId().equals(idDonoDaNota)) {
            throw new AccessDeniedException("Acesso negado. Você só pode deletar suas próprias notas.");
        }

        notaRepository.deleteById(notaId);
    }

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void copyDtoToEntity(NotaDTO dto, Nota entity) {
        entity.setTitulo(new Titulo(dto.getTitulo()));
        entity.setConteudo(new Conteudo(dto.getConteudo()));

        if (dto.getPerfilProfissionalId() != null) {
            entity.setPerfilProfissional(perfilProfissionalRepository.findById(dto.getPerfilProfissionalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Perfil Profissional não encontrado para a nota")));
        }
    }
}