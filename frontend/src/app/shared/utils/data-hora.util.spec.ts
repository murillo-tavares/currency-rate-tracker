import { formatarDataHora, formatarHora } from './data-hora.util';

describe('formatarHora', () => {
  it('formata só a hora, no padrão pt-BR', () => {
    const data = new Date(2026, 7, 16, 9, 5, 30);
    expect(formatarHora(data)).toBe(data.toLocaleTimeString('pt-BR'));
  });
});

describe('formatarDataHora', () => {
  it('formata dia/mês e hora:minuto, no padrão pt-BR', () => {
    expect(formatarDataHora('2026-08-15T12:05:00')).toBe('15/08, 12:05');
  });
});
