package com.mentoria.back_end_mentoria.security;

import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository;
import com.mentoria.back_end_mentoria.usuario.vo.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve retornar UserDetails quando o usuário é encontrado")
    void testLoadUserByUsername_UserFound() {
        String email = "teste@example.com";
        Usuario usuarioMock = new Usuario();
        usuarioMock.setEmail(new Email(email));

        when(usuarioRepository.findByEmailEmail(email)).thenReturn(usuarioMock);

        UserDetails userDetails = autenticacaoService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o usuário não é encontrado")
    void testLoadUserByUsername_UserNotFound() {
        String email = "naoexiste@example.com";

        when(usuarioRepository.findByEmailEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> autenticacaoService.loadUserByUsername(email));
    }
}
