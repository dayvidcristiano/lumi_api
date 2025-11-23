# GrowUp Backend - Exemplos de Requisições

Este documento contém exemplos práticos de como chamar cada endpoint da API.

## 🔗 Base URL

```
http://localhost:8080/api
```

---

## 1️⃣ Upload de Documentos

### cURL

```bash
curl -X POST http://localhost:8080/api/projetos/upload \
  -H "Content-Type: application/json" \
  -d '{
    "nomeProjeto": "E-commerce Platform",
    "conteudoDocumento": "Como um usuário, eu quero fazer login no sistema para acessar minhas compras. Como um gerente, eu quero visualizar relatórios de vendas para tomar decisões estratégicas.",
    "contextoAdicional": "Projeto de plataforma de e-commerce com foco em vendas online"
  }'
```

### JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/projetos/upload', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    nomeProjeto: 'E-commerce Platform',
    conteudoDocumento: 'Como um usuário, eu quero fazer login...',
    contextoAdicional: 'Projeto de plataforma de e-commerce'
  })
});

const data = await response.json();
console.log(data);
```

### Python/Requests

```python
import requests

url = 'http://localhost:8080/api/projetos/upload'
payload = {
    'nomeProjeto': 'E-commerce Platform',
    'conteudoDocumento': 'Como um usuário, eu quero fazer login...',
    'contextoAdicional': 'Projeto de plataforma de e-commerce'
}

response = requests.post(url, json=payload)
print(response.json())
```

### Postman

1. Método: **POST**
2. URL: `http://localhost:8080/api/projetos/upload`
3. Headers:
   - `Content-Type: application/json`
4. Body (raw JSON):
```json
{
  "nomeProjeto": "E-commerce Platform",
  "conteudoDocumento": "Como um usuário, eu quero fazer login...",
  "contextoAdicional": "Projeto de plataforma de e-commerce"
}
```

---

## 2️⃣ Listar Histórias

### cURL

```bash
curl -X GET http://localhost:8080/api/projetos/1/historias \
  -H "Content-Type: application/json"
```

### JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/projetos/1/historias');
const historias = await response.json();
console.log(historias);
```

### Python/Requests

```python
import requests

url = 'http://localhost:8080/api/projetos/1/historias'
response = requests.get(url)
print(response.json())
```

### Postman

1. Método: **GET**
2. URL: `http://localhost:8080/api/projetos/1/historias`

---

## 3️⃣ Listar Histórias Não Alocadas

### cURL

```bash
curl -X GET http://localhost:8080/api/projetos/1/historias/nao-alocadas \
  -H "Content-Type: application/json"
```

### JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/projetos/1/historias/nao-alocadas');
const historias = await response.json();
console.log(historias);
```

---

## 4️⃣ Atualizar História

### cURL

```bash
curl -X PUT http://localhost:8080/api/historias/1 \
  -H "Content-Type: application/json" \
  -d '{
    "papel": "gerente",
    "acao": "visualizar relatórios de vendas",
    "beneficio": "tomar decisões estratégicas",
    "prioridade": "ALTA",
    "estimativa": "8 tarefas"
  }'
```

### JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/historias/1', {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    papel: 'gerente',
    acao: 'visualizar relatórios de vendas',
    beneficio: 'tomar decisões estratégicas',
    prioridade: 'ALTA',
    estimativa: '8 tarefas'
  })
});

const data = await response.json();
console.log(data);
```

### Python/Requests

```python
import requests

url = 'http://localhost:8080/api/historias/1'
payload = {
    'papel': 'gerente',
    'acao': 'visualizar relatórios de vendas',
    'beneficio': 'tomar decisões estratégicas',
    'prioridade': 'ALTA',
    'estimativa': '8 tarefas'
}

response = requests.put(url, json=payload)
print(response.json())
```

---

## 5️⃣ Deletar História

### cURL

```bash
curl -X DELETE http://localhost:8080/api/historias/1
```

### JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/historias/1', {
  method: 'DELETE'
});

console.log(response.status); // 204
```

### Python/Requests

```python
import requests

url = 'http://localhost:8080/api/historias/1'
response = requests.delete(url)
print(response.status_code)  # 204
```

---

## 6️⃣ Listar Sprints

### cURL

```bash
curl -X GET http://localhost:8080/api/sprints/projeto/1 \
  -H "Content-Type: application/json"
```

### JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/sprints/projeto/1');
const sprints = await response.json();
console.log(sprints);
```

---

## 7️⃣ Criar Sprint

### cURL

```bash
curl -X POST http://localhost:8080/api/sprints \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Semana 1",
    "periodo": "05/11 - 12/11"
  }'
```

### JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/sprints', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    titulo: 'Semana 1',
    periodo: '05/11 - 12/11'
  })
});

const sprint = await response.json();
console.log(sprint);
```

---

## 8️⃣ Alocar História em Sprint

### cURL

```bash
curl -X POST http://localhost:8080/api/sprints/1/alocar-historia/1
```

### JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/sprints/1/alocar-historia/1', {
  method: 'POST'
});

console.log(response.status); // 200
```

---

## 9️⃣ Desalocar História de Sprint

### cURL

```bash
curl -X POST http://localhost:8080/api/sprints/1/desalocar-historia/1
```

### JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/sprints/1/desalocar-historia/1', {
  method: 'POST'
});

console.log(response.status); // 200
```

---

## 🔟 Sincronizar com Jira

### cURL

```bash
curl -X POST http://localhost:8080/api/jira/sincronizar \
  -H "Content-Type: application/json" \
  -d '{
    "historiaIds": [1, 2, 3],
    "jiraProjectKey": "GROWUP"
  }'
```

### JavaScript/Fetch

```javascript
const response = await fetch('http://localhost:8080/api/jira/sincronizar', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    historiaIds: [1, 2, 3],
    jiraProjectKey: 'GROWUP'
  })
});

const result = await response.json();
console.log(result);
```

### Python/Requests

```python
import requests

url = 'http://localhost:8080/api/jira/sincronizar'
payload = {
    'historiaIds': [1, 2, 3],
    'jiraProjectKey': 'GROWUP'
}

response = requests.post(url, json=payload)
print(response.json())
```

---

## 📋 Fluxo Completo de Exemplo

### 1. Upload de Documento

```bash
curl -X POST http://localhost:8080/api/projetos/upload \
  -H "Content-Type: application/json" \
  -d '{
    "nomeProjeto": "Meu Projeto",
    "conteudoDocumento": "Como um usuário, eu quero fazer login",
    "contextoAdicional": "Contexto do projeto"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "nome": "Meu Projeto",
  "totalHistorias": 3,
  "historias": [...]
}
```

### 2. Listar Histórias

```bash
curl -X GET http://localhost:8080/api/projetos/1/historias
```

### 3. Criar Sprint

```bash
curl -X POST http://localhost:8080/api/sprints \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Semana 1",
    "periodo": "05/11 - 12/11"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "titulo": "Semana 1",
  "periodo": "05/11 - 12/11",
  "totalHistorias": 0
}
```

### 4. Alocar Histórias

```bash
curl -X POST http://localhost:8080/api/sprints/1/alocar-historia/1
curl -X POST http://localhost:8080/api/sprints/1/alocar-historia/2
```

### 5. Sincronizar com Jira

```bash
curl -X POST http://localhost:8080/api/jira/sincronizar \
  -H "Content-Type: application/json" \
  -d '{
    "historiaIds": [1, 2],
    "jiraProjectKey": "GROWUP"
  }'
```

---

## 🧪 Testando com Postman

### Importar Coleção

1. Abra Postman
2. Clique em **Import**
3. Cole o JSON abaixo:

```json
{
  "info": {
    "name": "GrowUp Backend",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Upload Documento",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"nomeProjeto\": \"Meu Projeto\", \"conteudoDocumento\": \"Como um usuário...\", \"contextoAdicional\": \"Contexto\"}"
        },
        "url": {
          "raw": "http://localhost:8080/api/projetos/upload",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "projetos", "upload"]
        }
      }
    }
  ]
}
```

---

## 🔍 Debugging

### Ver Logs do Backend

```bash
# Terminal onde o Backend está rodando
# Você verá logs como:

INFO com.growup.controller.UploadController - Recebido upload de documento para projeto: Meu Projeto
DEBUG com.growup.service.UserStoryService - Processando documento para projeto: Meu Projeto
INFO com.growup.service.UserStoryService - Projeto criado com ID: 1
```

### Verificar Resposta HTTP

```bash
# Ver headers da resposta
curl -i http://localhost:8080/api/projetos/1/historias

# Ver apenas status
curl -o /dev/null -s -w "%{http_code}" http://localhost:8080/api/projetos/1/historias
```

### Validar JSON

```bash
# Usar jq para formatar e validar JSON
curl http://localhost:8080/api/projetos/1/historias | jq .
```

---

## ⚠️ Códigos de Erro

| Código | Significado | Solução |
| --- | --- | --- |
| 200 | OK | Requisição bem-sucedida |
| 201 | Created | Recurso criado com sucesso |
| 204 | No Content | Operação bem-sucedida (sem retorno) |
| 400 | Bad Request | Verifique o JSON enviado |
| 404 | Not Found | Recurso não encontrado |
| 500 | Internal Server Error | Erro no servidor (verifique logs) |

---

## 💡 Dicas

1. **Use variáveis no Postman** para não repetir URLs:
   ```
   {{base_url}}/projetos/upload
   ```

2. **Salve respostas** para reutilizar em outras requisições

3. **Use o modo "Pre-request Script"** para gerar dados dinâmicos

4. **Teste com dados reais** antes de integrar com o Frontend

---

**Documento criado por**: Manus AI  
**Última atualização**: 23 de Novembro de 2025
