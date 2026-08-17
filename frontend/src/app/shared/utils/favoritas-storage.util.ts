const CHAVE_FAVORITAS = 'currency-rate-tracker:favoritas';

export function carregarFavoritas(): string[] {
  try {
    const bruto = localStorage.getItem(CHAVE_FAVORITAS);
    return bruto ? JSON.parse(bruto) : [];
  } catch {
    return [];
  }
}

export function salvarFavoritas(codigos: Iterable<string>): void {
  localStorage.setItem(CHAVE_FAVORITAS, JSON.stringify([...codigos]));
}
