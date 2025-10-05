package com.mentoria.back_end_mentoria.security;

import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @InjectMocks
    private SecurityFilter securityFilter;

    @Mock
    private TokenService tokenService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        // Limpa o contexto de segurança antes de cada teste
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve autenticar o usuário com um token JWT válido")
    void testDoFilterInternal_WithValidToken() throws ServletException, IOException {
        String token = "token_jwt_valido";
        String email = "usuario@example.com";
        Usuario usuarioMock = new Usuario();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.getSubject(token)).thenReturn(email);
        when(usuarioRepository.findByEmailEmail(email)).thenReturn(usuarioMock);

        securityFilter.doFilterInternal(request, response, filterChain);

        // Verifica se o usuário foi autenticado e está no contexto de segurança
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(usuarioMock, SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // Verifica se o filtro continuou a cadeia
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Não deve autenticar se o token não for fornecido")
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        // Verifica se o contexto de segurança permanece vazio
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verifica se o filtro continuou a cadeia
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Não deve autenticar se o token for inválido")
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String token = "token_jwt_invalido";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        // Simula o TokenService lançando uma exceção para um token inválido
        when(tokenService.getSubject(token)).thenThrow(new RuntimeException("Token inválido"));

        securityFilter.doFilterInternal(request, response, filterChain);

        // Verifica se o contexto de segurança permanece vazio
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verifica se o filtro continuou a cadeia
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Não deve autenticar se o token não tiver o prefixo 'Bearer '")
    void testDoFilterInternal_NoBearerPrefix() throws ServletException, IOException {
        String token = "token_sem_prefixo";

        when(request.getHeader("Authorization")).thenReturn(token);

        securityFilter.doFilterInternal(request, response, filterChain);

        // Verifica se o contexto de segurança permanece vazio
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verifica se o filtro continuou a cadeia
        verify(filterChain).doFilter(request, response);
        // Garante que o tokenService nunca foi chamado
        verify(tokenService, never()).getSubject(any());
    }
}
