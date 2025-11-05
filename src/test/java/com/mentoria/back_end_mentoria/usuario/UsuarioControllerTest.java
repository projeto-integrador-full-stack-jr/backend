package com.mentoria.back_end_mentoria.usuario;

import com.mentoria.back_end_mentoria.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoria.back_end_mentoria.security.AutenticacaoService;
import com.mentoria.back_end_mentoria.security.TokenService;
import com.mentoria.back_end_mentoria.usuario.vo.Email;
import com.mentoria.back_end_mentoria.usuario.vo.Senha;
import com.mentoria.back_end_mentoria.usuario.vo.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UsuarioController.class)
@ActiveProfiles("prod") // Força o uso das regras de segurança de produção
@Import(SecurityConfig.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private TokenService tokenService;
    @MockBean
    private AutenticacaoService autenticacaoService;
    @MockBean
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioCompleto;

    @BeforeEach
    void setUp() {
        usuarioCompleto = new Usuario(new Email("teste@example.com"), new Senha("Senha@123"), UserRole.USER);
    }

    // --- Testes para Endpoints de ADMIN --- //

    @Test
    @DisplayName("Admin deve conseguir listar todos os usuários")
    @WithMockUser(roles = "ADMIN")
    void testFindAll_AsAdmin_ShouldSucceed() throws Exception {
        when(usuarioService.findAll()).thenReturn(Collections.singletonList(usuarioCompleto));
        mockMvc.perform(get("/usuarios/listar"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User não deve conseguir listar todos os usuários")
    @WithMockUser(roles = "USER")
    void testFindAll_AsUser_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/usuarios/listar"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin deve conseguir buscar usuário por ID")
    @WithMockUser(roles = "ADMIN")
    void testFindById_AsAdmin_ShouldSucceed() throws Exception {
        when(usuarioService.findById(any(UUID.class))).thenReturn(usuarioCompleto);
        mockMvc.perform(get("/usuarios/" + UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User não deve conseguir buscar usuário por ID")
    @WithMockUser(roles = "USER")
    void testFindById_AsUser_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/usuarios/" + UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin deve conseguir deletar usuário por ID")
    @WithMockUser(roles = "ADMIN")
    void testDelete_AsAdmin_ShouldSucceed() throws Exception {
        mockMvc.perform(delete("/usuarios/" + UUID.randomUUID()).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("User não deve conseguir deletar usuário por ID")
    @WithMockUser(roles = "USER")
    void testDelete_AsUser_ShouldBeForbidden() throws Exception {
        mockMvc.perform(delete("/usuarios/" + UUID.randomUUID()).with(csrf()))
                .andExpect(status().isForbidden());
    }

    // --- Testes para Endpoints de Usuário Autenticado --- //

    @Test
    @DisplayName("User autenticado deve conseguir buscar seu próprio perfil")
    @WithMockUser(roles = "USER")
    void testGetMyUser_AsUser_ShouldSucceed() throws Exception {
        when(usuarioService.getUsuarioLogado()).thenReturn(usuarioCompleto);
        mockMvc.perform(get("/usuarios/eu"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin autenticado deve conseguir buscar seu próprio perfil")
    @WithMockUser(roles = "ADMIN")
    void testGetMyUser_AsAdmin_ShouldSucceed() throws Exception {
        when(usuarioService.getUsuarioLogado()).thenReturn(usuarioCompleto);
        mockMvc.perform(get("/usuarios/eu"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Usuário não autenticado não deve conseguir buscar seu perfil")
    void testGetMyUser_AsUnauthenticated_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/usuarios/eu"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User autenticado deve conseguir deletar seu próprio perfil")
    @WithMockUser(roles = "USER")
    void testDeleteMyUser_AsUser_ShouldSucceed() throws Exception {
        mockMvc.perform(delete("/usuarios/eu").with(csrf()))
                .andExpect(status().isNoContent());
    }

    // --- Testes para Endpoints Públicos --- //

//    @Test
//    @DisplayName("Qualquer um deve conseguir criar um novo usuário")
//    void testSave_AsUnauthenticated_ShouldSucceed() throws Exception {
//        UsuarioDTO dto = new UsuarioDTO(usuarioCompleto);
//        when(usuarioService.save(any(Usuario.class))).thenReturn(usuarioCompleto);
//
//        mockMvc.perform(post("/usuarios").with(csrf())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk());
//    }
}