package com.mentoria.back_end_mentoria.resumo;

import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResumoService {

    @Autowired
    private ResumoRepository resumoRepository;

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Autowired
    private ChatModel chatModel;

    public Resumo criarResumoIA(UUID perfilID) {
        Optional<PerfilProfissional> obj = perfilProfissionalRepository.findById(perfilID);

        if (!obj.isPresent()) {
            throw new RuntimeException("Esse perfil não existe");
        }

        PerfilProfissional perfil = obj.get();
        String cargo = perfil.getCargo();
        String experiencia = perfil.getExperiencia();
        String objetivoProfissional = perfil.getObjetivoPrincipal();

        var template = """
                Atue como um especialista em jornada de carreira e ajude essa pessoa a processe gir\s
                em sua jornada, o cargo dela atual é {cargo}, a experiência profissional dela é de {experiencia}
                e seu objetivo é chegar até {objetivoProfissional}.
                
                Essa pessoa precisa de seus conselhos para planejar a sua carreira e com esse breve resumo.
                trace uma trajetória para ela.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Map<String, Object> params = Map.of(
                "cargo", cargo,
                "experiencia", experiencia,
                "objetivoProfissional", objetivoProfissional
        );

        Prompt prompt = promptTemplate.create(params);

        Conteudo conteudo = new Conteudo(chatModel.call(prompt).getResult().getOutput().getText());

        Resumo novoResumo = new Resumo(UUID.randomUUID(), perfil, new Titulo(cargo), conteudo);

        return resumoRepository.save(novoResumo);
    }

    public List<Resumo> findAll() {
        return resumoRepository.findAll();
    }
}

