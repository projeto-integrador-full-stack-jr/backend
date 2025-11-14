package com.mentoria.back_end_mentoria.security;

import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = extrairToken(request);
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            processarToken(token);
        }
        filterChain.doFilter(request, response);
    }

    private void processarToken(String token) {
        try {
            String email = tokenService.getSubject(token);
            logger.debug("Subject extraído do token: {}", email);

            // CORREÇÃO: Alterado para findByEmail_Email
            Usuario usuario = usuarioRepository.findByEmail_Email(email);

            if (usuario == null) {
                logger.warn("Usuário não encontrado para email: {}", email);
                return;
            }

            logger.debug("Usuário encontrado: {}", usuario.getUsuarioId());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            usuario.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Autenticação definida para usuário: {}", email);

        } catch (Exception e) {
            logger.error("Erro ao processar token: {}", e.getMessage(), e);
        }
    }

    private String extrairToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            logger.debug("Token extraído: {}", token);
            return token;
        }
        return null;
    }
}