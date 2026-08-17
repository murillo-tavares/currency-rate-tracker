import { lerVariavelCss } from './css-var.util';

describe('lerVariavelCss', () => {
  afterEach(() => {
    document.documentElement.style.removeProperty('--cor-teste');
  });

  it('lê o valor de uma variável CSS definida no elemento raiz', () => {
    document.documentElement.style.setProperty('--cor-teste', 'oklch(0.75 0.16 145)');

    expect(lerVariavelCss('--cor-teste')).toBe('oklch(0.75 0.16 145)');
  });

  it('retorna string vazia para uma variável inexistente', () => {
    expect(lerVariavelCss('--nao-existe')).toBe('');
  });
});
