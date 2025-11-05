package com.mentoria.back_end_mentoria.usuario;

import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.usuario.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario save(UsuarioRequest NovoUsuario) {
        if (NovoUsuario.getSenha() == null) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
        String password = NovoUsuario.getSenha();
        Senha senhaValidadaECodificada = new Senha(password);
        String senhaCriptografada = passwordEncoder.encode(senhaValidadaECodificada.getValor());
        senhaValidadaECodificada.setValor(senhaCriptografada);
        Usuario usuario = new Usuario(new Email(NovoUsuario.getEmail()), senhaValidadaECodificada, UserRole.USER);
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(UUID id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public UsuarioResponse update(UUID id, UserRole acesso) {
        Usuario entity = usuarioRepository.getReferenceById(id);
        if (entity == null) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + id);
        }
        entity.setAcesso(acesso);
        usuarioRepository.save(entity);
        return new UsuarioResponse(entity);
    }

    @Transactional
    public UsuarioResponse updateMyUser(UsuarioRequest usuarioRequest) {
        Usuario entity = getUsuarioLogado();

        entity.setEmail(new Email(usuarioRequest.getEmail()));

        if (usuarioRequest.getSenha() != null && !usuarioRequest.getSenha().isBlank()) {
            String senhaCriptografada = passwordEncoder.encode(usuarioRequest.getSenha());
            entity.getSenha().setValor(senhaCriptografada);
        }
        usuarioRepository.save(entity);

        return new UsuarioResponse(entity);
    }

    @Transactional
    public void delete(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            return;
        }
        perfilProfissionalRepository.deleteByUsuario_UsuarioId(id);
        usuarioRepository.deleteById(id);
    }

    public void deleteMyUser() {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!usuarioRepository.existsById(usuarioLogado.getUsuarioId())) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + usuarioLogado.getUsuarioId());
        }
        usuarioRepository.deleteById(usuarioLogado.getUsuarioId());
    }

    public Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
