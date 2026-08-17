import { Cotacao } from '../../core/models/cotacao.model';
import { GraficoCotacao, PontoHistorico } from '../../core/models/dashboard.model';

export interface CardVisivel {
  cotacao: Cotacao;
  pontos: PontoHistorico[];
  favorita: boolean;
}

export function montarCards(
  cotacoes: Cotacao[],
  graficos: GraficoCotacao[],
  favoritas: Set<string>,
): CardVisivel[] {
  const pontosPorCodigo = new Map(graficos.map((grafico) => [grafico.codigoMoeda, grafico.pontos]));

  return cotacoes.map((cotacao) => ({
    cotacao,
    pontos: pontosPorCodigo.get(cotacao.codigoMoeda) ?? [],
    favorita: favoritas.has(cotacao.codigoMoeda),
  }));
}
