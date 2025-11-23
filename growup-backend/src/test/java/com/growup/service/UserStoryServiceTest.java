package com.growup.service;

import com.growup.dto.UserStoryDTO;
import com.growup.dto.UploadDocumentoRequest;
import com.growup.model.Projeto;
import com.growup.model.UserStory;
import com.growup.repository.ProjetoRepository;
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
class UserStoryServiceTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private AIService aiService;

    @InjectMocks
    private UserStoryService userStoryService;

    private Projeto projeto;
    private UserStory historia;

    @BeforeEach
    void setUp() {
        projeto = Projeto.builder()
                .id(1L)
                .nome("Projeto Teste")
                .descricao("Descrição teste")
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
                .build();
    }

    @Test
    void testProcessarDocumento() {
        UploadDocumentoRequest request = UploadDocumentoRequest.builder()
                .nomeProjeto("Novo Projeto")
                .conteudoDocumento("Como um usuário...")
                .contextoAdicional("Contexto")
                .build();

        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);
        when(aiService.gerarHistorias(anyString())).thenReturn(List.of(historia));
        when(userStoryRepository.saveAll(anyList())).thenReturn(List.of(historia));

        Projeto resultado = userStoryService.processarDocumento(request);

        assertNotNull(resultado);
        assertEquals("Novo Projeto", resultado.getNome());
        verify(projetoRepository, times(2)).save(any(Projeto.class));
    }

    @Test
    void testListarHistoriasPorProjeto() {
        when(userStoryRepository.findByProjetoId(1L)).thenReturn(List.of(historia));

        List<UserStoryDTO> resultado = userStoryService.listarHistoriasPorProjeto(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("usuário", resultado.get(0).getPapel());
    }

    @Test
    void testAtualizarHistoria() {
        UserStoryDTO dto = UserStoryDTO.builder()
                .papel("gerente")
                .acao("visualizar relatórios")
                .beneficio("tomar decisões")
                .prioridade("MEDIA")
                .estimativa("6 tarefas")
                .build();

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(historia));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(historia);

        UserStoryDTO resultado = userStoryService.atualizarHistoria(1L, dto);

        assertNotNull(resultado);
        verify(userStoryRepository).save(any(UserStory.class));
    }

    @Test
    void testDeletarHistoria() {
        userStoryService.deletarHistoria(1L);

        verify(userStoryRepository).deleteById(1L);
    }

    @Test
    void testListarHistoriasNaoAlocadas() {
        when(userStoryRepository.findByProjetoIdAndSprintIsNull(1L))
                .thenReturn(List.of(historia));

        List<UserStoryDTO> resultado = userStoryService.listarHistoriasNaoAlocadas(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }
}
