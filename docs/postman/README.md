# Postman

- [currency-rate-tracker-api.postman_collection.json](currency-rate-tracker-api.postman_collection.json)
- [currency-rate-tracker-api.local.postman_environment.json](currency-rate-tracker-api.local.postman_environment.json) — `baseUrl` apontando pra API local (`localhost:8080`)

A pasta "Autenticação" → "Login" salva o token retornado na variável de environment `token` automaticamente,
então as requisições da pasta "Favorito" já usam `Authorization: Bearer {{token}}` sem precisar copiar e colar
nada, só rodar "Cadastrar usuário" (uma vez) e depois "Login" antes de testar favoritos.

> **Importando:** no Postman, **Import** → arraste a collection e o environment → selecione o environment
> "Currency Rate Tracker API - Local" no seletor do canto superior direito.
