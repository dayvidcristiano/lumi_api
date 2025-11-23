# GrowUp Backend - Referência Completa para a Equipe

**Data**: 23 de Novembro de 2025  
**Versão**: 1.0.0  
**Ambiente**: Spring Boot 3.2 + PostgreSQL + Java 17

---

## 📋 Índice

1. [Visão Geral](#visão-geral)
2. [Alterações Necessárias](#alterações-necessárias)
3. [Endpoints Disponíveis](#endpoints-disponíveis)
4. [Modelos de Dados](#modelos-de-dados)
5. [Configuração do Ambiente](#configuração-do-ambiente)
6. [Guia de Desenvolvimento](#guia-de-desenvolvimento)
7. [Testes](#testes)
8. [Deployment](#deployment)
9. [Troubleshooting](#troubleshooting)

---

## 🎯 Visão Geral

O **GrowUp Backend** é uma API REST que gerencia:

- **Projetos**: Criação e gerenciamento de projetos
- **Histórias de Usuário**: Geração automática, CRUD, priorização
- **Sprints**: Organização em sprints/roadmap
- **Sincronização**: Envio automático para Jira
- **IA**: Geração inteligente de histórias (OpenAI)

### Arquitetura

```
Frontend (Angular)
        ↓ HTTP/REST
Backend (Spring Boot)
        ↓ JDBC
PostgreSQL Database
```

### Stack Tecnológico

| Componente | Versão | Propósito |
| --- | --- | --- |
| Java | 17+ | Linguagem de programação |
| Spring Boot | 3.2 | Framework web |
| Spring Data JPA | 3.2 | ORM e acesso a dados |
| PostgreSQL | 12+ | Banco de dados |
| Lombok | 1.18 | Redução de boilerplate |
| WebFlux | 3.2 | Chamadas HTTP assíncronas |
| JUnit 5 | 5.9 | Framework de testes |
| Mockito | 5.x | Mocking para testes |

---

## 🔄 Alterações Necessárias

### 1. Configuração do Banco de Dados

**Arquivo**: `src/main/resources/application.properties`

Atualize com suas credenciais:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://seu_host:5432/growup_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

**Passo a passo:**
1. Crie um banco de dados PostgreSQL chamado `growup_db`
2. Substitua `seu_host`, `seu_usuario` e `sua_senha` pelas suas credenciais
3. O Hibernate criará as tabelas automaticamente na primeira execução

### 2. Integração com Jira (Opcional)

**Arquivo**: `src/main/resources/application.properties`

```properties
# Jira Configuration
jira.api.url=https://seu-jira-instance.atlassian.net
jira.api.email=seu-email@dominio.com
jira.api.token=seu_token_jira
```

**Como obter o token:**
1. Acesse: https://id.atlassian.com/manage-profile/security/api-tokens
2. Clique em "Create API token"
3. Copie o token gerado
4. Cole em `jira.api.token`

### 3. Integração com OpenAI (Opcional)

**Arquivo**: `src/main/resources/application.properties`

```properties
# AI Configuration
ai.api.url=https://api.openai.com/v1/chat/completions
ai.api.key=sua_chave_openai
```

**Como obter a chave:**
1. Acesse: https://platform.openai.com/api-keys
2. Clique em "Create new secret key"
3. Copie a chave
4. Cole em `ai.api.key`

### 4. Configuração de CORS

**Arquivo**: `src/main/java/com/growup/config/CorsConfig.java`

Se precisar adicionar mais origens (além de `*`):

```java
registry.addMapping("/**")
        .allowedOrigins("http://localhost:4200", "https://seu-dominio.com")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .maxAge(3600);
```

### 5. Variáveis de Ambiente para Produção

No Render ou outro serviço de hosting, configure:

```
SPRING_DATASOURCE_URL=postgresql://user:pass@host:5432/growup_db
SPRING_DATASOURCE_USERNAME=seu_usuario
SPRING_DATASOURCE_PASSWORD=sua_senha
JIRA_API_URL=https://seu-jira.atlassian.net
JIRA_API_EMAIL=seu-email@dominio.com
JIRA_API_TOKEN=seu_token_jira
AI_API_URL=https://api.openai.com/v1/chat/completions
AI_API_KEY=sua_chave_openai
```

---

## 🔌 Endpoints Disponíveis

### Base URL

**Desenvolvimento**: `http://localhost:8080/api`  
**Produção**: `https://seu-dominio.com/api`

### 1. Upload de Documentos

#### POST `/projetos/upload`

Cria um novo projeto e gera histórias a partir de um documento.

**Request:**
```json
{
  "nomeProjeto": "Meu Projeto",
  "conteudoDocumento": "Como um usuário, eu quero fazer login no sistema para acessar minhas funcionalidades",
  "contextoAdicional": "Projeto de e-commerce com foco em vendas"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "nome": "Meu Projeto",
  "descricao": "Projeto criado a partir de documento",
  "totalHistorias": 3,
  "historias": [
    {
      "id": 1,
      "papel": "usuário",
      "acao": "fazer login",
      "beneficio": "acessar conta",
      "prioridade": "ALTA",
      "estimativa": "4 tarefas",
      "sprintId": null,
      "jiraIssueKey": null
    }
  ]
}
```

**Erros:**
- `400 Bad Request`: Campos obrigatórios faltando
- `500 Internal Server Error`: Erro ao processar documento

---

### 2. Histórias de Usuário

#### GET `/projetos/{projetoId}/historias`

Lista todas as histórias de um projeto.

**Parameters:**
- `projetoId` (path): ID do projeto

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "papel": "usuário",
    "acao": "fazer login",
    "beneficio": "acessar conta",
    "prioridade": "ALTA",
    "estimativa": "4 tarefas",
    "sprintId": null,
    "jiraIssueKey": null
  },
  {
    "id": 2,
    "papel": "gerente",
    "acao": "visualizar relatórios",
    "beneficio": "tomar decisões",
    "prioridade": "ALTA",
    "estimativa": "6 tarefas",
    "sprintId": 1,
    "jiraIssueKey": "GROWUP-123"
  }
]
```

---

#### GET `/projetos/{projetoId}/historias/nao-alocadas`

Lista histórias que ainda não foram alocadas em sprints.

**Parameters:**
- `projetoId` (path): ID do projeto

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "papel": "usuário",
    "acao": "fazer login",
    "beneficio": "acessar conta",
    "prioridade": "ALTA",
    "estimativa": "4 tarefas",
    "sprintId": null,
    "jiraIssueKey": null
  }
]
```

---

#### PUT `/historias/{id}`

Atualiza uma história existente.

**Parameters:**
- `id` (path): ID da história

**Request:**
```json
{
  "papel": "gerente",
  "acao": "visualizar relatórios de vendas",
  "beneficio": "tomar decisões estratégicas",
  "prioridade": "MEDIA",
  "estimativa": "6 tarefas"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "papel": "gerente",
  "acao": "visualizar relatórios de vendas",
  "beneficio": "tomar decisões estratégicas",
  "prioridade": "MEDIA",
  "estimativa": "6 tarefas",
  "sprintId": null,
  "jiraIssueKey": null
}
```

**Erros:**
- `404 Not Found`: História não encontrada
- `500 Internal Server Error`: Erro ao atualizar

---

#### DELETE `/historias/{id}`

Deleta uma história.

**Parameters:**
- `id` (path): ID da história

**Response (204 No Content):**
```
(sem corpo)
```

**Erros:**
- `404 Not Found`: História não encontrada
- `500 Internal Server Error`: Erro ao deletar

---

#### POST `/historias/{id}/alocar-sprint/{sprintId}`

Aloca uma história a uma sprint.

**Parameters:**
- `id` (path): ID da história
- `sprintId` (path): ID da sprint

**Response (200 OK):**
```json
{
  "id": 1,
  "papel": "usuário",
  "acao": "fazer login",
  "beneficio": "acessar conta",
  "prioridade": "ALTA",
  "estimativa": "4 tarefas",
  "sprintId": 1,
  "jiraIssueKey": null
}
```

---

### 3. Sprints / Roadmap

#### GET `/sprints/projeto/{projetoId}`

Lista todas as sprints de um projeto.

**Parameters:**
- `projetoId` (path): ID do projeto

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "titulo": "Semana 1",
    "periodo": "05/11 - 12/11",
    "totalHistorias": 2
  },
  {
    "id": 2,
    "titulo": "Semana 2",
    "periodo": "13/11 - 20/11",
    "totalHistorias": 1
  }
]
```

---

#### POST `/sprints`

Cria uma nova sprint.

**Request:**
```json
{
  "titulo": "Semana 1",
  "periodo": "05/11 - 12/11"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "titulo": "Semana 1",
  "periodo": "05/11 - 12/11",
  "totalHistorias": 0
}
```

---

#### POST `/sprints/{sprintId}/alocar-historia/{historiaId}`

Aloca uma história a uma sprint.

**Parameters:**
- `sprintId` (path): ID da sprint
- `historiaId` (path): ID da história

**Response (200 OK):**
```
(sem corpo)
```

---

#### POST `/sprints/{sprintId}/desalocar-historia/{historiaId}`

Remove uma história de uma sprint.

**Parameters:**
- `sprintId` (path): ID da sprint
- `historiaId` (path): ID da história

**Response (200 OK):**
```
(sem corpo)
```

---

#### DELETE `/sprints/{id}`

Deleta uma sprint (e desaloca todas as histórias).

**Parameters:**
- `id` (path): ID da sprint

**Response (204 No Content):**
```
(sem corpo)
```

---

### 4. Sincronização com Jira

#### POST `/jira/sincronizar`

Sincroniza histórias com o Jira, criando issues.

**Request:**
```json
{
  "historiaIds": [1, 2, 3],
  "jiraProjectKey": "GROWUP"
}
```

**Response (200 OK):**
```json
{
  "sucesso": true,
  "mensagem": "Histórias sincronizadas com sucesso",
  "totalSincronizadas": 3
}
```

**Erros:**
- `500 Internal Server Error`: Erro ao sincronizar com Jira

---

## 📊 Modelos de Dados

### UserStory

| Campo | Tipo | Descrição |
| --- | --- | --- |
| id | Long | ID único (auto-incrementado) |
| papel | String | Quem é o usuário (ex: "usuário", "gerente") |
| acao | String | O que o usuário quer fazer |
| beneficio | String | Por que quer fazer |
| prioridade | Enum | ALTA, MEDIA, BAIXA |
| estimativa | String | Estimativa de esforço (ex: "4 tarefas") |
| projeto | Projeto | Projeto ao qual pertence |
| sprint | Sprint | Sprint ao qual foi alocada (nullable) |
| jiraIssueKey | String | Chave do issue no Jira (nullable) |
| criadoEm | LocalDateTime | Data de criação |
| atualizadoEm | LocalDateTime | Data da última atualização |

**Tabela**: `user_stories`

---

### Projeto

| Campo | Tipo | Descrição |
| --- | --- | --- |
| id | Long | ID único (auto-incrementado) |
| nome | String | Nome do projeto |
| descricao | String | Descrição do projeto |
| contextoAdicional | String | Contexto adicional |
| jiraProjectKey | String | Chave do projeto no Jira (nullable) |
| historias | List<UserStory> | Histórias do projeto |
| sprints | List<Sprint> | Sprints do projeto |
| criadoEm | LocalDateTime | Data de criação |
| atualizadoEm | LocalDateTime | Data da última atualização |

**Tabela**: `projetos`

---

### Sprint

| Campo | Tipo | Descrição |
| --- | --- | --- |
| id | Long | ID único (auto-incrementado) |
| titulo | String | Título da sprint (ex: "Semana 1") |
| periodo | String | Período da sprint (ex: "05/11 - 12/11") |
| projeto | Projeto | Projeto ao qual pertence |
| historias | List<UserStory> | Histórias alocadas |
| criadoEm | LocalDateTime | Data de criação |
| atualizadoEm | LocalDateTime | Data da última atualização |

**Tabela**: `sprints`

---

## ⚙️ Configuração do Ambiente

### Pré-requisitos

- Java 17 ou superior
- Maven 3.8 ou superior
- PostgreSQL 12 ou superior
- Git

### Instalação Local

**1. Clonar o repositório:**
```bash
git clone https://github.com/seu-usuario/growup-backend.git
cd growup-backend
```

**2. Criar banco de dados:**
```sql
CREATE DATABASE growup_db;
```

**3. Configurar arquivo de propriedades:**
```bash
# Editar src/main/resources/application.properties
# Atualizar credenciais do banco de dados
```

**4. Compilar:**
```bash
mvn clean install
```

**5. Executar:**
```bash
mvn spring-boot:run
```

**6. Verificar:**
```bash
curl http://localhost:8080/api/projetos
```

### Variáveis de Ambiente

Para desenvolvimento, você pode usar variáveis de ambiente:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/growup_db
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=sua_senha
export JIRA_API_URL=https://seu-jira.atlassian.net
export JIRA_API_EMAIL=seu-email@dominio.com
export JIRA_API_TOKEN=seu_token_jira
export AI_API_KEY=sua_chave_openai

mvn spring-boot:run
```

---

## 👨‍💻 Guia de Desenvolvimento

### Estrutura do Projeto

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
├── pom.xml
├── README.md
├── DEPLOYMENT_RENDER.md
└── INTEGRATION_FRONTEND.md
```

### Adicionando um Novo Endpoint

**1. Criar o DTO (se necessário):**
```java
// src/main/java/com/growup/dto/MeuDTO.java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeuDTO {
    private String campo1;
    private String campo2;
}
```

**2. Criar o Service:**
```java
// src/main/java/com/growup/service/MeuService.java
@Service
@RequiredArgsConstructor
@Slf4j
public class MeuService {
    // Implementar lógica
}
```

**3. Criar o Controller:**
```java
// src/main/java/com/growup/controller/MeuController.java
@RestController
@RequestMapping("/meus-recursos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MeuController {
    
    private final MeuService meuService;
    
    @GetMapping
    public ResponseEntity<List<MeuDTO>> listar() {
        // Implementar
    }
}
```

### Boas Práticas

1. **Sempre use DTOs** para comunicação com o Frontend
2. **Adicione logs** com `@Slf4j` e `log.info()`, `log.error()`
3. **Trate exceções** com try-catch e retorne status HTTP apropriados
4. **Use transações** com `@Transactional` para operações de escrita
5. **Valide entrada** antes de processar
6. **Documente** com comentários e JavaDoc

---

## 🧪 Testes

### Executar Testes

```bash
# Todos os testes
mvn test

# Teste específico
mvn test -Dtest=UserStoryServiceTest

# Com cobertura
mvn test jacoco:report
```

### Estrutura de Testes

```
src/test/java/com/growup/
├── controller/
│   └── UploadControllerIntegrationTest.java
└── service/
    ├── UserStoryServiceTest.java
    └── SprintServiceTest.java
```

### Exemplo de Teste Unitário

```java
@ExtendWith(MockitoExtension.class)
class MeuServiceTest {
    
    @Mock
    private MeuRepository repository;
    
    @InjectMocks
    private MeuService service;
    
    @Test
    void testMeuMetodo() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(new Meu()));
        
        // Act
        Meu resultado = service.obter(1L);
        
        // Assert
        assertNotNull(resultado);
        verify(repository).findById(1L);
    }
}
```

---

## 🚀 Deployment

### Render

1. Crie um repositório Git
2. Faça push para GitHub
3. Acesse https://render.com
4. Crie um Web Service + PostgreSQL
5. Configure variáveis de ambiente
6. Deploy automático!

Veja `DEPLOYMENT_RENDER.md` para detalhes.

### Docker

```dockerfile
FROM openjdk:17-slim
COPY target/growup-backend-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t growup-backend .
docker run -p 8080:8080 growup-backend
```

### Heroku

```bash
heroku create growup-backend
git push heroku main
```

---

## 🔧 Troubleshooting

### Erro: "Connection refused"

**Problema**: Backend não consegue conectar ao banco de dados.

**Solução**:
1. Verifique se PostgreSQL está rodando
2. Verifique credenciais em `application.properties`
3. Verifique se o banco de dados existe

### Erro: "CORS error"

**Problema**: Frontend não consegue chamar a API.

**Solução**:
1. Verifique se CORS está habilitado em `CorsConfig.java`
2. Verifique a origem do Frontend
3. Verifique se o Backend está rodando

### Erro: "401 Unauthorized" do Jira

**Problema**: Credenciais do Jira inválidas.

**Solução**:
1. Verifique se o token Jira está correto
2. Verifique se o email está correto
3. Regenere o token em https://id.atlassian.com/manage-profile/security/api-tokens

### Erro: "400 Bad Request"

**Problema**: Requisição malformada.

**Solução**:
1. Verifique o JSON enviado
2. Verifique se todos os campos obrigatórios estão presentes
3. Verifique os tipos de dados

### Logs

Para ver logs detalhados:

```properties
# application.properties
logging.level.com.growup=DEBUG
logging.level.org.springframework=INFO
logging.level.org.hibernate=DEBUG
```

---

## 📞 Contato e Suporte

Para dúvidas ou problemas:
1. Verifique a documentação em `README.md`
2. Abra uma issue no GitHub
3. Entre em contato com o time de desenvolvimento

---

## 📝 Histórico de Versões

| Versão | Data | Alterações |
| --- | --- | --- |
| 1.0.0 | 23/11/2025 | Versão inicial com CRUD completo, Jira e IA |

---

## ✅ Checklist de Implementação

- [ ] Banco de dados PostgreSQL criado
- [ ] Variáveis de ambiente configuradas
- [ ] Backend rodando localmente
- [ ] Testes passando
- [ ] Integração com Frontend testada
- [ ] Credenciais Jira configuradas
- [ ] Credenciais OpenAI configuradas
- [ ] Deployment no Render realizado
- [ ] Domínio customizado configurado (opcional)
- [ ] Monitoramento configurado (opcional)

---

**Documento criado por**: Manus AI  
**Última atualização**: 23 de Novembro de 2025  
**Status**: Pronto para Produção ✅
