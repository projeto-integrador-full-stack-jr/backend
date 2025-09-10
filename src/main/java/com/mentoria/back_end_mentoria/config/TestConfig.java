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

        // Adiciona o resumo ao perfil
        pp.getResumos().add(ru);
        perfilProfissionalRepository.save(pp);

        Meta m1 = new Meta(UUID.randomUUID(), c1, new Titulo("Estudar Spring Boot"), new Prazo(Instant.now().plusSeconds(864000)), StatusMeta.EM_ANDAMENTO);
        metaRepository.save(m1);

        Nota n1 = new Nota(UUID.randomUUID(), c1, new Titulo("Anotação Importante"), new Conteudo("Lembrar de refatorar o código amanhã."));
        notaRepository.save(n1);

        Usuario c2 = new Usuario(UUID.randomUUID(), new Email("exemplo@email.com"), new Senha("OutraSenha#456"));
        usuarioRepository.save(c2);

        PerfilProfissional pp2 = new PerfilProfissional(UUID.randomUUID(), c2, "Ciclano", "Analista de Dados Pleno", "Ciência de Dados", "2 anos", "Liderar projetos de Machine Learning");
        perfilProfissionalRepository.save(pp2);

        Resumo ru2 = new Resumo(UUID.randomUUID(), pp2, new Titulo("Experiência Profissional"), new Conteudo("Vasta experiência em análise de dados e modelagem preditiva."));
        resumoRepository.save(ru2);

        pp2.getResumos().add(ru2);
        perfilProfissionalRepository.save(pp2);

        Meta m2 = new Meta(UUID.randomUUID(), c2, new Titulo("Aprender Python para Data Science"), new Prazo(Instant.now().plusSeconds(2592000)), StatusMeta.PENDENTE);
        metaRepository.save(m2);

        Nota n2 = new Nota(UUID.randomUUID(), c2, new Titulo("Ideia para novo projeto"), new Conteudo("Criar um dashboard interativo com os dados de vendas."));
        notaRepository.save(n2);

        Usuario c3 = new Usuario(UUID.randomUUID(), new Email("dev@sistema.com"), new Senha("DevSenha!789"));
        usuarioRepository.save(c3);

        PerfilProfissional pp3 = new PerfilProfissional(UUID.randomUUID(), c3, "Beltrano", "Engenheiro de DevOps Sênior", "Infraestrutura e Cloud", "8 anos", "Otimizar a esteira de CI/CD");
        perfilProfissionalRepository.save(pp3);

        Resumo ru3 = new Resumo(UUID.randomUUID(), pp3, new Titulo("Sobre Mim"), new Conteudo("Especialista em automação de infraestrutura e orquestração de contêineres."));
        resumoRepository.save(ru3);

        pp3.getResumos().add(ru3);
        perfilProfissionalRepository.save(pp3);

        Meta m3 = new Meta(UUID.randomUUID(), c3, new Titulo("Certificação AWS Solutions Architect"), new Prazo(Instant.now().plusSeconds(7776000)), StatusMeta.EM_ANDAMENTO);
        metaRepository.save(m3);

        Nota n3 = new Nota(UUID.randomUUID(), c3, new Titulo("Reunião com a equipe"), new Conteudo("Discutir a migração para Kubernetes na próxima segunda-feira."));
        notaRepository.save(n3);
        /*
        ---- Nota Importante sobre essa classe!!! ----

        Você que chegou até aqui para entender o que é essa classe,
        deixo essa explicação:

        - Essa classe serve para testar a persistência das entidades no
        banco de dados em memória chamado H2. Ele funciona como se fosse
        um banco de dados comum, porém fica na memória RAM. Isso significa
        que ele é volátil: toda vez que o Spring for desligado, os dados
        serão apagados.

        Então por que usar o H2?
        Simples: para testar a interface JpaRepository de um modo que não
        polua as tabelas do banco de dados real (aquele que persiste de fato).
        No início do projeto usamos o H2 justamente para validar as entidades,
        controllers e services da API REST sem comprometer o banco definitivo.

        Eu já testei as entidades no H2 e funcionou bem. Mas caso as propriedades
        das entidades mudem, essa classe serve para retestar.

        Outra coisa importante: para evitar erros de persistência, é bom sempre
        analisar o construtor da classe antes de salvar. Se o construtor exigir
        outra entidade que ainda não exista no banco, vai dar erro de SQL.

        Exemplo:
        As classes com objetos n1, m1 e pp usam a entidade Usuario no construtor.
        Por isso, a classe c1 (Usuario) deve ser criada antes. Não tem como salvar
        um objeto que depende de outro se esse outro ainda nem existe.

        Qualquer dúvida, falar com o Alef.
*/
    }
}
