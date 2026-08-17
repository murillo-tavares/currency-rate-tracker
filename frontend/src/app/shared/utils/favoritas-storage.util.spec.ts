import { carregarFavoritas, salvarFavoritas } from './favoritas-storage.util';

describe('favoritas-storage', () => {
  beforeEach(() => localStorage.clear());

  it('retorna lista vazia quando nada foi salvo', () => {
    expect(carregarFavoritas()).toEqual([]);
  });

  it('persiste e recarrega os códigos salvos', () => {
    salvarFavoritas(['USD', 'EUR']);
    expect(carregarFavoritas()).toEqual(['USD', 'EUR']);
  });

  it('ignora conteúdo corrompido no storage', () => {
    localStorage.setItem('currency-rate-tracker:favoritas', '{not json');
    expect(carregarFavoritas()).toEqual([]);
  });
});
