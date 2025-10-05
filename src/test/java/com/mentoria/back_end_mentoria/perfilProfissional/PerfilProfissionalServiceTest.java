package com.mentoria.back_end_mentoria.perfilProfissional;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilProfissionalServiceTest {

    @InjectMocks
    private PerfilProfissionalService perfilProfissionalService;

    @Mock
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private PerfilProfissional perfilProfissional;
    private PerfilProfissionalDTO perfilProfissionalDTO;
    private Usuario usuario;
    private UUID perfilId;
    private UUID usuarioId;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        usuarioId = UUID.randomUUID();
        usuario = new Usuario();
        Field idField = Usuario.class.getDeclaredField("usuarioId");
        idField.setAccessible(true);
        idField.set(usuario, usuarioId);

        perfilId = UUID.randomUUID();
        perfilProfissional = new PerfilProfissional(perfilId, usuario, "Nome Teste", "Cargo Teste", "Carreira Teste", "Experiencia Teste", "Objetivo Teste");

        perfilProfissionalDTO = new PerfilProfissionalDTO(perfilProfissional);
    }

    private void mockSecurityContext() {
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Deve retornar todos os perfis")
    void testFindAll() {
        when(perfilProfissionalRepository.findAll()).thenReturn(Collections.singletonList(perfilProfissional));

        List<PerfilProfissionalDTO> result = perfilProfissionalService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(perfilId, result.get(0).getPerfilId());
        verify(perfilProfissionalRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar um perfil por ID")
    void testFindById() {
        when(perfilProfissionalRepository.findById(perfilId)).thenReturn(Optional.of(perfilProfissional));

        PerfilProfissionalDTO result = perfilProfissionalService.findById(perfilId);

        assertNotNull(result);
        assertEquals(perfilId, result.getPerfilId());
        verify(perfilProfissionalRepository).findById(perfilId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o perfil não for encontrado por ID")
    void testFindById_NotFound() {
        when(perfilProfissionalRepository.findById(perfilId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> perfilProfissionalService.findById(perfilId));
        verify(perfilProfissionalRepository).findById(perfilId);
    }

    @Test
    @DisplayName("Deve inserir um novo perfil")
    void testInsert() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(perfilProfissionalRepository.save(any(PerfilProfissional.class))).thenReturn(perfilProfissional);

        PerfilProfissionalDTO result = perfilProfissionalService.insert(perfilProfissionalDTO);

        assertNotNull(result);
        assertEquals(perfilId, result.getPerfilId());
        verify(perfilProfissionalRepository).save(any(PerfilProfissional.class));
    }

    @Test
    @DisplayName("Deve atualizar um perfil existente")
    void testUpdate() {
        when(perfilProfissionalRepository.getReferenceById(perfilId)).thenReturn(perfilProfissional);
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(perfilProfissionalRepository.save(any(PerfilProfissional.class))).thenReturn(perfilProfissional);

        PerfilProfissionalDTO result = perfilProfissionalService.update(perfilId, perfilProfissionalDTO);

        assertNotNull(result);
        assertEquals(perfilId, result.getPerfilId());
        verify(perfilProfissionalRepository).save(any(PerfilProfissional.class));
    }

    @Test
    @DisplayName("Deve deletar um perfil")
    void testDelete() {
        when(perfilProfissionalRepository.existsById(perfilId)).thenReturn(true);
        doNothing().when(perfilProfissionalRepository).deleteById(perfilId);

        perfilProfissionalService.delete(perfilId);

        verify(perfilProfissionalRepository).deleteById(perfilId);
    }

    @Test
    @DisplayName("Deve retornar o perfil do usuário logado")
    void testFindMyProfile() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.of(perfilProfissional));

        PerfilProfissionalDTO result = perfilProfissionalService.findMyProfile();

        assertNotNull(result);
        assertEquals(perfilId, result.getPerfilId());
        verify(perfilProfissionalRepository).findByUsuarioUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("Deve criar um novo perfil para o usuário logado")
    void testCreateMyProfile() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.empty());
        when(perfilProfissionalRepository.save(any(PerfilProfissional.class))).thenReturn(perfilProfissional);

        PerfilProfissionalDTO result = perfilProfissionalService.createOrUpdateMyProfile(perfilProfissionalDTO);

        assertNotNull(result);
        assertEquals(perfilId, result.getPerfilId());
        verify(perfilProfissionalRepository).save(any(PerfilProfissional.class));
    }

    @Test
    @DisplayName("Deve atualizar o perfil existente do usuário logado")
    void testUpdateMyProfile() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.of(perfilProfissional));
        when(perfilProfissionalRepository.save(any(PerfilProfissional.class))).thenReturn(perfilProfissional);

        PerfilProfissionalDTO result = perfilProfissionalService.createOrUpdateMyProfile(perfilProfissionalDTO);

        assertNotNull(result);
        assertEquals(perfilId, result.getPerfilId());
        verify(perfilProfissionalRepository).save(any(PerfilProfissional.class));
    }

    @Test
    @DisplayName("Deve deletar o perfil do usuário logado")
    void testDeleteMyProfile() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.of(perfilProfissional));
        doNothing().when(perfilProfissionalRepository).deleteById(perfilId);

        perfilProfissionalService.deleteMyProfile();

        verify(perfilProfissionalRepository).deleteById(perfilId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar um perfil inexistente")
    void testDeleteMyProfile_NotFound() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> perfilProfissionalService.deleteMyProfile());
        verify(perfilProfissionalRepository, never()).deleteById(any());
    }
}
