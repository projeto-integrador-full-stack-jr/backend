package com.mentoria.back_end_mentoria.security;

import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository;
import com.mentoria.back_end_mentoria.usuario.vo.Email;
import com.mentoria.back_end_mentoria.usuario.vo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String email = oidcUser.getAttribute("email");
        String imageUrl = oidcUser.getAttribute("picture");

        Usuario usuario = findOrCreateUser(email, imageUrl);

        return new DefaultOidcUser(usuario.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
    }

    private Usuario findOrCreateUser(String email, String imageUrl) {
        // CORREÇÃO: Alterado para findByEmail_Email
        Usuario usuario = usuarioRepository.findByEmail_Email(email);

        if (usuario != null) {
            if (imageUrl != null && !imageUrl.equals(usuario.getImageUrl())) {
                usuario.setImageUrl(imageUrl);
                usuarioRepository.save(usuario);
            }
        } else {
            usuario = new Usuario(new Email(email), null, UserRole.USER, imageUrl);
            usuarioRepository.save(usuario);
        }
        return usuario;
    }
}