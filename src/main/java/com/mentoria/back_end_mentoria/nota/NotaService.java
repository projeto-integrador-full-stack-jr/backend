package com.mentoria.back_end_mentoria.nota;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<NotaDTO> findAll(){
        return notaRepository.findAll().stream().map(NotaDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotaDTO findById(UUID id) {
        Nota entity = notaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nota não encontrada"));
        return new NotaDTO(entity);
    }

    @Transactional
    public NotaDTO insert(NotaDTO dto) {
        Nota entity = new Nota();
        entity.setNotaId(UUID.randomUUID());
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

    public void delete(UUID id) {
        if (!notaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Nota não encontrada com o id: " + id);
        }
        notaRepository.deleteById(id);
    }

    private void copyDtoToEntity(NotaDTO dto, Nota entity) {
        entity.setTitulo(new Titulo(dto.getTitulo()));
        entity.setConteudo(new Conteudo(dto.getConteudo()));

        if (dto.getUsuarioId() != null) {
            entity.setUsuario(usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para a nota")));
        }
    }
}