package com.growup.controller;

import com.growup.dto.UploadDocumentoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UploadControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUploadDocumento() throws Exception {
        UploadDocumentoRequest request = UploadDocumentoRequest.builder()
                .nomeProjeto("Projeto Teste")
                .conteudoDocumento("Como um usuário, eu quero fazer login")
                .contextoAdicional("Contexto do projeto")
                .build();

        mockMvc.perform(post("/api/projetos/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Projeto Teste"))
                .andExpect(jsonPath("$.totalHistorias").exists());
    }

    @Test
    void testListarHistorias() throws Exception {
        mockMvc.perform(get("/api/projetos/1/historias")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testListarHistoriasNaoAlocadas() throws Exception {
        mockMvc.perform(get("/api/projetos/1/historias/nao-alocadas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
