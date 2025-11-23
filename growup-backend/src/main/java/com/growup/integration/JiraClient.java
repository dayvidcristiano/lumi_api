package com.growup.integration;

import com.growup.model.UserStory;
import com.growup.service.JiraClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JiraClientImpl implements JiraClient {

    private final WebClient webClient;

    @Value("${jira.api.url}")
    private String jiraApiUrl;

    @Value("${jira.api.email}")
    private String jiraEmail;

    @Value("${jira.api.token}")
    private String jiraToken;

    @Override
    public String criarIssue(UserStory historia, String projectKey) {
        log.info("Criando issue no Jira para história: {}", historia.getAcao());

        try {
            Map<String, Object> requestBody = construirCorpoRequisicao(historia, projectKey);

            String response = webClient.post()
                    .uri(jiraApiUrl + "/rest/api/3/issue")
                    .header("Authorization", "Basic " + gerarBasicAuth())
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse da resposta para extrair a chave do issue
            String issueKey = extrairIssueKey(response);
            log.info("Issue criada com sucesso: {}", issueKey);

            return issueKey;
        } catch (Exception e) {
            log.error("Erro ao criar issue no Jira", e);
            throw new RuntimeException("Erro ao sincronizar com Jira: " + e.getMessage());
        }
    }

    private Map<String, Object> construirCorpoRequisicao(UserStory historia, String projectKey) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("project", Map.of("key", projectKey));
        fields.put("summary", String.format("Como %s, eu quero %s", historia.getPapel(), historia.getAcao()));
        fields.put("description", String.format(
                "Como %s, eu quero %s para %s\n\nEstimativa: %s\nPrioridade: %s",
                historia.getPapel(),
                historia.getAcao(),
                historia.getBeneficio(),
                historia.getEstimativa(),
                historia.getPrioridade()
        ));
        fields.put("issuetype", Map.of("name", "Story"));
        fields.put("priority", Map.of("name", mapearPrioridade(historia.getPrioridade())));

        Map<String, Object> request = new HashMap<>();
        request.put("fields", fields);

        return request;
    }

    private String mapearPrioridade(UserStory.Prioridade prioridade) {
        return switch (prioridade) {
            case ALTA -> "Highest";
            case MEDIA -> "Medium";
            case BAIXA -> "Low";
        };
    }

    private String gerarBasicAuth() {
        String credentials = jiraEmail + ":" + jiraToken;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    private String extrairIssueKey(String response) {
        // Parse simples - em produção, usar JSON parser
        if (response != null && response.contains("\"key\"")) {
            int start = response.indexOf("\"key\":\"") + 7;
            int end = response.indexOf("\"", start);
            return response.substring(start, end);
        }
        return "UNKNOWN";
    }
}
