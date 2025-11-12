package com.mentoria.back_end_mentoria.security;

import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository; // IMPORTAR
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser; // IMPORTAR
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;

    // Injetar o repositório para buscar o usuário
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${frontend.redirect.url}")
    private String frontendRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 1. Obtém o usuário autenticado (que foi carregado pelo CustomOidcUserService)
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        // 2. Extrair o email dele
        String email = oidcUser.getAttribute("email");

        // 3. Buscar o usuário completo no *nosso* banco de dados
        // (O CustomOidcUserService já garantiu que ele existe)
        Usuario usuario = (Usuario) usuarioRepository.findByEmailEmail(email);

        // 4. Gerar o JWT a partir do *nosso* objeto Usuario
        String token = tokenService.gerarToken(usuario);

        // 5. Constrói a URL de redirecionamento para o frontend com o token
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectUrl)
                .queryParam("token", token)
                .build().toUriString();

        // 6. Limpa qualquer autenticação antiga e redireciona
        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        //System.out.println(redirectUrl);
    }
}