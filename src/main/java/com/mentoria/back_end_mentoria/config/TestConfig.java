package com.mentoria.back_end_mentoria.config;

import com.mentoria.back_end_mentoria.meta.Meta;
import com.mentoria.back_end_mentoria.meta.MetaRepository;
import com.mentoria.back_end_mentoria.meta.vo.Prazo;
import com.mentoria.back_end_mentoria.meta.vo.StatusMeta;
import com.mentoria.back_end_mentoria.nota.Nota;
import com.mentoria.back_end_mentoria.nota.NotaRepository;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.resumo.Resumo;
import com.mentoria.back_end_mentoria.resumo.ResumoRepository;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository;
import com.mentoria.back_end_mentoria.usuario.vo.Email;
import com.mentoria.back_end_mentoria.usuario.vo.Senha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.UUID;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Autowired
    private ResumoRepository resumoRepository;

    @Override
    public void run(String... args) throws Exception {

        Usuario c1 = new Usuario(UUID.randomUUID(), new Email("teste@teste.com"), new Senha("Senha@123"));
        usuarioRepository.save(c1);

        PerfilProfissional pp = new PerfilProfissional(UUID.randomUUID(), c1, "Fulano", "Desenvolvedor Júnior", "Desenvolvimento de Software", "6 meses", "Se tornar Sênior");
        perfilProfissionalRepository.save(pp);

        Resumo ru = new Resumo(UUID.randomUUID(), pp, new Titulo("Resumo de Carreira"), new Conteudo("Este é um resumo gerado para o perfil."));
        resumoRepository.save(ru);

        Meta m1 = new Meta(pp, new Titulo("Estudar Spring Boot"), new Prazo(Instant.now().plusSeconds(864000)), StatusMeta.EM_ANDAMENTO);
        metaRepository.save(m1);

        Nota n1 = new Nota(UUID.randomUUID(), pp, new Titulo("Anotação Importante"), new Conteudo("Lembrar de refatorar o código amanhã."));
        notaRepository.save(n1);


        Usuario c2 = new Usuario(UUID.randomUUID(), new Email("exemplo@email.com"), new Senha("OutraSenha#456"));
        usuarioRepository.save(c2);

        PerfilProfissional pp2 = new PerfilProfissional(UUID.randomUUID(), c2, "Ciclano", "Analista de Dados Pleno", "Ciência de Dados", "2 anos", "Liderar projetos de Machine Learning");
        perfilProfissionalRepository.save(pp2);

        Resumo ru2 = new Resumo(UUID.randomUUID(), pp2, new Titulo("Experiência Profissional"), new Conteudo("Vasta experiência em análise de dados e modelagem preditiva."));
        resumoRepository.save(ru2);

        Meta m2 = new Meta(pp2, new Titulo("Aprender Python para Data Science"), new Prazo(Instant.now().plusSeconds(2592000)), StatusMeta.PENDENTE);
        metaRepository.save(m2);

        Nota n2 = new Nota(UUID.randomUUID(), pp2, new Titulo("Ideia para novo projeto"), new Conteudo("Criar um dashboard interativo com os dados de vendas."));
        notaRepository.save(n2);


        Usuario c3 = new Usuario(UUID.randomUUID(), new Email("dev@sistema.com"), new Senha("DevSenha!789"));
        usuarioRepository.save(c3);

        PerfilProfissional pp3 = new PerfilProfissional(UUID.randomUUID(), c3, "Beltrano", "Engenheiro de DevOps Sênior", "Infraestrutura e Cloud", "8 anos", "Otimizar a esteira de CI/CD");
        perfilProfissionalRepository.save(pp3);

        Resumo ru3 = new Resumo(UUID.randomUUID(), pp3, new Titulo("Sobre Mim"), new Conteudo("Especialista em automação de infraestrutura e orquestração de contêineres."));
        resumoRepository.save(ru3);

        Meta m3 = new Meta(pp3, new Titulo("Certificação AWS Solutions Architect"), new Prazo(Instant.now().plusSeconds(7776000)), StatusMeta.EM_ANDAMENTO);
        metaRepository.save(m3);

        Nota n3 = new Nota(UUID.randomUUID(), pp3, new Titulo("Reunião com a equipe"), new Conteudo("Discutir a migração para Kubernetes na próxima segunda-feira."));
        notaRepository.save(n3);
    }
}