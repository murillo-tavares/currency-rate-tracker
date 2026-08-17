const fs = require('fs');
const path = require('path');

// Carrega frontend/.env explicitamente, se existir (nunca commitado, ver .env.example).
// Não sobrescreve uma API_URL que já veio de fora (Docker ARG/ENV, painel da Vercel):
// dotenv, por padrão, só preenche o que ainda não está definido em process.env.
require('dotenv').config({ path: path.join(__dirname, '../.env') });

// Gera src/environments/environment.ts a partir de API_URL antes do build de producao.
// Sem valor nenhum (nem .env, nem env var externa), cai no caminho relativo de sempre;
// dev (npm start) nunca passa por aqui, continua fixo em environment.development.ts.
// "||" (nao "??") de proposito: um ARG do Docker nao passado chega aqui como string vazia,
// nao como undefined, e precisa cair no fallback tambem.
const apiUrl = process.env.API_URL || '/api/v1';

const destino = path.join(__dirname, '../src/environments/environment.ts');
const conteudo = `// Gerado por scripts/gerar-environment.js a partir de API_URL (.env ou env var); não editar à mão.
export const environment = {
  apiUrl: '${apiUrl}',
};
`;

fs.writeFileSync(destino, conteudo);
console.log(`environment.ts gerado com apiUrl: ${apiUrl}`);
