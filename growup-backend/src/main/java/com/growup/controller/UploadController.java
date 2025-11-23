package com.growup.controller;

import com.growup.dto.UploadDocumentoRequest;
import com.growup.dto.UserStoryDTO;
import com.growup.model.Projeto;
import com.growup.service.UserStoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UploadController {

    private final UserStoryService userStoryService;

    @PostMapping("/upload")
    public ResponseEntity<ProjetoResponse> uploadDocumento(@RequestBody UploadDocumentoRequest request) {
        log.info("Recebido upload de documento para projeto: {}", request.getNomeProjeto());

        try {
            Projeto projeto = userStoryService.processarDocumento(request);
            ProjetoResponse response = ProjetoResponse.builder()
                    .id(projeto.getId())
                    .nome(projeto.getNome())
                    .descricao(projeto.getDescricao())
                    .totalHistorias(projeto.getHistorias().size())
                    .historias(projeto.getHistorias().stream()
                            .map(h -> UserStoryDTO.builder()
                                    .id(h.getId())
                                    .papel(h.getPapel())
                                    .acao(h.getAcao())
                                    .beneficio(h.getBeneficio())
                                    .prioridade(h.getPrioridade().toString())
                                    .estimativa(h.getEstimativa())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Erro ao processar documento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{projetoId}/historias")
    public ResponseEntity<List<UserStoryDTO>> listarHistorias(@PathVariable Long projetoId) {
        log.info("Listando histórias do projeto: {}", projetoId);

        try {
            List<UserStoryDTO> historias = userStoryService.listarHistoriasPorProjeto(projetoId);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            log.error("Erro ao listar histórias", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{projetoId}/historias/nao-alocadas")
    public ResponseEntity<List<UserStoryDTO>> listarHistoriasNaoAlocadas(@PathVariable Long projetoId) {
        log.info("Listando histórias não alocadas do projeto: {}", projetoId);

        try {
            List<UserStoryDTO> historias = userStoryService.listarHistoriasNaoAlocadas(projetoId);
            return ResponseEntity.ok(historias);
        } catch (Exception e) {
            log.error("Erro ao listar histórias não alocadas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @lombok.Data
    @lombok.Builder
    public static class ProjetoResponse {
        private Long id;
        private String nome;
        private String descricao;
        private Integer totalHistorias;
        private List<UserStoryDTO> historias;
    }
}
