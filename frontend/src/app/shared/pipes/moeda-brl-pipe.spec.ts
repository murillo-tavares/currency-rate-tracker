import { MoedaBrlPipe } from './moeda-brl-pipe';

describe('MoedaBrlPipe', () => {
  const pipe = new MoedaBrlPipe();

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('formata moeda com 2 casas decimais por padrão', () => {
    expect(pipe.transform(5.4231, 'USD')).toBe('R$ 5,42');
  });

  it('formata moedas de baixo valor com 4 casas decimais', () => {
    expect(pipe.transform(0.00354039, 'ARS')).toBe('R$ 0,0035');
    expect(pipe.transform(0.0362, 'JPY')).toBe('R$ 0,0362');
  });

  it('formata valores altos com separador de milhar pt-BR', () => {
    expect(pipe.transform(617454.37, 'BTC')).toBe('R$ 617.454,37');
  });
});
