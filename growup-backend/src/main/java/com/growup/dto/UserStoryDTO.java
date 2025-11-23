package com.growup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStoryDTO {
    private Long id;
    private String papel;
    private String acao;
    private String beneficio;
    private String prioridade; // ALTA, MEDIA, BAIXA
    private String estimativa;
    private Long sprintId;
    private String jiraIssueKey;
}
