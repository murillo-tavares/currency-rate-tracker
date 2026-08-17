import { decimaisPara } from './moeda-decimais.util';

describe('decimaisPara', () => {
  it('retorna 4 casas para moedas de baixo valor (ARS, JPY)', () => {
    expect(decimaisPara('ARS')).toBe(4);
    expect(decimaisPara('JPY')).toBe(4);
  });

  it('retorna 2 casas como padrão para as demais moedas', () => {
    expect(decimaisPara('USD')).toBe(2);
    expect(decimaisPara('BTC')).toBe(2);
    expect(decimaisPara('CODIGO_INEXISTENTE')).toBe(2);
  });
});
