package com.mentoria.back_end_mentoria.config;

import com.mentoria.back_end_mentoria.security.CustomOidcUserService;
import com.mentoria.back_end_mentoria.security.OAuth2LoginSuccessHandler;
import com.mentoria.back_end_mentoria.security.SecurityFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private CustomOidcUserService customOidcUserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    @Profile("prod")
    public SecurityFilterChain securityFilterChainProd(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou ausente")
                        )
                )
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login", "/usuarios").permitAll()

                        .requestMatchers(HttpMethod.GET, "/usuarios/eu").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/usuarios/eu").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/eu").authenticated()
                        .requestMatchers(HttpMethod.GET, "/perfis/meu").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/perfis/meu").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/perfis/meu").authenticated()
                        .requestMatchers(HttpMethod.GET, "/metas/minhas").authenticated()
                        .requestMatchers(HttpMethod.POST, "/metas/minhas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/metas/minhas/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/metas/minhas/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/notas/minhas").authenticated()
                        .requestMatchers(HttpMethod.POST, "/notas/minhas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/notas/minhas/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/notas/minhas/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/resumos/meus", "/resumos/meus/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/resumos/meus", "/resumos/meus/cv").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/resumos/meus/{id}").authenticated()

                        .requestMatchers("/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/perfis/**").hasRole("ADMIN")
                        .requestMatchers("/metas/**").hasRole("ADMIN")
                        .requestMatchers("/notas/**").hasRole("ADMIN")
                        .requestMatchers("/resumos/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Profile("test")
    public SecurityFilterChain securityFilterChainTest(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(toH2Console())
                )
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers(toH2Console()).permitAll()
                        .requestMatchers(HttpMethod.POST, "/login", "/usuarios").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "https://projeto-mentoria.vercel.app",
                "http://localhost:5173"
        ));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}