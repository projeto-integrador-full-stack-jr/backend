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
import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.usuario.UsuarioRepository;
import com.mentoria.back_end_mentoria.usuario.vo.Email;
import com.mentoria.back_end_mentoria.usuario.vo.Senha;
import com.mentoria.back_end_mentoria.usuario.vo.UserRole;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Autowired
    private ResumoRepository resumoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Cria a senha com o valor original para passar na validação do Value Object
        Senha senhaAdmin = new Senha("Senha@123");
        // Criptografa a senha antes de salvar
        senhaAdmin.setValor(passwordEncoder.encode(senhaAdmin.getValor()));
        Usuario c1 = new Usuario(new Email("teste@teste.com"), senhaAdmin, UserRole.ADMIN);

        Senha senhaUser = new Senha("Senha@1234");
        senhaUser.setValor(passwordEncoder.encode(senhaUser.getValor()));
        Usuario c2 = new Usuario(new Email("teste2@teste.com"), senhaUser, UserRole.USER);

        usuarioRepository.save(c1);
        usuarioRepository.save(c2);

        PerfilProfissional pp = new PerfilProfissional(UUID.randomUUID(), c1, "Fulano", "Cabeça de lata", "Batedor de caixa", "Iniciante", "Ganhar dinheiro");
        perfilProfissionalRepository.save(pp);

        Meta m1 = new Meta(pp, new Titulo("Teste"), new Prazo(Instant.now().plus(1, ChronoUnit.DAYS)), StatusMeta.EM_ANDAMENTO);
        Nota n1 = new Nota(UUID.randomUUID(), pp, new Titulo("Teste"), new Conteudo("cabeça de limão"));
        Resumo ru = new Resumo(UUID.randomUUID(), pp, new Titulo("Teste"), new Conteudo("cabeça de limão"));

        resumoRepository.save(ru);
        notaRepository.save(n1);
        metaRepository.save(m1);

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