import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthService } from '../../../core/services/auth';
import { AppHeader } from './app-header';

const CHAVE_SESSAO = 'currency-rate-tracker:sessao';

describe('AppHeader', () => {
  function elemento(fixture: ComponentFixture<AppHeader>): HTMLElement {
    return fixture.nativeElement as HTMLElement;
  }

  describe('deslogado', () => {
    let fixture: ComponentFixture<AppHeader>;
    let auth: AuthService;

    beforeEach(async () => {
      localStorage.clear();
      await TestBed.configureTestingModule({
        imports: [AppHeader],
        providers: [provideHttpClient(), provideHttpClientTesting()],
      }).compileComponents();

      auth = TestBed.inject(AuthService);
      fixture = TestBed.createComponent(AppHeader);
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(fixture.componentInstance).toBeTruthy();
    });

    it('exibe a marca da aplicação', () => {
      expect(elemento(fixture).textContent).toContain('CotaçãoBR');
    });

    it('mostra o botão Entrar, sem nome nem avatar', () => {
      expect(elemento(fixture).querySelector('.entrar')).toBeTruthy();
      expect(elemento(fixture).querySelector('.avatar')).toBeNull();
    });

    it('clicar em Entrar solicita autenticação', () => {
      let notificacoes = 0;
      auth.pedidoAutenticacao$.subscribe(() => notificacoes++);

      elemento(fixture).querySelector<HTMLButtonElement>('.entrar')!.click();

      expect(notificacoes).toBe(1);
    });
  });

  describe('logado', () => {
    let fixture: ComponentFixture<AppHeader>;
    let auth: AuthService;

    function autenticarComo(sessao: { token: string; email: string; nome?: string }): void {
      localStorage.setItem(CHAVE_SESSAO, JSON.stringify(sessao));
      TestBed.configureTestingModule({
        imports: [AppHeader],
        providers: [provideHttpClient(), provideHttpClientTesting()],
      });
      auth = TestBed.inject(AuthService);
      fixture = TestBed.createComponent(AppHeader);
      fixture.detectChanges();
    }

    beforeEach(() => localStorage.clear());

    it('mostra nome, iniciais e Sair; não mostra Entrar', () => {
      autenticarComo({ token: 'jwt-abc', email: 'ana@email.com', nome: 'Ana Souza' });

      expect(elemento(fixture).querySelector('.entrar')).toBeNull();
      expect(elemento(fixture).querySelector('.identidade .nome')?.textContent).toBe('Ana Souza');
      expect(elemento(fixture).querySelector('.avatar')?.textContent).toBe('A');
    });

    it('sem nome salvo (login direto, sem cadastro nesta sessão): usa o e-mail como nome de exibição', () => {
      autenticarComo({ token: 'jwt-abc', email: 'ana@email.com' });

      expect(elemento(fixture).querySelector('.identidade .nome')?.textContent).toBe(
        'ana@email.com',
      );
      expect(elemento(fixture).querySelector('.avatar')?.textContent).toBe('A');
    });

    it('clicar em Sair encerra a sessão', () => {
      autenticarComo({ token: 'jwt-abc', email: 'ana@email.com', nome: 'Ana Souza' });

      elemento(fixture).querySelector<HTMLButtonElement>('.sair')!.click();

      expect(auth.usuarioLogado()).toBeNull();
    });
  });
});
