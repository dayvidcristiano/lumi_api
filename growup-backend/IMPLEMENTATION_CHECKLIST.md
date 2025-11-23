# GrowUp Backend - Checklist de Implementação

Use este documento para rastrear o progresso da implementação do Backend e integração com o Frontend.

---

## 📦 Fase 1: Setup Inicial

- [ ] **Clone o repositório**
  ```bash
  git clone https://github.com/seu-usuario/growup-backend.git
  cd growup-backend
  ```

- [ ] **Instale as dependências**
  ```bash
  mvn clean install
  ```

- [ ] **Crie o banco de dados PostgreSQL**
  ```sql
  CREATE DATABASE growup_db;
  ```

- [ ] **Configure as credenciais do banco**
  - Edite: `src/main/resources/application.properties`
  - Atualize: `spring.datasource.url`, `username`, `password`

- [ ] **Execute o Backend localmente**
  ```bash
  mvn spring-boot:run
  ```

- [ ] **Verifique se está rodando**
  ```bash
  curl http://localhost:8080/api/projetos
  ```

---

## 🔌 Fase 2: Configuração de Integrações

### Jira (Opcional)

- [ ] **Gere token de API do Jira**
  - Acesse: https://id.atlassian.com/manage-profile/security/api-tokens
  - Clique em "Create API token"
  - Copie o token

- [ ] **Configure credenciais do Jira**
  - Edite: `src/main/resources/application.properties`
  - Atualize: `jira.api.url`, `jira.api.email`, `jira.api.token`

- [ ] **Teste a integração**
  ```bash
  curl -X POST http://localhost:8080/api/jira/sincronizar \
    -H "Content-Type: application/json" \
    -d '{"historiaIds": [1], "jiraProjectKey": "GROWUP"}'
  ```

### OpenAI (Opcional)

- [ ] **Obtenha chave de API do OpenAI**
  - Acesse: https://platform.openai.com/api-keys
  - Clique em "Create new secret key"
  - Copie a chave

- [ ] **Configure chave do OpenAI**
  - Edite: `src/main/resources/application.properties`
  - Atualize: `ai.api.key`

---

## 🧪 Fase 3: Testes

- [ ] **Execute testes unitários**
  ```bash
  mvn test
  ```

- [ ] **Verifique cobertura de testes**
  ```bash
  mvn test jacoco:report
  ```

- [ ] **Teste cada endpoint manualmente**
  - [ ] POST `/projetos/upload`
  - [ ] GET `/projetos/{id}/historias`
  - [ ] PUT `/historias/{id}`
  - [ ] DELETE `/historias/{id}`
  - [ ] GET `/sprints/projeto/{id}`
  - [ ] POST `/sprints`
  - [ ] POST `/jira/sincronizar`

- [ ] **Teste com Postman ou cURL**
  - Use exemplos em `API_EXAMPLES.md`

---

## 🔗 Fase 4: Integração com Frontend

### Configuração do Frontend Angular

- [ ] **Instale dependências HTTP no Frontend**
  - Verifique se `HttpClientModule` está importado

- [ ] **Crie serviço de API**
  - Arquivo: `src/app/services/api.service.ts`
  - Implemente métodos para cada endpoint

- [ ] **Atualize componente Upload**
  - Integre chamada para `/projetos/upload`
  - Armazene `projetoId` em `localStorage`

- [ ] **Atualize componente Backlog**
  - Integre chamada para `/projetos/{id}/historias`
  - Implemente edição e deleção

- [ ] **Atualize componente Roadmap**
  - Integre chamada para `/sprints/projeto/{id}`
  - Implemente sincronização com Jira

### Testes de Integração

- [ ] **Inicie Backend**
  ```bash
  mvn spring-boot:run
  ```

- [ ] **Inicie Frontend**
  ```bash
  cd growup-front
  ng serve
  ```

- [ ] **Teste fluxo completo**
  - [ ] Upload de documento
  - [ ] Visualização de histórias
  - [ ] Edição de história
  - [ ] Criação de sprint
  - [ ] Alocação de história
  - [ ] Sincronização com Jira

---

## 🚀 Fase 5: Deployment

### Render

- [ ] **Crie repositório Git**
  ```bash
  git init
  git add .
  git commit -m "Initial commit"
  git remote add origin https://github.com/seu-usuario/growup-backend.git
  git push -u origin main
  ```

- [ ] **Crie Web Service no Render**
  - Acesse: https://render.com
  - Clique em "New Web Service"
  - Selecione repositório GitHub

- [ ] **Configure Build Settings**
  - Build Command: `./mvnw clean package`
  - Start Command: `java -jar target/growup-backend-1.0.0.jar`

- [ ] **Crie PostgreSQL Database**
  - Clique em "New PostgreSQL"
  - Copie credenciais

- [ ] **Configure variáveis de ambiente**
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`
  - `JIRA_API_URL`
  - `JIRA_API_EMAIL`
  - `JIRA_API_TOKEN`
  - `AI_API_KEY`

- [ ] **Faça deploy**
  - Clique em "Deploy"
  - Aguarde conclusão

- [ ] **Teste deployment**
  ```bash
  curl https://seu-backend.onrender.com/api/projetos
  ```

### Frontend (Vercel)

- [ ] **Atualize URL da API no Frontend**
  - Altere `apiUrl` para URL do Backend no Render

- [ ] **Faça deploy do Frontend**
  - Push para GitHub
  - Vercel detecta e faz deploy automático

---

## 📋 Fase 6: Documentação

- [ ] **Revise README.md**
  - Atualize instruções de setup
  - Atualize URLs de deployment

- [ ] **Revise DEPLOYMENT_RENDER.md**
  - Confirme instruções passo a passo

- [ ] **Revise INTEGRATION_FRONTEND.md**
  - Confirme exemplos de código

- [ ] **Revise TEAM_REFERENCE.md**
  - Confirme endpoints e modelos

- [ ] **Revise API_EXAMPLES.md**
  - Confirme exemplos de requisições

---

## ✅ Fase 7: Validação Final

- [ ] **Teste todos os endpoints em produção**
  - [ ] Upload de documento
  - [ ] CRUD de histórias
  - [ ] CRUD de sprints
  - [ ] Sincronização com Jira

- [ ] **Verifique logs em produção**
  - Acesse dashboard do Render
  - Verifique se não há erros

- [ ] **Teste performance**
  - Upload de documento grande
  - Listar muitas histórias
  - Sincronizar muitas histórias com Jira

- [ ] **Teste segurança**
  - [ ] CORS está configurado corretamente
  - [ ] Não há credenciais expostas
  - [ ] Validação de entrada está funcionando

- [ ] **Teste com dados reais**
  - [ ] Teste com projeto real
  - [ ] Teste com usuários reais
  - [ ] Colete feedback

---

## 🐛 Fase 8: Troubleshooting

Se encontrar problemas:

- [ ] **Verifique logs do Backend**
  ```bash
  # No terminal onde Backend está rodando
  # Procure por mensagens de erro
  ```

- [ ] **Verifique logs do Frontend**
  ```bash
  # No console do navegador (F12)
  # Procure por erros de rede
  ```

- [ ] **Teste conectividade**
  ```bash
  curl -v http://localhost:8080/api/projetos
  ```

- [ ] **Verifique banco de dados**
  ```sql
  SELECT * FROM projetos;
  SELECT * FROM user_stories;
  SELECT * FROM sprints;
  ```

- [ ] **Revise documentação**
  - Consulte `TEAM_REFERENCE.md`
  - Consulte `README.md`

---

## 📊 Status Geral

| Fase | Status | Responsável | Data |
| --- | --- | --- | --- |
| Setup Inicial | ⬜ | | |
| Configuração de Integrações | ⬜ | | |
| Testes | ⬜ | | |
| Integração com Frontend | ⬜ | | |
| Deployment | ⬜ | | |
| Documentação | ⬜ | | |
| Validação Final | ⬜ | | |
| Troubleshooting | ⬜ | | |

**Legenda**: ⬜ = Não iniciado | 🟨 = Em progresso | ✅ = Concluído

---

## 📝 Notas

```
[Espaço para anotações da equipe]

```

---

## 👥 Responsabilidades

| Função | Responsável | Contato |
| --- | --- | --- |
| Backend | | |
| Frontend | | |
| DevOps/Deployment | | |
| QA/Testes | | |
| Product Manager | | |

---

## 🔗 Links Úteis

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Documentação PostgreSQL](https://www.postgresql.org/docs/)
- [Documentação Jira API](https://developer.atlassian.com/cloud/jira/rest/v3/)
- [Documentação OpenAI API](https://platform.openai.com/docs/api-reference)
- [Documentação Angular](https://angular.io/docs)
- [Render Documentation](https://render.com/docs)

---

**Documento criado por**: Manus AI  
**Última atualização**: 23 de Novembro de 2025  
**Status**: Pronto para Implementação ✅
