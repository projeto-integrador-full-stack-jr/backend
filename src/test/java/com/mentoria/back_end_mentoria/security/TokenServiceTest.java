package com.mentoria.back_end_mentoria.security;

import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.usuario.vo.Email;
import com.mentoria.back_end_mentoria.usuario.vo.Senha;
import com.mentoria.back_end_mentoria.usuario.vo.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Injeta um segredo de teste no TokenService
        // Este segredo deve ter pelo menos 256 bits (32 bytes) para ser compatível com HS256
        String testSecret = "meu_segredo_de_teste_super_longo_e_seguro_para_hs256";
        ReflectionTestUtils.setField(tokenService, "secret", testSecret);

        usuario = new Usuario(new Email("teste@example.com"), new Senha("Senha@123"), UserRole.USER);
    }

    @Test
    @DisplayName("Deve gerar um token JWT válido")
    void testGerarToken() {
        String token = tokenService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        // Verifica se o token tem a estrutura de um JWT (3 partes separadas por ponto)
        assertTrue(token.matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$"));
    }

    @Test
    @DisplayName("Deve extrair o subject (email) de um token válido")
    void testGetSubject() {
        String token = tokenService.gerarToken(usuario);
        String subject = tokenService.getSubject(token);

        assertEquals(usuario.getUsername(), subject);
    }

    @Test
    @DisplayName("Deve lançar RuntimeException para um token inválido ou malformado")
    void testGetSubject_InvalidToken() {
        String invalidToken = "um.token.invalido";

        assertThrows(RuntimeException.class, () -> tokenService.getSubject(invalidToken));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException para um token com assinatura incorreta")
    void testGetSubject_WrongSignature() {
        String token = tokenService.gerarToken(usuario);

        // Altera o segredo para simular uma assinatura inválida
        ReflectionTestUtils.setField(tokenService, "secret", "outro_segredo_diferente_e_tambem_longo_suficiente");

        assertThrows(RuntimeException.class, () -> tokenService.getSubject(token));
    }
}
