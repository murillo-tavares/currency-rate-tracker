# Postman

- [currency-rate-tracker-api.postman_collection.json](currency-rate-tracker-api.postman_collection.json)
- [currency-rate-tracker-api.local.postman_environment.json](currency-rate-tracker-api.local.postman_environment.json) — `baseUrl` apontando pra API local (`localhost:8080`)
- [currency-rate-tracker-api.render.postman_environment.json](currency-rate-tracker-api.render.postman_environment.json) — `baseUrl` apontando pra API hospedada no Render

Troca de ambiente é só selecionar o environment correspondente no Postman; o resto da collection não muda.

A pasta "Autenticação" → "Login" salva o token retornado na variável de environment `token` automaticamente,
então as requisições da pasta "Favorito" já usam `Authorization: Bearer {{token}}` sem precisar copiar e colar
nada, só rodar "Cadastrar usuário" (uma vez) e depois "Login" antes de testar favoritos.

> **Importando:** no Postman, **Import** → arraste a collection e o(s) environment(s) → selecione o environment
> desejado no seletor do canto superior direito.
