package com.mentoria.back_end_mentoria.meta;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.meta.vo.Prazo;
import com.mentoria.back_end_mentoria.meta.vo.StatusMeta;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.vog.Titulo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetaServiceTest {

    @InjectMocks
    private MetaService metaService;

    @Mock
    private MetaRepository metaRepository;

    @Mock
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private Meta meta;
    private MetaDTO metaDTO;
    private Usuario usuario;
    private PerfilProfissional perfilProfissional;
    private UUID metaId;
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
        perfilProfissional = new PerfilProfissional();
        perfilProfissional.setPerfilId(perfilId);
        perfilProfissional.setUsuario(usuario);

        metaId = UUID.randomUUID();
        Instant futurePrazo = Instant.now().plus(1, ChronoUnit.DAYS);
        meta = new Meta(perfilProfissional, new Titulo("Título da Meta"), new Prazo(futurePrazo), StatusMeta.PENDENTE);
        meta.setMetaId(metaId);

        metaDTO = new MetaDTO(meta);
    }

    private void mockSecurityContext() {
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Deve retornar todas as metas")
    void testFindAll() {
        when(metaRepository.findAll()).thenReturn(Collections.singletonList(meta));

        List<MetaDTO> result = metaService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(metaDTO.getMetaId(), result.get(0).getMetaId());
        verify(metaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma meta por ID")
    void testFindById() {
        when(metaRepository.findById(metaId)).thenReturn(Optional.of(meta));

        MetaDTO result = metaService.findById(metaId);

        assertNotNull(result);
        assertEquals(metaDTO.getMetaId(), result.getMetaId());
        verify(metaRepository).findById(metaId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando a meta não for encontrada por ID")
    void testFindById_NotFound() {
        when(metaRepository.findById(metaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> metaService.findById(metaId));
        verify(metaRepository).findById(metaId);
    }

    @Test
    @DisplayName("Deve inserir uma nova meta")
    void testInsert() {
        when(perfilProfissionalRepository.findById(perfilId)).thenReturn(Optional.of(perfilProfissional));
        when(metaRepository.save(any(Meta.class))).thenReturn(meta);

        MetaDTO result = metaService.insert(metaDTO);

        assertNotNull(result);
        assertEquals(metaDTO.getMetaId(), result.getMetaId());
        verify(metaRepository).save(any(Meta.class));
    }

    @Test
    @DisplayName("Deve atualizar uma meta existente")
    void testUpdate() {
        when(metaRepository.getReferenceById(metaId)).thenReturn(meta);
        when(perfilProfissionalRepository.findById(perfilId)).thenReturn(Optional.of(perfilProfissional));
        when(metaRepository.save(any(Meta.class))).thenReturn(meta);

        MetaDTO result = metaService.update(metaId, metaDTO);

        assertNotNull(result);
        assertEquals(metaDTO.getMetaId(), result.getMetaId());
        verify(metaRepository).save(any(Meta.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar atualizar uma meta inexistente")
    void testUpdate_NotFound() {
        when(metaRepository.getReferenceById(metaId)).thenThrow(new jakarta.persistence.EntityNotFoundException());

        assertThrows(ResourceNotFoundException.class, () -> metaService.update(metaId, metaDTO));
        verify(metaRepository).getReferenceById(metaId);
    }

    @Test
    @DisplayName("Deve deletar uma meta")
    void testDelete() {
        when(metaRepository.existsById(metaId)).thenReturn(true);
        doNothing().when(metaRepository).deleteById(metaId);

        metaService.delete(metaId);

        verify(metaRepository).deleteById(metaId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar uma meta inexistente")
    void testDelete_NotFound() {
        when(metaRepository.existsById(metaId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> metaService.delete(metaId));
        verify(metaRepository, never()).deleteById(metaId);
    }

    @Test
    @DisplayName("Deve retornar as metas do usuário logado")
    void testFindMyMetas() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.of(perfilProfissional));
        when(metaRepository.findByPerfilProfissionalPerfilId(perfilId)).thenReturn(Collections.singletonList(meta));

        List<MetaDTO> result = metaService.findMyMetas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(metaDTO.getMetaId(), result.get(0).getMetaId());
        verify(metaRepository).findByPerfilProfissionalPerfilId(perfilId);
    }

    @Test
    @DisplayName("Deve inserir uma nova meta para o usuário logado")
    void testInsertMyMeta() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.of(perfilProfissional));
        when(metaRepository.save(any(Meta.class))).thenReturn(meta);

        MetaDTO result = metaService.insertMyMeta(metaDTO);

        assertNotNull(result);
        assertEquals(metaDTO.getMetaId(), result.getMetaId());
        verify(metaRepository).save(any(Meta.class));
    }

    @Test
    @DisplayName("Deve atualizar uma meta do usuário logado")
    void testUpdateMyMeta() {
        mockSecurityContext();
        when(metaRepository.findById(metaId)).thenReturn(Optional.of(meta));
        when(perfilProfissionalRepository.findById(perfilId)).thenReturn(Optional.of(perfilProfissional)); // Mock que faltava
        when(metaRepository.save(any(Meta.class))).thenReturn(meta);

        MetaDTO result = metaService.updateMyMeta(metaId, metaDTO);

        assertNotNull(result);
        assertEquals(metaDTO.getMetaId(), result.getMetaId());
        verify(metaRepository).save(any(Meta.class));
    }

    @Test
    @DisplayName("Deve lançar AccessDeniedException ao tentar atualizar a meta de outro usuário")
    void testUpdateMyMeta_AccessDenied() throws NoSuchFieldException, IllegalAccessException {
        mockSecurityContext();
        
        Usuario outroUsuario = new Usuario();
        UUID outroUsuarioId = UUID.randomUUID();
        Field idField = Usuario.class.getDeclaredField("usuarioId");
        idField.setAccessible(true);
        idField.set(outroUsuario, outroUsuarioId);
        
        PerfilProfissional outroPerfil = new PerfilProfissional();
        outroPerfil.setUsuario(outroUsuario);
        
        Instant futurePrazo = Instant.now().plus(1, ChronoUnit.DAYS);
        Meta outraMeta = new Meta(outroPerfil, new Titulo("Outra Meta"), new Prazo(futurePrazo), StatusMeta.PENDENTE);
        
        when(metaRepository.findById(metaId)).thenReturn(Optional.of(outraMeta));

        assertThrows(AccessDeniedException.class, () -> metaService.updateMyMeta(metaId, metaDTO));
        verify(metaRepository, never()).save(any(Meta.class));
    }

    @Test
    @DisplayName("Deve deletar uma meta do usuário logado")
    void testDeleteMyMeta() {
        mockSecurityContext();
        when(metaRepository.findById(metaId)).thenReturn(Optional.of(meta));
        doNothing().when(metaRepository).deleteById(metaId);

        metaService.deleteMyMeta(metaId);

        verify(metaRepository).deleteById(metaId);
    }

    @Test
    @DisplayName("Deve lançar AccessDeniedException ao tentar deletar a meta de outro usuário")
    void testDeleteMyMeta_AccessDenied() throws NoSuchFieldException, IllegalAccessException {
        mockSecurityContext();
        
        Usuario outroUsuario = new Usuario();
        UUID outroUsuarioId = UUID.randomUUID();
        Field idField = Usuario.class.getDeclaredField("usuarioId");
        idField.setAccessible(true);
        idField.set(outroUsuario, outroUsuarioId);
        
        PerfilProfissional outroPerfil = new PerfilProfissional();
        outroPerfil.setUsuario(outroUsuario);
        
        Instant futurePrazo = Instant.now().plus(1, ChronoUnit.DAYS);
        Meta outraMeta = new Meta(outroPerfil, new Titulo("Outra Meta"), new Prazo(futurePrazo), StatusMeta.PENDENTE);

        when(metaRepository.findById(metaId)).thenReturn(Optional.of(outraMeta));

        assertThrows(AccessDeniedException.class, () -> metaService.deleteMyMeta(metaId));
        verify(metaRepository, never()).deleteById(metaId);
    }
}