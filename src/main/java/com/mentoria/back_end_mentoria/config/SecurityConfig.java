package com.mentoria.back_end_mentoria.config;

import com.mentoria.back_end_mentoria.security.SecurityFilter;
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

    @Bean
    @Profile("prod")
    public SecurityFilterChain securityFilterChainProd(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();

                    req.requestMatchers(HttpMethod.GET, "/usuarios/eu").authenticated();
                    req.requestMatchers(HttpMethod.PUT, "/usuarios/eu").authenticated();
                    req.requestMatchers(HttpMethod.DELETE, "/usuarios/eu").authenticated();
                    req.requestMatchers(HttpMethod.GET, "/perfis/meu").authenticated();
                    req.requestMatchers(HttpMethod.PUT, "/perfis/meu").authenticated();
                    req.requestMatchers(HttpMethod.DELETE, "/perfis/meu").authenticated();
                    req.requestMatchers(HttpMethod.GET, "/metas/minhas").authenticated();
                    req.requestMatchers(HttpMethod.POST, "/metas/minhas").authenticated();
                    req.requestMatchers(HttpMethod.PUT, "/metas/minhas/{id}").authenticated();
                    req.requestMatchers(HttpMethod.DELETE, "/metas/minhas/{id}").authenticated();
                    req.requestMatchers(HttpMethod.GET, "/notas/minhas").authenticated();
                    req.requestMatchers(HttpMethod.POST, "/notas/minhas").authenticated();
                    req.requestMatchers(HttpMethod.PUT, "/notas/minhas/{id}").authenticated();
                    req.requestMatchers(HttpMethod.DELETE, "/notas/minhas/{id}").authenticated();
                    req.requestMatchers(HttpMethod.GET, "/resumos/meus").authenticated();
                    req.requestMatchers(HttpMethod.POST, "/resumos/meus").authenticated();
                    req.requestMatchers(HttpMethod.POST, "/resumos/meus/cv").authenticated();
                    req.requestMatchers(HttpMethod.DELETE, "/resumos/meus/{id}").authenticated();

                    req.requestMatchers(HttpMethod.GET, "/usuarios/listar").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/usuarios/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/usuarios/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/perfis").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/perfis/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/perfis").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/perfis/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/perfis/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/metas").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/metas/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/metas").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/metas/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/metas/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/notas").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/notas/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/notas").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/notas/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/notas/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/resumos").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/resumos/{id}").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/resumos").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/resumos/{id}").hasRole("ADMIN");

                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Profile("test")
    public SecurityFilterChain securityFilterChainTest(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()).disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(toH2Console()).permitAll();
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();

                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://projeto-mentoria.vercel.app", "http://localhost:5173/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}