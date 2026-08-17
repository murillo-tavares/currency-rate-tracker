import { SelecaoPeriodo } from '../../core/models/timeframe.model';

const HORA_MS = 3_600_000;
const DIA_MS = 24 * HORA_MS;

export interface FiltroPeriodo {
  inicio?: string;
  fim?: string;
}

function paraLocalDateTime(data: Date): string {
  const pad = (n: number) => n.toString().padStart(2, '0');
  return (
    `${data.getFullYear()}-${pad(data.getMonth() + 1)}-${pad(data.getDate())}` +
    `T${pad(data.getHours())}:${pad(data.getMinutes())}:${pad(data.getSeconds())}`
  );
}

export function filtroParaSelecaoPeriodo(
  selecao: SelecaoPeriodo,
  agora = new Date(),
): FiltroPeriodo {
  switch (selecao.timeframe) {
    case '24h':
      return { inicio: paraLocalDateTime(new Date(agora.getTime() - DIA_MS)) };
    case '7d':
      return { inicio: paraLocalDateTime(new Date(agora.getTime() - 7 * DIA_MS)) };
    case '30d':
      return { inicio: paraLocalDateTime(new Date(agora.getTime() - 30 * DIA_MS)) };
    case 'custom':
      return {
        inicio: selecao.inicio ? `${selecao.inicio}T00:00:00` : undefined,
        fim: selecao.fim ? `${selecao.fim}T23:59:59` : undefined,
      };
  }
}
