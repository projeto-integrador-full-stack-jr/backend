package com.mentoria.back_end_mentoria.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MentorIA API Documento")
                        .description("Documentação da API do projeto MentorIA")
                        .version("1.1.0")
                        .contact(new Contact()
                                .name("MentorIA S.A")
                                .url("https://projeto-mentoria.vercel.app/")));
    }
}
