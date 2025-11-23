# Integração com Frontend Angular (GrowUp)

Este documento descreve como integrar o **GrowUp Backend** com o **GrowUp Frontend** (Angular 19).

## Configuração do Frontend

### 1. Instalar Dependências HTTP

O Frontend Angular já possui o `HttpClientModule` disponível. Certifique-se de que está importado em `app.config.ts`:

```typescript
import { HttpClientModule } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    // ... outras configurações
  ]
};
```

### 2. Criar Serviço de API

Crie um arquivo `src/app/services/api.service.ts`:

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // Upload de documento
  uploadDocumento(nomeProjeto: string, conteudo: string, contexto: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/projetos/upload`, {
      nomeProjeto,
      conteudoDocumento: conteudo,
      contextoAdicional: contexto
    });
  }

  // Listar histórias
  listarHistorias(projetoId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/projetos/${projetoId}/historias`);
  }

  // Atualizar história
  atualizarHistoria(id: number, historia: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/historias/${id}`, historia);
  }

  // Deletar história
  deletarHistoria(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/historias/${id}`);
  }

  // Listar sprints
  listarSprints(projetoId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/sprints/projeto/${projetoId}`);
  }

  // Sincronizar com Jira
  sincronizarComJira(historiaIds: number[], jiraProjectKey: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/jira/sincronizar`, {
      historiaIds,
      jiraProjectKey
    });
  }
}
```

### 3. Integrar no Componente Upload

Atualize `src/app/pages/upload-inicial/upload-inicial.component.ts`:

```typescript
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-upload-inicial',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './upload-inicial.component.html',
  styleUrls: ['./upload-inicial.component.css']
})
export class UploadInicialComponent {
  nomeProjeto: string = '';
  contextoAdicional: string = '';
  conteudoDocumento: string = '';
  arquivoSelecionado: File | null = null;
  carregando: boolean = false;
  erro: string = '';

  constructor(
    private router: Router,
    private apiService: ApiService
  ) {}

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.arquivoSelecionado = file;
      // Ler o conteúdo do arquivo
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.conteudoDocumento = e.target.result;
      };
      reader.readAsText(file);
    }
  }

  gerarRoadmap() {
    if (!this.nomeProjeto || !this.conteudoDocumento) {
      this.erro = 'Por favor, preencha todos os campos';
      return;
    }

    this.carregando = true;
    this.erro = '';

    this.apiService.uploadDocumento(
      this.nomeProjeto,
      this.conteudoDocumento,
      this.contextoAdicional
    ).subscribe({
      next: (response) => {
        // Armazenar ID do projeto
        localStorage.setItem('projetoId', response.id);
        this.router.navigate(['/backlog']);
      },
      error: (error) => {
        console.error('Erro ao processar documento', error);
        this.erro = 'Erro ao processar documento. Tente novamente.';
        this.carregando = false;
      }
    });
  }
}
```

### 4. Integrar no Componente Backlog

Atualize `src/app/pages/backlog/backlog.component.ts`:

```typescript
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { UserStory } from '../../models/user-story.model';

@Component({
  selector: 'app-backlog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './backlog.component.html',
  styleUrls: ['./backlog.component.css']
})
export class BacklogComponent implements OnInit {
  historias: UserStory[] = [];
  projetoId: number | null = null;
  carregando: boolean = false;

  constructor(
    private router: Router,
    private apiService: ApiService
  ) {}

  ngOnInit() {
    this.projetoId = Number(localStorage.getItem('projetoId'));
    if (this.projetoId) {
      this.carregarHistorias();
    }
  }

  carregarHistorias() {
    this.carregando = true;
    this.apiService.listarHistorias(this.projetoId!).subscribe({
      next: (historias) => {
        this.historias = historias;
        this.carregando = false;
      },
      error: (error) => {
        console.error('Erro ao carregar histórias', error);
        this.carregando = false;
      }
    });
  }

  deletar(id: number) {
    if (confirm('Tem certeza que deseja remover esta história?')) {
      this.apiService.deletarHistoria(id).subscribe({
        next: () => {
          this.historias = this.historias.filter(h => h.id !== id);
        },
        error: (error) => {
          console.error('Erro ao deletar história', error);
        }
      });
    }
  }

  alternarEdicao(historia: UserStory) {
    historia.editando = !historia.editando;
  }

  salvarEdicao(historia: UserStory) {
    this.apiService.atualizarHistoria(historia.id, historia).subscribe({
      next: () => {
        historia.editando = false;
      },
      error: (error) => {
        console.error('Erro ao atualizar história', error);
      }
    });
  }

  avancar() {
    this.router.navigate(['/roadmap']);
  }
}
```

### 5. Integrar no Componente Roadmap

Atualize `src/app/pages/roadmap/roadmap.component.ts`:

```typescript
import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-roadmap',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './roadmap.component.html',
  styleUrls: ['./roadmap.component.css']
})
export class RoadmapComponent implements OnInit {
  jiraKey: string = 'GROWUP';
  statusSincronizacao: 'PARADO' | 'CARREGANDO' | 'SUCESSO' = 'PARADO';
  sprints: any[] = [];
  projetoId: number | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.projetoId = Number(localStorage.getItem('projetoId'));
    if (this.projetoId) {
      this.carregarSprints();
    }
  }

  carregarSprints() {
    this.apiService.listarSprints(this.projetoId!).subscribe({
      next: (sprints) => {
        this.sprints = sprints;
      },
      error: (error) => {
        console.error('Erro ao carregar sprints', error);
      }
    });
  }

  sincronizarComJira() {
    if (!this.jiraKey) return;

    this.statusSincronizacao = 'CARREGANDO';

    // Coletar IDs das histórias
    const historiaIds: number[] = [];
    this.sprints.forEach(sprint => {
      sprint.cards.forEach((card: any) => {
        historiaIds.push(card.id);
      });
    });

    this.apiService.sincronizarComJira(historiaIds, this.jiraKey).subscribe({
      next: (response) => {
        this.statusSincronizacao = 'SUCESSO';
        setTimeout(() => {
          this.statusSincronizacao = 'PARADO';
        }, 4000);
      },
      error: (error) => {
        console.error('Erro ao sincronizar com Jira', error);
        this.statusSincronizacao = 'PARADO';
      }
    });
  }
}
```

## Configuração de CORS

O Backend já está configurado com CORS habilitado. Se tiver problemas, verifique o arquivo `src/main/java/com/growup/config/CorsConfig.java`.

## Variáveis de Ambiente

Para desenvolvimento local, use:

```typescript
private apiUrl = 'http://localhost:8080/api';
```

Para produção, use a URL do Backend deployado:

```typescript
private apiUrl = 'https://growup-backend.onrender.com/api';
```

Você pode usar variáveis de ambiente do Angular para isso:

```typescript
import { environment } from '../../environments/environment';

export class ApiService {
  private apiUrl = environment.apiUrl;
  // ...
}
```

## Testando a Integração

1. **Inicie o Backend**:
   ```bash
   cd growup-backend
   mvn spring-boot:run
   ```

2. **Inicie o Frontend**:
   ```bash
   cd growup-front
   ng serve
   ```

3. **Teste o fluxo**:
   - Acesse `http://localhost:4200`
   - Faça upload de um documento
   - Verifique se as histórias são geradas
   - Teste a edição e sincronização com Jira

## Troubleshooting

### Erro de CORS
Se receber erro de CORS, certifique-se de que:
1. O Backend está rodando na porta 8080
2. O Frontend está rodando na porta 4200
3. O CORS está habilitado no Backend

### Erro 404 ao chamar API
Verifique se a URL da API está correta e se o Backend está rodando.

### Erro de autenticação com Jira
Verifique se as credenciais do Jira estão corretas no arquivo `application.properties` do Backend.

## Próximos Passos

1. Implementar autenticação de usuários
2. Adicionar validação de formulários
3. Melhorar tratamento de erros
4. Implementar cache de dados
5. Adicionar testes E2E
