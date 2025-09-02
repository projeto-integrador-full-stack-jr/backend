package com.mentoria.back_end_mentoria.config;

import com.mentoria.back_end_mentoria.meta.Meta;
import com.mentoria.back_end_mentoria.meta.MetaRepository;
import com.mentoria.back_end_mentoria.meta.vo.Prazo;
import com.mentoria.back_end_mentoria.meta.vo.StatusMeta;
import com.mentoria.back_end_mentoria.nota.Nota;
import com.mentoria.back_end_mentoria.nota.NotaRepository;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
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

    @Override
    public void run(String... args) throws Exception {

        Usuario c1 = new Usuario(UUID.randomUUID(), new Email("teste@teste.com"), new Senha("Senha@123"));
        Meta m1 = new Meta(UUID.randomUUID(), c1, new Titulo("Teste"), new Prazo(Instant.now()), StatusMeta.EM_ANDAMENTO);
        Nota n1 = new Nota(UUID.randomUUID(), c1, new Titulo("Teste"), new Conteudo("cabeça de limão"));
        PerfilProfissional pp = new PerfilProfissional(UUID.randomUUID(), c1, "Fulano", "Cabeça de lata", "Batedor de caixa", "Iniciante", "Ganhar dinheiro");


        usuarioRepository.save(c1);
        perfilProfissionalRepository.save(pp);
        notaRepository.save(n1);
        metaRepository.save(m1);
    }
}
