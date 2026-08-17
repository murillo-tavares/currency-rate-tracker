import { carregarSessao, limparSessao, salvarSessao } from './auth-storage.util';

describe('auth-storage', () => {
  beforeEach(() => localStorage.clear());

  it('retorna null quando nada foi salvo', () => {
    expect(carregarSessao()).toBeNull();
  });

  it('persiste e recarrega a sessão salva', () => {
    salvarSessao({ token: 'abc123', email: 'ana@email.com', nome: 'Ana' });
    expect(carregarSessao()).toEqual({ token: 'abc123', email: 'ana@email.com', nome: 'Ana' });
  });

  it('ignora conteúdo corrompido no storage', () => {
    localStorage.setItem('currency-rate-tracker:sessao', '{not json');
    expect(carregarSessao()).toBeNull();
  });

  it('remove a sessão salva', () => {
    salvarSessao({ token: 'abc123', email: 'ana@email.com' });
    limparSessao();
    expect(carregarSessao()).toBeNull();
  });
});
