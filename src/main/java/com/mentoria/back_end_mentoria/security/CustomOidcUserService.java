package com.mentoria.back_end_mentoria.security;

import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository;
import com.mentoria.back_end_mentoria.usuario.vo.Email;
import com.mentoria.back_end_mentoria.usuario.vo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService { // Note a mudança aqui

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. Delega ao OidcUserService padrão para obter o usuário OIDC
        OidcUser oidcUser = super.loadUser(userRequest);

        // 2. Extrai os atributos
        String email = oidcUser.getAttribute("email");
        String imageUrl = oidcUser.getAttribute("picture");

        // 3. Procura ou cria o usuário no seu banco
        Usuario usuario = findOrCreateUser(email, imageUrl);

        // 4. Cria um novo principal OIDC, mas agora com as authorities do *nosso* usuário
        // Isso garante que o Spring use as roles (ADMIN/USER) do nosso banco.
        // O "email" como último argumento diz ao Spring qual atributo usar para principal.getName()
        return new DefaultOidcUser(usuario.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
    }

    private Usuario findOrCreateUser(String email, String imageUrl) {
        UserDetails userDetails = usuarioRepository.findByEmailEmail(email);
        Usuario usuario;

        if (userDetails != null) {
            // Usuário encontrado
            usuario = (Usuario) userDetails;

            // Atualiza a imagem do perfil se ela mudou no Google
            if (imageUrl != null && !imageUrl.equals(usuario.getImageUrl())) {
                usuario.setImageUrl(imageUrl);
                usuarioRepository.save(usuario);
            }
        } else {
            // Usuário novo (senha nula, pois é login social)
            usuario = new Usuario(new Email(email), null, UserRole.USER, imageUrl);
            usuarioRepository.save(usuario);
        }
        return usuario;
    }
}