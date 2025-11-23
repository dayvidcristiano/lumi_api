package com.growup.controller;

import com.growup.dto.UserStoryDTO;
import com.growup.service.UserStoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/historias")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserStoryController {

    private final UserStoryService userStoryService;

    @PutMapping("/{id}")
    public ResponseEntity<UserStoryDTO> atualizarHistoria(
            @PathVariable Long id,
            @RequestBody UserStoryDTO dto) {
        log.info("Atualizando história: {}", id);

        try {
            UserStoryDTO atualizada = userStoryService.atualizarHistoria(id, dto);
            return ResponseEntity.ok(atualizada);
        } catch (Exception e) {
            log.error("Erro ao atualizar história", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarHistoria(@PathVariable Long id) {
        log.info("Deletando história: {}", id);

        try {
            userStoryService.deletarHistoria(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao deletar história", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/alocar-sprint/{sprintId}")
    public ResponseEntity<UserStoryDTO> alocarEmSprint(
            @PathVariable Long id,
            @PathVariable Long sprintId) {
        log.info("Alocando história {} em sprint {}", id, sprintId);

        try {
            UserStoryDTO alocada = userStoryService.alocarEmSprint(id, sprintId);
            return ResponseEntity.ok(alocada);
        } catch (Exception e) {
            log.error("Erro ao alocar história em sprint", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
