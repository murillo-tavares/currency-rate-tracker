import { filtroParaSelecaoPeriodo } from './filtro-periodo.util';

describe('filtroParaSelecaoPeriodo', () => {
  const agora = new Date(2026, 7, 15, 18, 30, 0);

  it('calcula início 24h atrás para o preset 24h', () => {
    expect(filtroParaSelecaoPeriodo({ timeframe: '24h' }, agora)).toEqual({
      inicio: '2026-08-14T18:30:00',
    });
  });

  it('calcula início 7 dias atrás para o preset 7d', () => {
    expect(filtroParaSelecaoPeriodo({ timeframe: '7d' }, agora)).toEqual({
      inicio: '2026-08-08T18:30:00',
    });
  });

  it('calcula início 30 dias atrás para o preset 30d', () => {
    expect(filtroParaSelecaoPeriodo({ timeframe: '30d' }, agora)).toEqual({
      inicio: '2026-07-16T18:30:00',
    });
  });

  it('converte datas do modo personalizado para início/fim do dia', () => {
    expect(
      filtroParaSelecaoPeriodo(
        { timeframe: 'custom', inicio: '2026-08-01', fim: '2026-08-10' },
        agora,
      ),
    ).toEqual({
      inicio: '2026-08-01T00:00:00',
      fim: '2026-08-10T23:59:59',
    });
  });

  it('modo personalizado sem datas retorna filtro vazio', () => {
    expect(filtroParaSelecaoPeriodo({ timeframe: 'custom' }, agora)).toEqual({
      inicio: undefined,
      fim: undefined,
    });
  });
});
