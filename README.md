# 💱 Currency Rate Tracker

API de cotação de moedas: catálogo de moedas, cotações atuais/históricas com cache, dashboard em gráfico,
cadastro de usuário, login e favoritos. Backend (Spring Boot) e frontend (Angular) no mesmo repositório.

<p>
  <img src="https://img.shields.io/badge/Java-21-e76f00?logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1-6db33f?logo=springboot&logoColor=white" alt="Spring Boot 4.1">
  <img src="https://img.shields.io/badge/Angular-22-dd0031?logo=angular&logoColor=white" alt="Angular 22">
  <img src="https://img.shields.io/badge/PostgreSQL-4169e1?logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Redis-dc382d?logo=redis&logoColor=white" alt="Redis">
  <img src="https://img.shields.io/badge/Testcontainers-2496ed?logo=docker&logoColor=white" alt="Testcontainers">
</p>

## 🚀 Como executar

Sobe Postgres + Redis + API + frontend:

```bash
docker compose up --build
```

- API: http://localhost:8080/api/v1
- Frontend: http://localhost:4200

Alternativa rodando back/front localmente (IDE/debug), só a infra em container:

```bash
docker compose up postgres redis -d
cd backend && ./mvnw spring-boot:run
cd frontend && npm start
```

## 📖 Documentação

A spec fica em [openapi.yaml](backend/src/main/resources/static/openapi.yaml), um arquivo próprio em vez de
gerada por anotação no controller. **Decisão:** controller fica limpo, e a doc é livre pra customizar sem mexer
em Java.

- 🌀 Scalar: http://localhost:8080/api/v1/scalar.html
- 🧭 Swagger UI (alternativa): http://localhost:8080/api/v1/swagger-ui/index.html
- 📄 Spec: http://localhost:8080/api/v1/openapi.yaml

### 📮 Postman

- [currency-rate-tracker-api.postman_collection.json](docs/postman/currency-rate-tracker-api.postman_collection.json)
- [currency-rate-tracker-api.local.postman_environment.json](docs/postman/currency-rate-tracker-api.local.postman_environment.json)

Mais detalhes (import, fluxo de login) em [docs/postman/README.md](docs/postman/README.md).

## 🧪 Testes

Rodar a partir de `backend/`.

### Teste unitário

Rápido, sem infra:

```bash
./mvnw test
```

### Teste de integração

Sobe Postgres + Redis reais via Testcontainers. Precisa de **Docker rodando**:

```bash
./mvnw test -Dsurefire.excludedGroups= -Dsurefire.groups=integration
```

## 🏷️ Versionamento

API versionada por path (`/api/v1`).

Releases seguem [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) e
[Semantic Versioning](https://semver.org/lang/pt-BR/), com tag `vX.Y.Z` no repositório.

- 📝 Changelog: [CHANGELOG.md](CHANGELOG.md)
- 🚀 Releases: https://github.com/murillo-tavares/currency-rate-tracker/releases

## 🧠 Cache com Redis

Cotação atual e dashboard de cotações passam por cache antes de bater no banco. O valor só muda quando o job
agendado atualiza, então não faz sentido consultar o Postgres a cada requisição. Usa-se Redis como cache.

## 🧭 Decisões

### 📁 Back e front no mesmo repositório

Diferente de outros projetos onde back e front vivem em repositórios separados, aqui os dois moram juntos
(`backend/`, `frontend/`), compartilhando o mesmo `compose.yaml` na raiz. Facilita a leitura de quem for avaliar
o projeto: um repositório só pra clonar, um comando só pra subir tudo, em vez de precisar navegar entre dois
lugares diferentes.

### 🔓 Login simples, restrito a favoritos

A autenticação (cadastro + login com JWT) existe apenas nos endpoints de favoritos. É a única parte da API que
realmente precisa saber "de quem" é a requisição. O resto (moedas, cotações, dashboard) continua público, de
propósito: menos fricção pra quem for testar/avaliar a API sem precisar logar toda hora, e menos complexidade de
infraestrutura de autenticação (sem Authorization Server, sem sessão).

### 📊 Chart.js pros gráficos

O gráfico de cotações no frontend usa [Chart.js](https://www.chartjs.org/) (via `ng2-charts`), biblioteca
madura e leve pra série temporal, sem precisar escrever renderização de gráfico na mão.
