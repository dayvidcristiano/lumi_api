package com.growup.service;

import com.growup.model.UserStory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AIService {

    public List<UserStory> gerarHistorias(String prompt) {
        log.info("Gerando histórias com IA usando prompt");
        
        // TODO: Integrar com OpenAI ou outro serviço de IA
        // Por enquanto, retorna histórias simuladas
        
        List<UserStory> historias = new ArrayList<>();
        
        historias.add(UserStory.builder()
                .papel("usuário")
                .acao("fazer login no sistema")
                .beneficio("acessar minhas funcionalidades")
                .prioridade(UserStory.Prioridade.ALTA)
                .estimativa("4 tarefas")
                .build());

        historias.add(UserStory.builder()
                .papel("gerente")
                .acao("visualizar relatórios de vendas")
                .beneficio("tomar decisões estratégicas")
                .prioridade(UserStory.Prioridade.ALTA)
                .estimativa("6 tarefas")
                .build());

        historias.add(UserStory.builder()
                .papel("vendedor")
                .acao("registrar novas vendas")
                .beneficio("manter o estoque atualizado")
                .prioridade(UserStory.Prioridade.MEDIA)
                .estimativa("2 tarefas")
                .build());

        return historias;
    }
}
