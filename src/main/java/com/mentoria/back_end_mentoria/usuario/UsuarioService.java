package com.mentoria.back_end_mentoria.usuario;

import com.mentoria.back_end_mentoria.usuario.vo.Senha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.usuario.vo.Email;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario) {
        if (usuario.getSenha() == null || usuario.getSenha().getValor() == null || usuario.getSenha().getValor().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
        String password = usuario.getSenha().getValor();
        Senha senhaValidadaECodificada = new Senha(password);
        String senhaCriptografada = passwordEncoder.encode(senhaValidadaECodificada.getValor());
        senhaValidadaECodificada.setValor(senhaCriptografada);
        usuario.setSenha(senhaValidadaECodificada);
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(UUID id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario update(UUID id, UsuarioDTO dto) {
        try {
            Usuario entity = usuarioRepository.getReferenceById(id);
            entity.setEmail(new Email(dto.getEmail()));
            entity.setAcesso(dto.getAcesso());

            if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
                String password = dto.getSenha();
                Senha senhaValidadaECodificada = new Senha(password);
                String senhaCriptografada = passwordEncoder.encode(senhaValidadaECodificada.getValor());
                entity.getSenha().setValor(senhaCriptografada);
            }

            return usuarioRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + id);
        }
    }

    @Transactional
    public Usuario updateMyUser(UsuarioDTO dto) {
        Usuario entity = getUsuarioLogado();

        entity.setEmail(new Email(dto.getEmail()));

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
            entity.getSenha().setValor(senhaCriptografada);
        }

        return usuarioRepository.save(entity);
    }

    public void delete(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public void deleteMyUser() {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!usuarioRepository.existsById(usuarioLogado.getUsuarioId())) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + usuarioLogado.getUsuarioId());
        }
        usuarioRepository.deleteById(usuarioLogado.getUsuarioId());
    }

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
