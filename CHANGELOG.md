# Changelog

Todas as mudanças notáveis deste projeto serão documentadas neste arquivo.

O formato segue o [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [1.0.0] - 2026-08-17

### Added
- Catálogo de moedas (`GET /moedas`), populado via seed do Flyway.
- Integração com a AwesomeAPI para consulta de cotações.
- Cotação atual das moedas (`GET /cotacoes`), com filtro opcional por código, servida a
  partir do banco e guardada em cache Redis.
- Job agendado que atualiza as cotações periodicamente, evitando persistir registros
  repetidos quando a origem não muda o valor entre ciclos.
- Dashboard de cotações em lote (`GET /cotacoes/dashboard`), com filtro por moeda e por
  intervalo de datas, agrupando o histórico num gráfico por moeda.
- Tratamento global de erros com Zalando Problem e catálogo de exceptions de negócio.
- Especificação OpenAPI, com Swagger UI e Scalar para os endpoints de Moeda e Cotação.
- Página inicial no frontend (Angular) com as cotações em tempo real e gráfico com
  tooltip ao passar o mouse.
- Docker Compose com Postgres, Redis, backend e frontend, para subir a aplicação
  completa com um único comando.
- Suíte de testes dividida em unitário e integração (Testcontainers).

[1.0.0]: https://github.com/murillo-tavares/currency-rate-tracker/releases/tag/v1.0.0
