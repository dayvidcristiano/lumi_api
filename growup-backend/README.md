# GrowUp Backend

Backend em **Spring Boot 3.2** para o projeto **GrowUp Frontend** (Angular 19).

## 📋 Visão Geral

O GrowUp Backend fornece uma API REST completa para gerenciar:

- **Projetos**: Criação e gerenciamento de projetos
- **Histórias de Usuário**: Geração automática a partir de documentos, CRUD completo
- **Sprints/Roadmap**: Organização de histórias em sprints
- **Sincronização com Jira**: Envio automático de histórias para o Jira
- **Integração com IA**: Geração inteligente de histórias (OpenAI)

## 🏗️ Arquitetura

```
┌─────────────────────────────────────┐
│      Angular Frontend (Port 4200)   │
└────────────────┬────────────────────┘
                 │
                 │ HTTP/REST
                 │
┌────────────────▼────────────────────┐
│   Spring Boot Backend (Port 8080)   │
│  ├── Controllers (REST API)         │
│  ├── Services (Business Logic)      │
│  ├── Repositories (Data Access)     │
│  └── Integrations (Jira, AI)        │
└────────────────┬────────────────────┘
                 │
                 │ JDBC
                 │
┌────────────────▼────────────────────┐
│     PostgreSQL Database             │
│  ├── projetos                       │
│  ├── user_stories                   │
│  └── sprints                        │
└─────────────────────────────────────┘
```

## 🚀 Início Rápido

### Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL 12+
- Git

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/growup-backend.git
cd growup-backend
```

### 2. Configurar Banco de Dados

Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE growup_db;
```

Atualize o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/growup_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Configurar Variáveis de Ambiente (Opcional)

Para integração com Jira e OpenAI, atualize:

```properties
jira.api.url=https://seu-jira-instance.atlassian.net
jira.api.email=seu-email@dominio.com
jira.api.token=seu_token_jira

ai.api.url=https://api.openai.com/v1/chat/completions
ai.api.key=sua_chave_openai
```

### 4. Compilar e Executar

```bash
# Compilar
mvn clean package

# Executar
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080/api`

## 📚 Endpoints da API

### Upload de Documentos

```http
POST /api/projetos/upload
Content-Type: application/json

{
  "nomeProjeto": "Meu Projeto",
  "conteudoDocumento": "Como um usuário...",
  "contextoAdicional": "Contexto do projeto"
}
```

**Resposta:**
```json
{
  "id": 1,
  "nome": "Meu Projeto",
  "totalHistorias": 3,
  "historias": [
    {
      "id": 1,
      "papel": "usuário",
      "acao": "fazer login",
      "beneficio": "acessar conta",
      "prioridade": "ALTA",
      "estimativa": "4 tarefas"
    }
  ]
}
```

### Listar Histórias

```http
GET /api/projetos/{projetoId}/historias
```

### Atualizar História

```http
PUT /api/historias/{id}
Content-Type: application/json

{
  "papel": "usuário",
  "acao": "fazer login",
  "beneficio": "acessar conta",
  "prioridade": "ALTA",
  "estimativa": "4 tarefas"
}
```

### Deletar História

```http
DELETE /api/historias/{id}
```

### Listar Sprints

```http
GET /api/sprints/projeto/{projetoId}
```

### Criar Sprint

```http
POST /api/sprints
Content-Type: application/json

{
  "titulo": "Semana 1",
  "periodo": "05/11 - 12/11"
}
```

### Alocar História em Sprint

```http
POST /api/sprints/{sprintId}/alocar-historia/{historiaId}
```

### Sincronizar com Jira

```http
POST /api/jira/sincronizar
Content-Type: application/json

{
  "historiaIds": [1, 2, 3],
  "jiraProjectKey": "GROWUP"
}
```

## 🗂️ Estrutura do Projeto

```
growup-backend/
├── src/
│   ├── main/
│   │   ├── java/com/growup/
│   │   │   ├── config/           # Configurações (CORS, WebClient)
│   │   │   ├── controller/       # Controllers REST
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── model/            # Entidades JPA
│   │   │   ├── repository/       # Repositórios Spring Data
│   │   │   ├── service/          # Lógica de Negócio
│   │   │   ├── integration/      # Integrações (Jira, AI)
│   │   │   └── GrowupBackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/growup/
│           ├── controller/
│           └── service/
├── pom.xml
└── README.md
```

## 🧪 Testes

### Executar Testes

```bash
mvn test
```

### Testes Unitários

- `UserStoryServiceTest`: Testa lógica de geração e gerenciamento de histórias
- `SprintServiceTest`: Testa lógica de sprints
- `JiraServiceTest`: Testa sincronização com Jira

### Testes de Integração

- `UploadControllerIntegrationTest`: Testa endpoint de upload
- `UserStoryControllerIntegrationTest`: Testa CRUD de histórias
- `RoadmapControllerIntegrationTest`: Testa gerenciamento de sprints

## 🔌 Integrações

### Jira

A integração com Jira permite sincronizar histórias de usuário diretamente para o Jira.

**Configuração:**
1. Gere um token de API em: https://id.atlassian.com/manage-profile/security/api-tokens
2. Configure as variáveis de ambiente:
   ```properties
   jira.api.url=https://seu-jira-instance.atlassian.net
   jira.api.email=seu-email@dominio.com
   jira.api.token=seu_token_jira
   ```

### OpenAI (Geração de Histórias com IA)

A integração com OpenAI permite gerar histórias automaticamente a partir de documentos.

**Configuração:**
1. Obtenha uma chave de API em: https://platform.openai.com/api-keys
2. Configure a variável de ambiente:
   ```properties
   ai.api.key=sua_chave_openai
   ```

## 📦 Dependências Principais

- **Spring Boot 3.2**: Framework web
- **Spring Data JPA**: Acesso a dados
- **PostgreSQL Driver**: Banco de dados
- **Lombok**: Redução de boilerplate
- **Spring WebFlux**: Chamadas HTTP assíncronas
- **JUnit 5**: Testes unitários
- **Mockito**: Mocking para testes

## 🚢 Deployment

### Docker

```dockerfile
FROM openjdk:17-slim
COPY target/growup-backend-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Render

1. Crie um repositório Git com o código
2. Acesse https://render.com e crie um novo Web Service
3. Configure:
   - **Build Command**: `./mvnw clean package`
   - **Start Command**: `java -jar target/growup-backend-1.0.0.jar`
4. Defina as variáveis de ambiente
5. Deploy!

### Heroku

```bash
heroku create growup-backend
git push heroku main
```

## 📝 Logging

O projeto usa SLF4J com Logback. Configure em `application.properties`:

```properties
logging.level.com.growup=DEBUG
logging.level.org.springframework=INFO
```

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 📧 Suporte

Para dúvidas ou problemas, abra uma issue no GitHub ou entre em contato através de: seu-email@dominio.com

## 🔗 Links Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Jira REST API](https://developer.atlassian.com/cloud/jira/rest/v3/)
- [OpenAI API](https://platform.openai.com/docs/api-reference)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
