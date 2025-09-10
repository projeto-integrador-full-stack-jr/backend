package com.mentoria.back_end_mentoria.resumo;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResumoService {

    @Autowired
    private ResumoRepository resumoRepository;

    @Autowired
    private PerfilProfissionalRepository perfilProfissionalRepository;

    @Autowired
    private ChatModel chatModel;

    @Transactional(readOnly = true)
    public List<ResumoDTO> findAll() {
        List<Resumo> lista = resumoRepository.findAll();
        return lista.stream().map(ResumoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResumoDTO findById(UUID id) {
        Resumo entity = resumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resumo não encontrado"));
        return new ResumoDTO(entity);
    }

    @Transactional
    public ResumoDTO insert(ResumoDTO dto) {
        Resumo entity = new Resumo();
        entity.setResumoId(UUID.randomUUID());
        copyDtoToEntity(dto, entity);
        entity = resumoRepository.save(entity);
        return new ResumoDTO(entity);
    }

    @Transactional
    public ResumoDTO update(UUID id, ResumoDTO dto) {
        try {
            Resumo entity = resumoRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = resumoRepository.save(entity);
            return new ResumoDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resumo não encontrado com o id: " + id);
        }
    }

    public void delete(UUID id) {
        if (!resumoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resumo não encontrado com o id: " + id);
        }
        resumoRepository.deleteById(id);
    }

    @Transactional
    public ResumoDTO criarResumoIA(UUID perfilID) {
        PerfilProfissional perfil = perfilProfissionalRepository.findById(perfilID)
                .orElseThrow(() -> new ResourceNotFoundException("Esse perfil não existe"));

        String cargo = perfil.getCargo();
        String experiencia = perfil.getExperiencia();
        String objetivoProfissional = perfil.getObjetivoPrincipal();

        String tituloTexto = StringUtils.hasText(cargo) ? cargo : "Resumo de Carreira";

        var template = """
                Atue como um especialista em jornada de carreira e ajude essa pessoa a progredir
                em sua jornada, o cargo dela atual é {cargo}, a experiência profissional dela é de {experiencia}
                e seu objetivo é chegar até {objetivoProfissional}.
                
                Essa pessoa precisa de seus conselhos para planejar a sua carreira e com esse breve resumo,
                trace uma trajetória para ela.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Map<String, Object> params = Map.of(
                "cargo", StringUtils.hasText(cargo) ? cargo : "não informado",
                "experiencia", experiencia,
                "objetivoProfissional", objetivoProfissional
        );

        Prompt prompt = promptTemplate.create(params);
        Conteudo conteudo = new Conteudo(chatModel.call(prompt).getResult().getOutput().getText());
        Resumo novoResumo = new Resumo(UUID.randomUUID(), perfil, new Titulo(tituloTexto), conteudo);

        perfil.getResumos().add(novoResumo);
        perfilProfissionalRepository.save(perfil);

        return new ResumoDTO(novoResumo);
    }

    private void copyDtoToEntity(ResumoDTO dto, Resumo entity) {
        entity.setTitulo(new Titulo(dto.getTitulo()));
        entity.setConteudo(new Conteudo(dto.getConteudo()));

        if (dto.getPerfilProfissionalId() != null) {
            PerfilProfissional perfil = perfilProfissionalRepository.findById(dto.getPerfilProfissionalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Perfil Profissional não encontrado"));
            entity.setPerfilProfissional(perfil);
        }
    }
}