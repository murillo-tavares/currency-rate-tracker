export interface SessaoUsuario {
  token: string;
  email: string;
  nome?: string;
}

const CHAVE_SESSAO = 'currency-rate-tracker:sessao';

/** Lê a sessão salva. Falha de parse (storage corrompido ou alterado por fora) retorna null em vez de quebrar a aplicação. */
export function carregarSessao(): SessaoUsuario | null {
  try {
    const bruto = localStorage.getItem(CHAVE_SESSAO);
    return bruto ? JSON.parse(bruto) : null;
  } catch {
    return null;
  }
}

export function salvarSessao(sessao: SessaoUsuario): void {
  localStorage.setItem(CHAVE_SESSAO, JSON.stringify(sessao));
}

export function limparSessao(): void {
  localStorage.removeItem(CHAVE_SESSAO);
}
