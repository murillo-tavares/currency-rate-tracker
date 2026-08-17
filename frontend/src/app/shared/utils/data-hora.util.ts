export function formatarHora(data: Date): string {
  return data.toLocaleTimeString('pt-BR');
}

export function formatarDataHora(dataIso: string): string {
  return new Date(dataIso).toLocaleString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  });
}
