package com.growup.service;

import com.growup.dto.UserStoryDTO;
import com.growup.dto.UploadDocumentoRequest;
import com.growup.model.Projeto;
import com.growup.model.UserStory;
import com.growup.repository.ProjetoRepository;
import com.growup.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final ProjetoRepository projetoRepository;
    private final AIService aiService;

    @Transactional
    public Projeto processarDocumento(UploadDocumentoRequest request) {
        log.info("Processando documento para projeto: {}", request.getNomeProjeto());

        // Criar novo projeto
        Projeto projeto = Projeto.builder()
                .nome(request.getNomeProjeto())
                .contextoAdicional(request.getContextoAdicional())
                .descricao("Projeto criado a partir de documento")
                .build();

        projeto = projetoRepository.save(projeto);
        log.info("Projeto criado com ID: {}", projeto.getId());

        // Gerar histórias usando IA
        List<UserStory> historias = gerarHistoriasComIA(
                request.getConteudoDocumento(),
                request.getContextoAdicional(),
                projeto
        );

        projeto.setHistorias(historias);
        projeto = projetoRepository.save(projeto);

        log.info("Total de histórias geradas: {}", historias.size());
        return projeto;
    }

    private List<UserStory> gerarHistoriasComIA(String conteudo, String contexto, Projeto projeto) {
        try {
            String prompt = construirPrompt(conteudo, contexto);
            List<UserStory> historias = aiService.gerarHistorias(prompt);
            
            // Associar histórias ao projeto
            historias.forEach(h -> h.setProjeto(projeto));
            
            return userStoryRepository.saveAll(historias);
        } catch (Exception e) {
            log.error("Erro ao gerar histórias com IA", e);
            return gerarHistoriasSimuladas(projeto);
        }
    }

    private String construirPrompt(String conteudo, String contexto) {
        return String.format(
                "Analise o seguinte documento e gere histórias de usuário no formato: " +
                "papel|ação|benefício|prioridade|estimativa\n\n" +
                "Contexto adicional: %s\n\n" +
                "Documento:\n%s\n\n" +
                "Gere pelo menos 3 histórias de usuário bem estruturadas.",
                contexto, conteudo
        );
    }

    private List<UserStory> gerarHistoriasSimuladas(Projeto projeto) {
        log.warn("Gerando histórias simuladas (fallback)");
        List<UserStory> historias = new ArrayList<>();

        historias.add(UserStory.builder()
                .papel("usuário")
                .acao("fazer login no sistema")
                .beneficio("acessar minhas funcionalidades")
                .prioridade(UserStory.Prioridade.ALTA)
                .estimativa("4 tarefas")
                .projeto(projeto)
                .build());

        historias.add(UserStory.builder()
                .papel("gerente")
                .acao("visualizar relatórios")
                .beneficio("tomar decisões estratégicas")
                .prioridade(UserStory.Prioridade.ALTA)
                .estimativa("6 tarefas")
                .projeto(projeto)
                .build());

        historias.add(UserStory.builder()
                .papel("administrador")
                .acao("gerenciar usuários")
                .beneficio("garantir a segurança")
                .prioridade(UserStory.Prioridade.MEDIA)
                .estimativa("3 tarefas")
                .projeto(projeto)
                .build());

        return userStoryRepository.saveAll(historias);
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> listarHistoriasPorProjeto(Long projetoId) {
        return userStoryRepository.findByProjetoId(projetoId)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserStoryDTO> listarHistoriasNaoAlocadas(Long projetoId) {
        return userStoryRepository.findByProjetoIdAndSprintIsNull(projetoId)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserStoryDTO atualizarHistoria(Long id, UserStoryDTO dto) {
        UserStory historia = userStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("História não encontrada"));

        historia.setPapel(dto.getPapel());
        historia.setAcao(dto.getAcao());
        historia.setBeneficio(dto.getBeneficio());
        historia.setPrioridade(UserStory.Prioridade.valueOf(dto.getPrioridade()));
        historia.setEstimativa(dto.getEstimativa());

        historia = userStoryRepository.save(historia);
        return converterParaDTO(historia);
    }

    @Transactional
    public void deletarHistoria(Long id) {
        userStoryRepository.deleteById(id);
    }

    @Transactional
    public UserStoryDTO alocarEmSprint(Long historiaId, Long sprintId) {
        UserStory historia = userStoryRepository.findById(historiaId)
                .orElseThrow(() -> new RuntimeException("História não encontrada"));

        // Sprint será carregado pelo Hibernate
        historia.setSprint(null); // Será setado pelo controller/service de sprint
        historia = userStoryRepository.save(historia);

        return converterParaDTO(historia);
    }

    private UserStoryDTO converterParaDTO(UserStory historia) {
        return UserStoryDTO.builder()
                .id(historia.getId())
                .papel(historia.getPapel())
                .acao(historia.getAcao())
                .beneficio(historia.getBeneficio())
                .prioridade(historia.getPrioridade().toString())
                .estimativa(historia.getEstimativa())
                .sprintId(historia.getSprint() != null ? historia.getSprint().getId() : null)
                .jiraIssueKey(historia.getJiraIssueKey())
                .build();
    }
}
