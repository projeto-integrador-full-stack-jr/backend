package com.mentoria.back_end_mentoria.nota;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
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
class NotaServiceTest {

    @InjectMocks
    private NotaService notaService;

    @Mock
    private NotaRepository notaRepository;

    @Mock
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private Nota nota;
    private NotaDTO notaDTO;
    private Usuario usuario;
    private PerfilProfissional perfilProfissional;
    private UUID notaId;
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

        notaId = UUID.randomUUID();
        nota = new Nota(notaId, perfilProfissional, new Titulo("Título da Nota"), new Conteudo("Conteúdo da nota"));

        notaDTO = new NotaDTO(nota);
    }

    private void mockSecurityContext() {
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Deve retornar todas as notas")
    void testFindAll() {
        when(notaRepository.findAll()).thenReturn(Collections.singletonList(nota));

        List<NotaDTO> result = notaService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notaDTO.getNotaId(), result.get(0).getNotaId());
        verify(notaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma nota por ID")
    void testFindById() {
        when(notaRepository.findById(notaId)).thenReturn(Optional.of(nota));

        NotaDTO result = notaService.findById(notaId);

        assertNotNull(result);
        assertEquals(notaDTO.getNotaId(), result.getNotaId());
        verify(notaRepository).findById(notaId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando a nota não for encontrada por ID")
    void testFindById_NotFound() {
        when(notaRepository.findById(notaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> notaService.findById(notaId));
        verify(notaRepository).findById(notaId);
    }

    @Test
    @DisplayName("Deve inserir uma nova nota")
    void testInsert() {
        when(perfilProfissionalRepository.findById(perfilId)).thenReturn(Optional.of(perfilProfissional));
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);

        NotaDTO result = notaService.insert(notaDTO);

        assertNotNull(result);
        assertEquals(notaDTO.getNotaId(), result.getNotaId());
        verify(notaRepository).save(any(Nota.class));
    }

    @Test
    @DisplayName("Deve atualizar uma nota existente")
    void testUpdate() {
        when(notaRepository.getReferenceById(notaId)).thenReturn(nota);
        when(perfilProfissionalRepository.findById(perfilId)).thenReturn(Optional.of(perfilProfissional));
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);

        NotaDTO result = notaService.update(notaId, notaDTO);

        assertNotNull(result);
        assertEquals(notaDTO.getNotaId(), result.getNotaId());
        verify(notaRepository).save(any(Nota.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar atualizar uma nota inexistente")
    void testUpdate_NotFound() {
        when(notaRepository.getReferenceById(notaId)).thenThrow(new jakarta.persistence.EntityNotFoundException());

        assertThrows(ResourceNotFoundException.class, () -> notaService.update(notaId, notaDTO));
        verify(notaRepository).getReferenceById(notaId);
    }

    @Test
    @DisplayName("Deve deletar uma nota")
    void testDelete() {
        when(notaRepository.existsById(notaId)).thenReturn(true);
        doNothing().when(notaRepository).deleteById(notaId);

        notaService.delete(notaId);

        verify(notaRepository).deleteById(notaId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar uma nota inexistente")
    void testDelete_NotFound() {
        when(notaRepository.existsById(notaId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> notaService.delete(notaId));
        verify(notaRepository, never()).deleteById(notaId);
    }

    @Test
    @DisplayName("Deve retornar as notas do usuário logado")
    void testFindMyNotes() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.of(perfilProfissional));
        when(notaRepository.findByPerfilProfissionalPerfilId(perfilId)).thenReturn(Collections.singletonList(nota));

        List<NotaDTO> result = notaService.findMyNotas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notaDTO.getNotaId(), result.get(0).getNotaId());
        verify(notaRepository).findByPerfilProfissionalPerfilId(perfilId);
    }

    @Test
    @DisplayName("Deve inserir uma nova nota para o usuário logado")
    void testInsertMyNota() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.of(perfilProfissional));
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);

        NotaDTO result = notaService.insertMyNota(notaDTO);

        assertNotNull(result);
        assertEquals(notaDTO.getNotaId(), result.getNotaId());
        verify(notaRepository).save(any(Nota.class));
    }

    @Test
    @DisplayName("Deve atualizar uma nota do usuário logado")
    void testUpdateMyNota() {
        mockSecurityContext();
        when(notaRepository.findById(notaId)).thenReturn(Optional.of(nota));
        when(perfilProfissionalRepository.findById(perfilId)).thenReturn(Optional.of(perfilProfissional));
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);

        NotaDTO result = notaService.updateMyNota(notaId, notaDTO);

        assertNotNull(result);
        assertEquals(notaDTO.getNotaId(), result.getNotaId());
        verify(notaRepository).save(any(Nota.class));
    }

    @Test
    @DisplayName("Deve lançar AccessDeniedException ao tentar atualizar a nota de outro usuário")
    void testUpdateMyNota_AccessDenied() throws Exception {
        mockSecurityContext();

        Usuario outroUsuario = new Usuario();
        Field idField = Usuario.class.getDeclaredField("usuarioId");
        idField.setAccessible(true);
        idField.set(outroUsuario, UUID.randomUUID());

        PerfilProfissional outroPerfil = new PerfilProfissional();
        outroPerfil.setUsuario(outroUsuario);

        Nota outraNota = new Nota(UUID.randomUUID(), outroPerfil, new Titulo("Outra Nota"), new Conteudo("Outro conteúdo"));

        when(notaRepository.findById(notaId)).thenReturn(Optional.of(outraNota));

        assertThrows(AccessDeniedException.class, () -> notaService.updateMyNota(notaId, notaDTO));
        verify(notaRepository, never()).save(any(Nota.class));
    }

    @Test
    @DisplayName("Deve deletar uma nota do usuário logado")
    void testDeleteMyNota() {
        mockSecurityContext();
        when(notaRepository.findById(notaId)).thenReturn(Optional.of(nota));
        doNothing().when(notaRepository).deleteById(notaId);

        notaService.deleteMyNota(notaId);

        verify(notaRepository).deleteById(notaId);
    }

    @Test
    @DisplayName("Deve lançar AccessDeniedException ao tentar deletar a nota de outro usuário")
    void testDeleteMyNota_AccessDenied() throws Exception {
        mockSecurityContext();

        Usuario outroUsuario = new Usuario();
        Field idField = Usuario.class.getDeclaredField("usuarioId");
        idField.setAccessible(true);
        idField.set(outroUsuario, UUID.randomUUID());

        PerfilProfissional outroPerfil = new PerfilProfissional();
        outroPerfil.setUsuario(outroUsuario);

        Nota outraNota = new Nota(UUID.randomUUID(), outroPerfil, new Titulo("Outra Nota"), new Conteudo("Outro conteúdo"));

        when(notaRepository.findById(notaId)).thenReturn(Optional.of(outraNota));

        assertThrows(AccessDeniedException.class, () -> notaService.deleteMyNota(notaId));
        verify(notaRepository, never()).deleteById(notaId);
    }
}
