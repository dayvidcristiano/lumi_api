package com.growup.controller;

import com.growup.dto.SprintDTO;
import com.growup.model.Sprint;
import com.growup.service.SprintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sprints")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RoadmapController {

    private final SprintService sprintService;

    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<SprintDTO>> listarSprintsPorProjeto(@PathVariable Long projetoId) {
        log.info("Listando sprints do projeto: {}", projetoId);

        try {
            List<Sprint> sprints = sprintService.listarSprintsPorProjeto(projetoId);
            List<SprintDTO> dtos = sprints.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            log.error("Erro ao listar sprints", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<SprintDTO> criarSprint(@RequestBody SprintDTO dto) {
        log.info("Criando sprint: {}", dto.getTitulo());

        try {
            Sprint sprint = Sprint.builder()
                    .titulo(dto.getTitulo())
                    .periodo(dto.getPeriodo())
                    .build();

            Sprint criada = sprintService.criarSprint(sprint);
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(criada));
        } catch (Exception e) {
            log.error("Erro ao criar sprint", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{sprintId}/alocar-historia/{historiaId}")
    public ResponseEntity<Void> alocarHistoriaEmSprint(
            @PathVariable Long sprintId,
            @PathVariable Long historiaId) {
        log.info("Alocando história {} em sprint {}", historiaId, sprintId);

        try {
            sprintService.alocarHistoriaEmSprint(historiaId, sprintId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao alocar história em sprint", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{sprintId}/desalocar-historia/{historiaId}")
    public ResponseEntity<Void> desalocarHistoriaDeSprint(
            @PathVariable Long sprintId,
            @PathVariable Long historiaId) {
        log.info("Desalocando história {} da sprint {}", historiaId, sprintId);

        try {
            sprintService.desalocarHistoriaDeSprint(historiaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erro ao desalocar história de sprint", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSprint(@PathVariable Long id) {
        log.info("Deletando sprint: {}", id);

        try {
            sprintService.deletarSprint(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao deletar sprint", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private SprintDTO converterParaDTO(Sprint sprint) {
        return SprintDTO.builder()
                .id(sprint.getId())
                .titulo(sprint.getTitulo())
                .periodo(sprint.getPeriodo())
                .totalHistorias(sprint.getHistorias() != null ? sprint.getHistorias().size() : 0)
                .build();
    }
}
