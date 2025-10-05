package com.mentoria.back_end_mentoria.resumo;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
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
class ResumoServiceTest {

    @InjectMocks
    private ResumoService resumoService;

    @Mock
    private ResumoRepository resumoRepository;

    @Mock
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Mock
    private ChatModel chatModel;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private Resumo resumo;
    private ResumoDTO resumoDTO;
    private PerfilProfissional perfilProfissional;
    private Usuario usuario;
    private UUID resumoId;
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

        resumoId = UUID.randomUUID();
        resumo = new Resumo();
        resumo.setResumoId(resumoId);
        resumo.setPerfilProfissional(perfilProfissional);
        resumo.setTitulo(new com.mentoria.back_end_mentoria.vog.Titulo("Título Teste"));
        resumo.setConteudo(new com.mentoria.back_end_mentoria.vog.Conteudo("Conteúdo Teste"));

        resumoDTO = new ResumoDTO(resumo);
    }

    private void mockSecurityContext() {
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private void mockChatModel() {
        AssistantMessage assistantMessage = new AssistantMessage("Conteúdo gerado pela IA");
        Generation generation = new Generation(assistantMessage);
        ChatResponse chatResponse = new ChatResponse(Collections.singletonList(generation));
        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);
    }

    @Test
    @DisplayName("Deve retornar todos os resumos")
    void testFindAll() {
        when(resumoRepository.findAll()).thenReturn(Collections.singletonList(resumo));

        List<ResumoDTO> result = resumoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(resumoId, result.get(0).getResumoId());
        verify(resumoRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar um resumo por ID")
    void testFindById() {
        when(resumoRepository.findById(resumoId)).thenReturn(Optional.of(resumo));

        ResumoDTO result = resumoService.findById(resumoId);

        assertNotNull(result);
        assertEquals(resumoId, result.getResumoId());
        verify(resumoRepository).findById(resumoId);
    }

    @Test
    @DisplayName("Deve retornar meus resumos")
    void testFindMyResumos() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.of(perfilProfissional));
        when(resumoRepository.findByPerfilProfissionalPerfilId(perfilId)).thenReturn(Collections.singletonList(resumo));

        List<ResumoDTO> result = resumoService.findMyResumos();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(resumoRepository).findByPerfilProfissionalPerfilId(perfilId);
    }

    @Test
    @DisplayName("Deve inserir um novo resumo com conteúdo gerado por IA")
    void testInsert_WithIA() {
        when(perfilProfissionalRepository.findById(perfilId)).thenReturn(Optional.of(perfilProfissional));
        mockChatModel();
        when(resumoRepository.save(any(Resumo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResumoDTO result = resumoService.insert(perfilId);

        assertNotNull(result);
        assertEquals("Conteúdo gerado pela IA", result.getConteudo());
        verify(resumoRepository).save(any(Resumo.class));
        verify(chatModel).call(any(Prompt.class));
    }

    @Test
    @DisplayName("Deve inserir meu resumo com conteúdo gerado por IA")
    void testInsertMyResumo_WithIA() {
        mockSecurityContext();
        when(perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioId)).thenReturn(Optional.of(perfilProfissional));
        mockChatModel();
        when(resumoRepository.save(any(Resumo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResumoDTO result = resumoService.insertMyResumo();

        assertNotNull(result);
        assertEquals("Conteúdo gerado pela IA", result.getConteudo());
        verify(resumoRepository).save(any(Resumo.class));
        verify(chatModel).call(any(Prompt.class));
    }


    @Test
    @DisplayName("Deve deletar um resumo")
    void testDelete() {
        when(resumoRepository.existsById(resumoId)).thenReturn(true);
        doNothing().when(resumoRepository).deleteById(resumoId);

        resumoService.delete(resumoId);

        verify(resumoRepository).deleteById(resumoId);
    }

    @Test
    @DisplayName("Deve deletar meu resumo")
    void testDeleteMyResumo() {
        mockSecurityContext();
        when(resumoRepository.findById(resumoId)).thenReturn(Optional.of(resumo));
        doNothing().when(resumoRepository).deleteById(resumoId);

        resumoService.deleteMyResumo(resumoId);

        verify(resumoRepository).deleteById(resumoId);
    }

    @Test
    @DisplayName("Deve lançar AccessDeniedException ao tentar deletar resumo de outro usuário")
    void testDeleteMyResumo_AccessDenied() throws Exception {
        mockSecurityContext();

        Usuario outroUsuario = new Usuario();
        Field idField = Usuario.class.getDeclaredField("usuarioId");
        idField.setAccessible(true);
        idField.set(outroUsuario, UUID.randomUUID());

        PerfilProfissional outroPerfil = new PerfilProfissional();
        outroPerfil.setUsuario(outroUsuario);

        Resumo outroResumo = new Resumo();
        outroResumo.setPerfilProfissional(outroPerfil);

        when(resumoRepository.findById(resumoId)).thenReturn(Optional.of(outroResumo));

        assertThrows(AccessDeniedException.class, () -> resumoService.deleteMyResumo(resumoId));
        verify(resumoRepository, never()).deleteById(any());
    }
}
