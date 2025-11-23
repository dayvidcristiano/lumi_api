package com.growup.service;

import com.growup.model.Sprint;
import com.growup.model.UserStory;
import com.growup.repository.SprintRepository;
import com.growup.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SprintService {

    private final SprintRepository sprintRepository;
    private final UserStoryRepository userStoryRepository;

    @Transactional
    public Sprint criarSprint(Sprint sprint) {
        log.info("Criando sprint: {}", sprint.getTitulo());
        return sprintRepository.save(sprint);
    }

    @Transactional(readOnly = true)
    public List<Sprint> listarSprintsPorProjeto(Long projetoId) {
        return sprintRepository.findByProjetoId(projetoId);
    }

    @Transactional
    public void alocarHistoriaEmSprint(Long historiaId, Long sprintId) {
        UserStory historia = userStoryRepository.findById(historiaId)
                .orElseThrow(() -> new RuntimeException("História não encontrada"));

        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint não encontrada"));

        historia.setSprint(sprint);
        userStoryRepository.save(historia);

        log.info("História {} alocada na sprint {}", historiaId, sprintId);
    }

    @Transactional
    public void desalocarHistoriaDeSprint(Long historiaId) {
        UserStory historia = userStoryRepository.findById(historiaId)
                .orElseThrow(() -> new RuntimeException("História não encontrada"));

        historia.setSprint(null);
        userStoryRepository.save(historia);

        log.info("História {} desalocada da sprint", historiaId);
    }

    @Transactional
    public void deletarSprint(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint não encontrada"));

        // Desalocar todas as histórias
        sprint.getHistorias().forEach(h -> h.setSprint(null));

        sprintRepository.delete(sprint);
        log.info("Sprint {} deletada", sprintId);
    }
}
