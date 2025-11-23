package com.mentoria.back_end_mentoria.resumo;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        return resumoRepository.findAll().stream().map(ResumoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public ResumoDTO findById(UUID id) {
        Resumo entity = resumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resumo não encontrado"));
        return new ResumoDTO(entity);
    }

    @Transactional
    public ResumoDTO insert(UUID perfilProfissionalId) {
        PerfilProfissional perfil = perfilProfissionalRepository.findById(perfilProfissionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil Profissional não encontrado"));

        Resumo entity = new Resumo();
        entity.setPerfilProfissional(perfil);

        gerarConteudoComIA(perfil, entity, null);

        entity = resumoRepository.save(entity);
        return new ResumoDTO(entity);
    }

    @Transactional
    public void delete(UUID id) {
        if (!resumoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resumo não encontrado com o id: " + id);
        }
        resumoRepository.deleteById(id);
    }

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional(readOnly = true)
    public List<ResumoDTO> findMyResumos() {
        Usuario usuarioLogado = getUsuarioLogado();
        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil profissional não encontrado para o usuário logado."));

        List<Resumo> lista = resumoRepository.findByPerfilProfissionalPerfilId(perfil.getPerfilId());
        return lista.stream().map(ResumoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public ResumoDTO findMyResumoPerID(UUID id) {
        Usuario usuarioLogado = getUsuarioLogado();
        perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil profissional não encontrado para o usuário logado."));

        Resumo resumo = resumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resumo não encontrado com o ID: " + id));

        if (!resumo.getPerfilProfissional().getUsuario().getUsuarioId().equals(usuarioLogado.getUsuarioId())) {
            throw new AccessDeniedException("Você não tem permissão para acessar este resumo.");
        }

        return new ResumoDTO(resumo);
    }

    @Transactional
    public void deleteMyResumo(UUID resumoId) {
        Usuario usuarioLogado = getUsuarioLogado();
        Resumo entity = resumoRepository.findById(resumoId)
                .orElseThrow(() -> new ResourceNotFoundException("Resumo não encontrado com o id: " + resumoId));

        UUID idDonoDoResumo = entity.getPerfilProfissional().getUsuario().getUsuarioId();

        if (!usuarioLogado.getUsuarioId().equals(idDonoDoResumo)) {
            throw new AccessDeniedException("Acesso negado. Você só pode apagar seus próprios resumos.");
        }

        resumoRepository.deleteById(resumoId);
    }

    @Transactional
    public ResumoDTO insertMyResumo() {
        Usuario usuarioLogado = getUsuarioLogado();
        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Para criar um resumo, primeiro crie seu perfil profissional."));

        Resumo entity = new Resumo();
        entity.setPerfilProfissional(perfil);

        gerarConteudoComIA(perfil, entity, null);

        entity = resumoRepository.save(entity);
        return new ResumoDTO(entity);
    }

    @Transactional
    public ResumoDTO insertPDF(MultipartFile file) {
        Usuario usuarioLogado = getUsuarioLogado();
        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Para criar um resumo, primeiro crie seu perfil profissional."));

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            if (document.getNumberOfPages() > 4) {
                throw new IllegalArgumentException("O PDF não pode ter mais de 4 páginas.");
            }
            String textPDFCru = new PDFTextStripper().getText(document);
            String ExperienciaProfissional = extratorExperienciaProfissionalDoCurriculo(textPDFCru);
            Resumo resumo = new Resumo();
            resumo.setPerfilProfissional(perfil);
            gerarConteudoComIA(perfil, resumo, ExperienciaProfissional);
            resumo = resumoRepository.save(resumo);
            return new ResumoDTO(resumo);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível ler o arquivo PDF.", e);
        }
    }

    private String extratorExperienciaProfissionalDoCurriculo(String textPDFCru) {
        if (textPDFCru == null || textPDFCru.isEmpty()) {
            throw new IllegalArgumentException("O texto extraído do currículo está com algum problema.");
        }
        PromptTemplate promptTemplate = new PromptTemplate(templateCurriculoInstrucoes);
        Map<String, Object> params = Map.of(
                "conteudo_curriculo", textPDFCru
        );
        ChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-4.1-nano")
                .temperature(0.5)
                .build();
        Message message = (promptTemplate.create(params)).getUserMessage();
        ChatResponse chatResponse = chatModel.call(new Prompt(message, options));
        return chatResponse.getResult().getOutput().getText();
    }

    private void gerarConteudoComIA(PerfilProfissional perfil, Resumo resumo, String experienciaCurriculo) {
        String cargo = perfil.getCargo();
        String experiencia = perfil.getExperiencia();
        if (experienciaCurriculo != null && !experienciaCurriculo.isBlank()) {
            experiencia = experienciaCurriculo;
        }
        String objetivoProfissional = perfil.getObjetivoPrincipal();
        String nome_completo = perfil.getNomeUsuario();
        String tituloTexto = StringUtils.hasText(cargo) ? "Resumo sobre " + cargo : "Resumo de Carreira";
        resumo.setTitulo(new Titulo(tituloTexto));

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Map<String, Object> params = Map.of(
                "cargo", StringUtils.hasText(cargo) ? cargo : "não informado",
                "experiencia", StringUtils.hasText(experiencia) ? experiencia : "não informada",
                "objetivoProfissional", StringUtils.hasText(objetivoProfissional) ? objetivoProfissional : "não informado",
                "nome_completo", StringUtils.hasText(nome_completo) ? nome_completo : "Candidato"
        );
        Message message = (promptTemplate.create(params)).getUserMessage();
        ChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-4.1")
                .temperature(0.6)
                .build();

        ChatResponse response = chatModel.call(new Prompt(message, options));

        String conteudoGerado = response.getResult().getOutput().getText();
        if (conteudoGerado == null) conteudoGerado = "";
        conteudoGerado = conteudoGerado.replace("```", "");
        conteudoGerado = conteudoGerado.replace("\\n", " ");
        conteudoGerado = conteudoGerado.replace("\r", " ");
        conteudoGerado = conteudoGerado.replaceFirst("^\\s*html\\s*", "");
        conteudoGerado = conteudoGerado.trim();
        resumo.setConteudo(new Conteudo(conteudoGerado));
    }

    private final String templateCurriculoInstrucoes = """
            Leia o texto a seguir: {conteudo_curriculo}.
            Extraia **apenas** as experiências profissionais presentes no currículo.
            Ignore qualquer informação que não seja experiência profissional.
            A resposta deve conter **somente** os dados das experiências profissionais, sem comentários adicionais.
            """;

    private final String template = """
            [INÍCIO DO PROMPT]
            
            PERSONA:
            Você é um especialista em carreira e mentor de elite, focado em desenvolver profissionais de tecnologia. Sua comunicação é estratégica, motivacional e baseada em dados.
            
            OBJETIVO PRINCIPAL:
            Gerar um único documento HTML puro, contendo um plano de carreira estruturado. A resposta deve ser SOMENTE o código HTML, sem explicações, sem comentários e sem bloco de código (sem ```).
            
            PARÂMETROS DE ENTRADA (OBRIGATÓRIOS):
            {nome_completo}: Nome completo do profissional.
            {experiencia}: Tempo de experiência.
            {cargo}: Cargo atual do profissional.
            {objetivoProfissional}: Onde a pessoa deseja chegar (objetivo profissional ou bio futura).
            
            REGRAS ESTRITAS DE HTML:
            - Todo o conteúdo deve estar DENTRO de uma única tag <div>.
            - Não usar <html>, <head>, <body>, ou qualquer tag fora: <div>, <h1>, <h2>, <p>.
            - Nenhum atributo é permitido (sem class, id, style).
            - Nenhum comentário HTML é permitido.
            - A ordem das tags deve seguir exatamente o modelo abaixo.
            - A saída deve ser um ÚNICO parágrafo contínuo, SEM QUEBRAS DE LINHA, SEM \n, SEM \r.
            - O modelo abaixo indica a sequência obrigatória:
            
            <div>
            <p>Saudação inicial...</p>
            <h1>{nome_completo}</h1>
            <p>Cargo Atual: {cargo}</p>
            <p>Objetivo: {objetivoProfissional}</p>
            <h2>Resumo Estratégico</h2>
            <p>Texto...</p>
            <h2>Fase 01: Fundamentos e Aprofundamento Técnico</h2>
            <p>Duração: 1–2 anos</p>
            <p>Texto...</p>
            <h2>Fase 02: Expansão de Influência e Networking Estratégico</h2>
            <p>Duração: 1–2 anos</p>
            <p>Texto...</p>
            <h2>Fase 03: Liderança e Geração de Relevância</h2>
            <p>Duração: 1–2 anos</p>
            <p>Texto...</p>
            </div>
            
            REGRAS DE CONTEÚDO:
            - Saudação: parágrafo único, citando {nome_completo} e explicando que é um plano para atingir {objetivoProfissional}.
            - Resumo Estratégico: 200 a 300 palavras, analisando {experiencia}, {cargo}, pontos fortes e conexão com o objetivo.
            - Fase 01: 250 a 400 palavras, focada em base técnica, tecnologias, certificações e portfólio.
            - Fase 02: 250 a 400 palavras, focada em networking, comunidades, artigos, palestras e open-source.
            - Fase 03: 250 a 400 palavras, focada em liderança, mentoring, decisões técnicas e visão estratégica.
            
            REGRAS FINAIS (CRÍTICAS):
            - A resposta deve ser SOMENTE o HTML puro.
            - NÃO iniciar com "html", não colocar "```".
            - NÃO gerar nenhum "\n" ou quebra de linha. O HTML deve vir em uma única linha contínua.
            - NÃO incluir explicações, avisos, comentários ou textos fora do <div>.
            
            [FIM DO PROMPT]
            """;
}