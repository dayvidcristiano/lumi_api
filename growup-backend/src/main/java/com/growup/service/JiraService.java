package com.growup.service;

import com.growup.model.UserStory;
import com.growup.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JiraService {

    private final UserStoryRepository userStoryRepository;
    private final JiraClient jiraClient;

    @Transactional
    public void sincronizarHistoriasComJira(List<Long> historiaIds, String jiraProjectKey) {
        log.info("Sincronizando {} histórias com Jira (projeto: {})", historiaIds.size(), jiraProjectKey);

        for (Long historiaId : historiaIds) {
            UserStory historia = userStoryRepository.findById(historiaId)
                    .orElseThrow(() -> new RuntimeException("História não encontrada"));

            try {
                String issueKey = jiraClient.criarIssue(historia, jiraProjectKey);
                historia.setJiraIssueKey(issueKey);
                userStoryRepository.save(historia);
                log.info("História {} sincronizada com Jira: {}", historiaId, issueKey);
            } catch (Exception e) {
                log.error("Erro ao sincronizar história {} com Jira", historiaId, e);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<UserStory> listarHistoriasSincronizadas() {
        return userStoryRepository.findAll().stream()
                .filter(h -> h.getJiraIssueKey() != null)
                .toList();
    }
}
