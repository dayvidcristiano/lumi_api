# Deployment do GrowUp Backend no Render

Este guia descreve como fazer o deploy do **GrowUp Backend** (Spring Boot) no **Render**.

## Pré-requisitos

1. Conta no [Render](https://render.com)
2. Repositório Git com o código do Backend (GitHub, GitLab, etc.)
3. Variáveis de ambiente configuradas

## Passo 1: Preparar o Repositório Git

```bash
cd growup-backend
git init
git add .
git commit -m "Initial commit: GrowUp Backend"
git remote add origin https://github.com/seu-usuario/growup-backend.git
git branch -M main
git push -u origin main
```

## Passo 2: Criar Web Service no Render

1. Acesse [https://dashboard.render.com](https://dashboard.render.com)
2. Clique em **New +** → **Web Service**
3. Selecione **Deploy existing repository from GitHub**
4. Autorize o Render a acessar sua conta GitHub
5. Selecione o repositório `growup-backend`
6. Configure:
   - **Name**: `growup-backend`
   - **Environment**: `Java`
   - **Build Command**: `./mvnw clean package`
   - **Start Command**: `java -jar target/growup-backend-1.0.0.jar`
   - **Instance Type**: `Standard` (ou superior)

## Passo 3: Criar Banco de Dados PostgreSQL

1. No Render, clique em **New +** → **PostgreSQL**
2. Configure:
   - **Name**: `growup-db`
   - **Database**: `growup_db`
   - **User**: `postgres`
   - **Region**: Selecione a mesma região do Web Service
3. Clique em **Create Database**
4. Copie as credenciais fornecidas

## Passo 4: Configurar Variáveis de Ambiente

No Web Service do Backend, vá para **Environment** e adicione:

```
SPRING_DATASOURCE_URL=postgresql://seu_usuario:sua_senha@seu_host:5432/growup_db
SPRING_DATASOURCE_USERNAME=seu_usuario
SPRING_DATASOURCE_PASSWORD=sua_senha
SPRING_JPA_HIBERNATE_DDL_AUTO=update
JIRA_API_URL=https://seu-jira-instance.atlassian.net
JIRA_API_EMAIL=seu-email@dominio.com
JIRA_API_TOKEN=seu_token_jira
AI_API_URL=https://api.openai.com/v1/chat/completions
AI_API_KEY=sua_chave_openai
```

## Passo 5: Deploy

1. Clique em **Deploy** no dashboard
2. Acompanhe os logs de build e deployment
3. Após o sucesso, você receberá uma URL pública (ex: `https://growup-backend.onrender.com`)

## Passo 6: Verificar o Deployment

Teste a API:

```bash
curl https://growup-backend.onrender.com/api/projetos
```

## Variáveis de Ambiente Necessárias

| Variável | Descrição | Exemplo |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | URL do banco de dados PostgreSQL | `postgresql://user:pass@host:5432/growup_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco de dados | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco de dados | `sua_senha_segura` |
| `JIRA_API_URL` | URL da API do Jira | `https://seu-jira.atlassian.net` |
| `JIRA_API_EMAIL` | Email para autenticação no Jira | `seu-email@dominio.com` |
| `JIRA_API_TOKEN` | Token de API do Jira | `seu_token_jira` |
| `AI_API_URL` | URL da API de IA | `https://api.openai.com/v1/chat/completions` |
| `AI_API_KEY` | Chave de API de IA | `sua_chave_openai` |

## Troubleshooting

### Build falha com erro de Maven
Certifique-se de que o `pom.xml` está na raiz do repositório e que o Java 17 está configurado.

### Aplicação não inicia
Verifique os logs no dashboard do Render. Comum: variáveis de ambiente não configuradas ou banco de dados inacessível.

### Erro de conexão com Jira
Verifique se as credenciais do Jira estão corretas e se o token não expirou.

## Monitoramento

No dashboard do Render, você pode:
- Visualizar logs em tempo real
- Monitorar CPU e memória
- Configurar alertas
- Fazer rollback de versões anteriores

## Atualizações

Para fazer deploy de uma nova versão:
1. Faça commit e push das mudanças para o GitHub
2. O Render detectará automaticamente e fará o redeploy
3. Ou clique em **Manual Deploy** no dashboard
