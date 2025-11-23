package com.growup.service;

import com.growup.model.Projeto;
import com.growup.model.Sprint;
import com.growup.model.UserStory;
import com.growup.repository.SprintRepository;
import com.growup.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SprintServiceTest {

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private SprintService sprintService;

    private Sprint sprint;
    private UserStory historia;
    private Projeto projeto;

    @BeforeEach
    void setUp() {
        projeto = Projeto.builder()
                .id(1L)
                .nome("Projeto Teste")
                .build();

        sprint = Sprint.builder()
                .id(1L)
                .titulo("Semana 1")
                .periodo("05/11 - 12/11")
                .projeto(projeto)
                .historias(new ArrayList<>())
                .build();

        historia = UserStory.builder()
                .id(1L)
                .papel("usuário")
                .acao("fazer login")
                .beneficio("acessar conta")
                .prioridade(UserStory.Prioridade.ALTA)
                .estimativa("4 tarefas")
                .projeto(projeto)
                .sprint(sprint)
                .build();
    }

    @Test
    void testCriarSprint() {
        when(sprintRepository.save(any(Sprint.class))).thenReturn(sprint);

        Sprint resultado = sprintService.criarSprint(sprint);

        assertNotNull(resultado);
        assertEquals("Semana 1", resultado.getTitulo());
        verify(sprintRepository).save(any(Sprint.class));
    }

    @Test
    void testListarSprintsPorProjeto() {
        when(sprintRepository.findByProjetoId(1L)).thenReturn(List.of(sprint));

        List<Sprint> resultado = sprintService.listarSprintsPorProjeto(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Semana 1", resultado.get(0).getTitulo());
    }

    @Test
    void testAlocarHistoriaEmSprint() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(historia));
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(historia);

        sprintService.alocarHistoriaEmSprint(1L, 1L);

        verify(userStoryRepository).save(any(UserStory.class));
    }

    @Test
    void testDesalocarHistoriaDeSprint() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(historia));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(historia);

        sprintService.desalocarHistoriaDeSprint(1L);

        verify(userStoryRepository).save(any(UserStory.class));
    }

    @Test
    void testDeletarSprint() {
        sprint.setHistorias(List.of(historia));
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));

        sprintService.deletarSprint(1L);

        verify(sprintRepository).delete(any(Sprint.class));
    }
}
