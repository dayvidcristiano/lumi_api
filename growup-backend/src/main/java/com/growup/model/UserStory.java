package com.growup.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String papel; // quem é o usuário (ex: "usuário", "gerente")

    @Column(nullable = false)
    private String acao; // o que o usuário quer fazer

    @Column(nullable = false)
    private String beneficio; // por que quer fazer

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade; // ALTA, MEDIA, BAIXA

    @Column(nullable = false)
    private String estimativa; // estimativa de esforço (ex: "4 tarefas")

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    @Column(name = "jira_issue_key")
    private String jiraIssueKey; // chave do Jira quando sincronizado

    @Column(name = "created_at")
    private LocalDateTime criadoEm;

    @Column(name = "updated_at")
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    public enum Prioridade {
        ALTA, MEDIA, BAIXA
    }
}
