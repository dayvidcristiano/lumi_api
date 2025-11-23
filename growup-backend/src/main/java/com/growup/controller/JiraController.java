package com.growup.controller;

import com.growup.dto.JiraSyncRequest;
import com.growup.service.JiraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jira")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class JiraController {

    private final JiraService jiraService;

    @PostMapping("/sincronizar")
    public ResponseEntity<SyncResponse> sincronizarComJira(@RequestBody JiraSyncRequest request) {
        log.info("Sincronizando {} histórias com Jira", request.getHistoriaIds().size());

        try {
            jiraService.sincronizarHistoriasComJira(request.getHistoriaIds(), request.getJiraProjectKey());
            
            SyncResponse response = SyncResponse.builder()
                    .sucesso(true)
                    .mensagem("Histórias sincronizadas com sucesso")
                    .totalSincronizadas(request.getHistoriaIds().size())
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao sincronizar com Jira", e);
            
            SyncResponse response = SyncResponse.builder()
                    .sucesso(false)
                    .mensagem("Erro ao sincronizar: " + e.getMessage())
                    .totalSincronizadas(0)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @lombok.Data
    @lombok.Builder
    public static class SyncResponse {
        private Boolean sucesso;
        private String mensagem;
        private Integer totalSincronizadas;
    }
}
