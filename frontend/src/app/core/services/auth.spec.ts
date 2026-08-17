import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { AuthService } from './auth';

const CHAVE_SESSAO = 'currency-rate-tracker:sessao';

describe('AuthService', () => {
  let httpMock: HttpTestingController;

  afterEach(() => {
    httpMock.verify();
  });

  describe('sem sessão salva', () => {
    let service: AuthService;

    beforeEach(() => {
      localStorage.clear();
      TestBed.configureTestingModule({
        providers: [provideHttpClient(), provideHttpClientTesting()],
      });
      service = TestBed.inject(AuthService);
      httpMock = TestBed.inject(HttpTestingController);
    });

    it('should be created', () => {
      expect(service).toBeTruthy();
    });

    it('não tem usuário logado', () => {
      expect(service.usuarioLogado()).toBeNull();
      expect(service.token).toBeNull();
    });

    it('login autentica e guarda token + email na sessão', () => {
      let concluiu = false;
      service.login('ana@email.com', 'segredo123').subscribe(() => (concluiu = true));

      const req = httpMock.expectOne(`${environment.apiUrl}/auth/login`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({ email: 'ana@email.com', senha: 'segredo123' });
      req.flush({ token: 'jwt-abc' });

      expect(concluiu).toBe(true);
      expect(service.usuarioLogado()).toEqual({ token: 'jwt-abc', email: 'ana@email.com' });
      expect(service.token).toBe('jwt-abc');
    });

    it('login persiste a sessão no localStorage', () => {
      service.login('ana@email.com', 'segredo123').subscribe();
      httpMock.expectOne(`${environment.apiUrl}/auth/login`).flush({ token: 'jwt-abc' });

      expect(localStorage.getItem(CHAVE_SESSAO)).toContain('jwt-abc');
    });

    it('cadastrar cria o usuário e encadeia login pra obter o token', () => {
      let concluiu = false;
      service.cadastrar('ana@email.com', 'Ana', 'segredo123').subscribe(() => (concluiu = true));

      const reqCadastro = httpMock.expectOne(`${environment.apiUrl}/usuarios`);
      expect(reqCadastro.request.method).toBe('POST');
      expect(reqCadastro.request.body).toEqual({
        email: 'ana@email.com',
        nome: 'Ana',
        senha: 'segredo123',
      });
      reqCadastro.flush({ email: 'ana@email.com', nome: 'Ana' });

      const reqLogin = httpMock.expectOne(`${environment.apiUrl}/auth/login`);
      expect(reqLogin.request.body).toEqual({ email: 'ana@email.com', senha: 'segredo123' });
      reqLogin.flush({ token: 'jwt-abc' });

      expect(concluiu).toBe(true);
      expect(service.usuarioLogado()).toEqual({
        token: 'jwt-abc',
        email: 'ana@email.com',
        nome: 'Ana',
      });
    });

    it('não chama login se o cadastro falhar', () => {
      service.cadastrar('ana@email.com', 'Ana', 'segredo123').subscribe({ error: () => {} });

      httpMock
        .expectOne(`${environment.apiUrl}/usuarios`)
        .flush(
          { message: 'Já existe um usuário cadastrado com o email ana@email.com' },
          { status: 409, statusText: 'Conflict' },
        );

      httpMock.expectNone(`${environment.apiUrl}/auth/login`);
    });

    it('logout limpa a sessão em memória e no storage', () => {
      service.login('ana@email.com', 'segredo123').subscribe();
      httpMock.expectOne(`${environment.apiUrl}/auth/login`).flush({ token: 'jwt-abc' });

      service.logout();

      expect(service.usuarioLogado()).toBeNull();
      expect(service.token).toBeNull();
      expect(localStorage.getItem(CHAVE_SESSAO)).toBeNull();
    });

    it('notifica quem está inscrito a cada solicitação de autenticação', () => {
      let notificacoes = 0;
      service.pedidoAutenticacao$.subscribe(() => notificacoes++);

      service.solicitarAutenticacao();
      expect(notificacoes).toBe(1);

      service.solicitarAutenticacao();
      expect(notificacoes).toBe(2);
    });

    it('solicitar autenticação sem ninguém inscrito não quebra', () => {
      expect(() => service.solicitarAutenticacao()).not.toThrow();
    });
  });

  describe('com sessão salva', () => {
    it('restaura a sessão ao construir o serviço', () => {
      localStorage.setItem(
        CHAVE_SESSAO,
        JSON.stringify({ token: 'jwt-antigo', email: 'ana@email.com', nome: 'Ana' }),
      );

      TestBed.configureTestingModule({
        providers: [provideHttpClient(), provideHttpClientTesting()],
      });
      const service = TestBed.inject(AuthService);
      httpMock = TestBed.inject(HttpTestingController);

      expect(service.usuarioLogado()).toEqual({
        token: 'jwt-antigo',
        email: 'ana@email.com',
        nome: 'Ana',
      });
    });
  });
});
