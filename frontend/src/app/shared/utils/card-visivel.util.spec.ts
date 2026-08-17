import { Cotacao } from '../../core/models/cotacao.model';
import { GraficoCotacao } from '../../core/models/dashboard.model';
import { montarCards } from './card-visivel.util';

function criarCotacao(overrides: Partial<Cotacao> = {}): Cotacao {
  return {
    codigoMoeda: 'USD',
    nome: 'Dólar Americano',
    valor: 5.4,
    variacaoPercentual: 1.2,
    dataCotacao: '2026-08-15T12:00:00',
    ...overrides,
  };
}

describe('montarCards', () => {
  it('junta o histórico do dashboard com a cotação de mesmo código', () => {
    const cotacoes = [
      criarCotacao({ codigoMoeda: 'USD' }),
      criarCotacao({ codigoMoeda: 'EUR', nome: 'Euro' }),
    ];
    const graficos: GraficoCotacao[] = [
      {
        codigoMoeda: 'USD',
        pontos: [{ valor: 5.3, variacaoPercentual: 1, dataCotacao: '2026-08-15T11:00:00' }],
      },
    ];

    const cards = montarCards(cotacoes, graficos, new Set());

    expect(cards.find((c) => c.cotacao.codigoMoeda === 'USD')?.pontos.length).toBe(1);
    expect(cards.find((c) => c.cotacao.codigoMoeda === 'EUR')?.pontos).toEqual([]);
  });

  it('marca como favorita apenas os códigos presentes no Set', () => {
    const cotacoes = [criarCotacao({ codigoMoeda: 'USD' }), criarCotacao({ codigoMoeda: 'EUR' })];

    const cards = montarCards(cotacoes, [], new Set(['EUR']));

    expect(cards.find((c) => c.cotacao.codigoMoeda === 'USD')?.favorita).toBe(false);
    expect(cards.find((c) => c.cotacao.codigoMoeda === 'EUR')?.favorita).toBe(true);
  });

  it('retorna lista vazia sem cotações', () => {
    expect(montarCards([], [], new Set())).toEqual([]);
  });
});
