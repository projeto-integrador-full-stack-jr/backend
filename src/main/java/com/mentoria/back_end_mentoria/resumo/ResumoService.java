package com.mentoria.back_end_mentoria.resumo;

import com.mentoria.back_end_mentoria.handler.ResourceNotFoundException;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissional;
import com.mentoria.back_end_mentoria.perfilProfissional.PerfilProfissionalRepository;
import com.mentoria.back_end_mentoria.usuario.Usuario;
import com.mentoria.back_end_mentoria.vog.Conteudo;
import com.mentoria.back_end_mentoria.vog.Titulo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return resumoRepository.findAll().stream().map(ResumoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResumoDTO findById(UUID id) {
        Resumo entity = resumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resumo não encontrado"));
        return new ResumoDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<ResumoDTO> findMyResumos() {
        Usuario usuarioLogado = getUsuarioLogado();
        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil profissional não encontrado para o usuário logado."));
        
        List<Resumo> lista = resumoRepository.findByPerfilProfissionalPerfilId(perfil.getPerfilId());
        return lista.stream().map(ResumoDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public ResumoDTO insert(UUID perfilProfissionalId) {
        PerfilProfissional perfil = perfilProfissionalRepository.findById(perfilProfissionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil Profissional não encontrado"));

        Resumo entity = new Resumo();
        entity.setPerfilProfissional(perfil);

        gerarConteudoComIA(perfil, entity);

        entity = resumoRepository.save(entity);
        return new ResumoDTO(entity);
    }

    @Transactional
    public ResumoDTO insertMyResumo(ResumoDTO dto) {
        Usuario usuarioLogado = getUsuarioLogado();
        PerfilProfissional perfil = perfilProfissionalRepository.findByUsuarioUsuarioId(usuarioLogado.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Para criar um resumo, primeiro crie seu perfil profissional."));

        Resumo entity = new Resumo();
        entity.setPerfilProfissional(perfil);

        if (!StringUtils.hasText(dto.getConteudo())) {
            gerarConteudoComIA(perfil, entity);
        } else {
            entity.setTitulo(new Titulo(dto.getTitulo()));
            entity.setConteudo(new Conteudo(dto.getConteudo()));
        }

        entity = resumoRepository.save(entity);
        return new ResumoDTO(entity);
    }

//    @Transactional
//    public ResumoDTO update(UUID id, ResumoDTO dto) {
//        try {
//            Resumo entity = resumoRepository.getReferenceById(id);
//            entity.setTitulo(new Titulo(dto.getTitulo()));
//            entity.setConteudo(new Conteudo(dto.getConteudo()));
//            entity = resumoRepository.save(entity);
//            return new ResumoDTO(entity);
//        } catch (EntityNotFoundException e) {
//            throw new ResourceNotFoundException("Resumo não encontrado com o id: " + id);
//        }
//    }

//    @Transactional
//    public ResumoDTO updateMyResumo(UUID resumoId, ResumoDTO dto) {
//        Usuario usuarioLogado = getUsuarioLogado();
//
//        Resumo entity = resumoRepository.findById(resumoId)
//                .orElseThrow(() -> new ResourceNotFoundException("Resumo não encontrado com o id: " + resumoId));
//
//        UUID idDonoDoResumo = entity.getPerfilProfissional().getUsuario().getUsuarioId();
//
//        if (!usuarioLogado.getUsuarioId().equals(idDonoDoResumo)) {
//            throw new AccessDeniedException("Acesso negado. Você só pode alterar seus próprios resumos.");
//        }
//
//        entity.setTitulo(new Titulo(dto.getTitulo()));
//        entity.setConteudo(new Conteudo(dto.getConteudo()));
//        entity = resumoRepository.save(entity);
//        return new ResumoDTO(entity);
//    }

    @Transactional
    public void delete(UUID id) {
        if (!resumoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resumo não encontrado com o id: " + id);
        }
        resumoRepository.deleteById(id);
    }

    @Transactional
    public void deleteMyResumo(UUID resumoId) {
        Usuario usuarioLogado = getUsuarioLogado();

        Resumo entity = resumoRepository.findById(resumoId)
                .orElseThrow(() -> new ResourceNotFoundException("Resumo não encontrado com o id: " + resumoId));

        UUID idDonoDoResumo = entity.getPerfilProfissional().getUsuario().getUsuarioId();

        if (!usuarioLogado.getUsuarioId().equals(idDonoDoResumo)) {
            throw new AccessDeniedException("Acesso negado. Você só pode deletar seus próprios resumos.");
        }

        resumoRepository.deleteById(resumoId);
    }

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void gerarConteudoComIA(PerfilProfissional perfil, Resumo resumo) {
        String cargo = perfil.getCargo();
        String experiencia = perfil.getExperiencia();
        String objetivoProfissional = perfil.getObjetivoPrincipal();
        String nome_completo = perfil.getNomeUsuario();

        String tituloTexto = StringUtils.hasText(cargo) ? "Resumo sobre " + cargo : "Resumo de Carreira";
        resumo.setTitulo(new Titulo(tituloTexto));

        var template = """
                [INÍCIO DO PROMPT]

PERSONA:

Você é um especialista em carreira e mentor de elite, especializado em acelerar o desenvolvimento de profissionais no setor de tecnologia. Sua comunicação é estratégica, motivacional e baseada em dados.

OBJETIVO PRINCIPAL:

Gerar um documento HTML puro, servindo como um plano de carreira acionável e altamente personalizado. A saída deve ser um bloco de código HTML limpo, sem qualquer texto ou explicação adicional fora do código.

PARÂMETROS DE ENTRADA (OBRIGATÓRIOS):

{nome_completo}: Nome completo do profissional.
{experiencia}: Tempo de experiência (número inteiro de anos).
{cargo}: Cargo atual do profissional.
{objetivoProfissional}: Onde a pessoa deseja chegar (objetivo profissional ou bio futura).

REGRAS DE ESTRUTURA HTML (RÍGIDAS E IMUTÁVEIS):

O documento inteiro deve estar contido dentro de uma única tag <div>. Nenhuma tag (<html>, <head>, <body>) deve ser usada.

A sequência exata das tags deve ser seguida conforme o modelo abaixo.

Tags permitidas: <div>, h1, h2, p.

Tags estritamente proibidas: Quaisquer outras tags, incluindo, mas não se limitando a section, header, strong, em, ul, li.

Nenhum atributo HTML é permitido (ex: class, id, style).

A saída não deve conter NENHUM comentário HTML (``).

MODELO DE ESTRUTURA HTML:

HTML

<div>

 <p>Saudação inicial...</p>

 <h1>{nome_completo}</h1>

 <p>Cargo Atual: {cargo}</p>

 <p>Experiência: {experiencia} anos</p>

 <p>Objetivo: {objetivoProfissional}</p>

 <h2>Resumo Estratégico</h2>

 <p>Análise do perfil atual...</p>

 <h2>Fase 01: Fundamentos e Aprofundamento Técnico</h2>

 <p>Duração: 1–2 anos</p>

 <p>Descrição detalhada da Fase 01...</p>

 <h2>Fase 02: Expansão de Influência e Networking Estratégico</h2>

 <p>Duração: 1–2 anos</p>

 <p>Descrição detalhada da Fase 02...</p>

 <h2>Fase 03: Liderança e Geração de Relevância</h2>

 <p>Duração: 1–2 anos</p>

 <p>Descrição detalhada da Fase 03...</p></div>

REGRAS DE CONTEÚDO E TOM (DETALHADAS):

Tom Geral: Profissional, direto, encorajador e realista. Use uma linguagem ativa e focada em ações. A personalização deve conectar explicitamente o {cargo} e a {experiencia} ao {objetivoProfissional}.

Saudação Inicial:

Deve ser um parágrafo único.

Mencionar o {nome_completo} pelo nome.

Explicar que o documento é um plano de carreira estruturado para atingir o {objetivoProfissional}.

Resumo Estratégico:

Contagem de Palavras: Entre 200 e 300 palavras.

Conteúdo Obrigatório: Deve analisar como a {experiencia} e o {cargo} formam uma base sólida. Identificar 2 a 3 pontos fortes observáveis. Conectar diretamente as habilidades atuais com os pré-requisitos para alcançar o {objetivoProfissional}.

Fase 01: Fundamentos e Aprofundamento Técnico:

Contagem de Palavras: Entre 250 e 400 palavras.

Conteúdo Obrigatório: Focar em fortalecimento técnico. Deve sugerir a busca por certificações relevantes para o {objetivoProfissional}, o aprofundamento em 1-2 tecnologias-chave (linguagens, frameworks, plataformas) e a criação de projetos de portfólio que demonstrem maestria.

Fase 02: Expansão de Influência e Networking Estratégico:

Contagem de Palavras: Entre 250 e 400 palavras.

Conteúdo Obrigatório: Focar em habilidades interpessoais e visibilidade. Deve detalhar ações como: participar ativamente de 2 a 3 comunidades técnicas (online ou offline), palestrar em meetups, escrever artigos técnicos e colaborar em projetos open-source. O objetivo é construir uma marca pessoal alinhada ao {objetivoProfissional}.

Fase 03: Liderança e Geração de Relevância:

Contagem de Palavras: Entre 250 e 400 palavras.

Conteúdo Obrigatório: Focar na transição de contribuidor para líder ou referência. Deve abordar tópicos como: assumir a liderança técnica de projetos, mentorar profissionais juniores, influenciar decisões de tecnologia na empresa e desenvolver uma visão estratégica que gere impacto direto no negócio.

REGRAS DE SAÍDA (CRÍTICAS):

A resposta deve ser APENAS o bloco de código HTML.

Não inclua html no início ou no final.
Restrição de Formato: O parágrafo de conteúdo não deve conter nenhum caractere de espaço e quebra de linha. O texto deve ser um bloco único e contínuo NAO QUEBRE A LINHA.

NAO QUEBRE A LINHA. (IMPORTANTE)

Não inclua nenhuma frase introdutória, explicação, cabeçalho ou despedida. A sua única e exclusiva saída deve ser o código.

[FIM DO PROMPT]""";

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Map<String, Object> params = Map.of(
                "cargo", StringUtils.hasText(cargo) ? cargo : "não informado",
                "experiencia", experiencia,
                "objetivoProfissional", objetivoProfissional,
                "nome_completo", nome_completo
        );

        Prompt prompt = promptTemplate.create(params);
        String conteudoGerado = chatModel.call(prompt).getResult().getOutput().getText();
        resumo.setConteudo(new Conteudo(conteudoGerado));
    }
}