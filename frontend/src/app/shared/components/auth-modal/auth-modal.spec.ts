import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { environment } from '../../../../environments/environment';
import { AuthService } from '../../../core/services/auth';
import { AuthModal } from './auth-modal';

describe('AuthModal', () => {
  let fixture: ComponentFixture<AuthModal>;
  let auth: AuthService;
  let httpMock: HttpTestingController;
  const baseUrlAuth = `${environment.apiUrl}/auth`;
  const baseUrlUsuarios = `${environment.apiUrl}/usuarios`;

  beforeEach(async () => {
    localStorage.clear();
    await TestBed.configureTestingModule({
      imports: [AuthModal],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
    auth = TestBed.inject(AuthService);
    fixture = TestBed.createComponent(AuthModal);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  function elemento(): HTMLElement {
    return fixture.nativeElement as HTMLElement;
  }

  function modalAberto(): boolean {
    return !!elemento().querySelector('.modal');
  }

  /** Simula quem pede autenticação de fora (AppHeader, CotacoesDashboard): sempre abre em login. */
  function pedirAutenticacao(): void {
    auth.solicitarAutenticacao();
    fixture.detectChanges();
  }

  function irParaAbaCadastro(): void {
    Array.from(elemento().querySelectorAll<HTMLButtonElement>('.aba'))
      .find((b) => b.textContent?.trim() === 'Criar conta')!
      .click();
    fixture.detectChanges();
  }

  function preencher(seletor: string, valor: string): void {
    const input = elemento().querySelector<HTMLInputElement>(seletor)!;
    input.value = valor;
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  }

  function submeter(): void {
    elemento()
      .querySelector<HTMLFormElement>('form')!
      .dispatchEvent(new Event('submit', { cancelable: true }));
    fixture.detectChanges();
  }

  /**
   * dispatchEvent(new Event('submit')) pula a validação nativa do form (não passa pelo
   * "algoritmo de submissão" do browser); só um clique de verdade no botão de submit exercita
   * a validação nativa (required/minlength/type=email) de verdade.
   */
  function clicarEnviar(): void {
    elemento().querySelector<HTMLButtonElement>('.botao-enviar')!.click();
    fixture.detectChanges();
  }

  it('should create', () => {
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('não renderiza nada antes de qualquer pedido de autenticação', () => {
    expect(modalAberto()).toBe(false);
  });

  it('um pedido de autenticação abre no modo login: sem campo Nome, botão "Entrar"', () => {
    pedirAutenticacao();

    expect(modalAberto()).toBe(true);
    expect(elemento().querySelector('input[type="text"]')).toBeNull();
    expect(elemento().querySelector('.botao-enviar')?.textContent?.trim()).toBe('Entrar');
  });

  it('um novo pedido não reabre nem reseta a aba se o modal já estiver aberto', () => {
    pedirAutenticacao();
    irParaAbaCadastro();
    expect(elemento().querySelector('input[type="text"]')).toBeTruthy();

    pedirAutenticacao();

    expect(elemento().querySelector('input[type="text"]')).toBeTruthy();
    expect(elemento().querySelector('.botao-enviar')?.textContent?.trim()).toBe('Criar conta');
  });

  it('troca de aba entre Entrar e Criar conta', () => {
    pedirAutenticacao();

    irParaAbaCadastro();

    expect(elemento().querySelector('input[type="text"]')).toBeTruthy();
    expect(elemento().querySelector('.botao-enviar')?.textContent?.trim()).toBe('Criar conta');
  });

  it('faz login com o e-mail e senha digitados e fecha o modal ao ter sucesso', () => {
    pedirAutenticacao();

    preencher('input[type="email"]', 'ana@email.com');
    preencher('input[type="password"]', 'segredo123');
    submeter();

    const req = httpMock.expectOne(`${baseUrlAuth}/login`);
    expect(req.request.body).toEqual({ email: 'ana@email.com', senha: 'segredo123' });
    req.flush({ token: 'jwt-abc' });
    fixture.detectChanges();

    expect(auth.usuarioLogado()).toEqual({ token: 'jwt-abc', email: 'ana@email.com' });
    expect(modalAberto()).toBe(false);
  });

  it('cadastra com nome/e-mail/senha e encadeia o login', () => {
    pedirAutenticacao();
    irParaAbaCadastro();

    preencher('input[type="text"]', 'Ana');
    preencher('input[type="email"]', 'ana@email.com');
    preencher('input[type="password"]', 'segredo123');
    submeter();

    const reqCadastro = httpMock.expectOne(baseUrlUsuarios);
    expect(reqCadastro.request.body).toEqual({
      email: 'ana@email.com',
      nome: 'Ana',
      senha: 'segredo123',
    });
    reqCadastro.flush({ email: 'ana@email.com', nome: 'Ana' });

    httpMock.expectOne(`${baseUrlAuth}/login`).flush({ token: 'jwt-abc' });
    fixture.detectChanges();

    expect(auth.usuarioLogado()).toEqual({ token: 'jwt-abc', email: 'ana@email.com', nome: 'Ana' });
    expect(modalAberto()).toBe(false);
  });

  it('mostra a mensagem de erro do backend e mantém o modal aberto quando falha', () => {
    pedirAutenticacao();

    preencher('input[type="email"]', 'ana@email.com');
    preencher('input[type="password"]', 'senha-errada');
    submeter();

    httpMock
      .expectOne(`${baseUrlAuth}/login`)
      .flush({ message: 'Email ou senha inválidos' }, { status: 401, statusText: 'Unauthorized' });
    fixture.detectChanges();

    expect(elemento().querySelector('.erro')?.textContent).toBe('Email ou senha inválidos');
    expect(modalAberto()).toBe(true);
  });

  it('desabilita o botão e mostra "Aguarde…" enquanto a requisição está em andamento', () => {
    pedirAutenticacao();

    preencher('input[type="email"]', 'ana@email.com');
    preencher('input[type="password"]', 'segredo123');
    submeter();

    const botao = elemento().querySelector<HTMLButtonElement>('.botao-enviar')!;
    expect(botao.disabled).toBe(true);
    expect(botao.textContent?.trim()).toBe('Aguarde…');

    httpMock.expectOne(`${baseUrlAuth}/login`).flush({ token: 'jwt-abc' });
  });

  it('fecha ao clicar fora do modal', () => {
    pedirAutenticacao();

    elemento().querySelector<HTMLElement>('.fundo')!.click();
    fixture.detectChanges();

    expect(modalAberto()).toBe(false);
  });

  it('não fecha ao clicar dentro do modal', () => {
    pedirAutenticacao();

    elemento().querySelector<HTMLElement>('.modal')!.click();
    fixture.detectChanges();

    expect(modalAberto()).toBe(true);
  });

  it('reabre em login mesmo se a última vez tiver fechado em cadastro', () => {
    pedirAutenticacao();
    irParaAbaCadastro();
    elemento().querySelector<HTMLElement>('.fundo')!.click();
    fixture.detectChanges();
    expect(modalAberto()).toBe(false);

    pedirAutenticacao();

    expect(elemento().querySelector('input[type="text"]')).toBeNull();
    expect(elemento().querySelector('.botao-enviar')?.textContent?.trim()).toBe('Entrar');
  });

  describe('validação nativa do form (required/minlength/type=email)', () => {
    it('não envia com o formulário vazio', () => {
      pedirAutenticacao();

      clicarEnviar();

      httpMock.expectNone(`${baseUrlAuth}/login`);
      expect(modalAberto()).toBe(true);
    });

    it('não envia com e-mail em formato inválido', () => {
      pedirAutenticacao();

      preencher('input[type="email"]', 'não-é-um-email');
      preencher('input[type="password"]', 'segredo123');
      clicarEnviar();

      httpMock.expectNone(`${baseUrlAuth}/login`);
    });

    it('exige pelo menos 6 caracteres de senha', () => {
      // tooShort/minlength só valida em valor digitado por interação real do usuário (flag
      // interna do browser); setar .value programaticamente não ativa isso, em nenhum browser.
      // Verifica o atributo em vez de tentar simular a "sujeira" que só um keystroke real seta.
      pedirAutenticacao();

      const senha = elemento().querySelector<HTMLInputElement>('input[type="password"]')!;
      expect(senha.minLength).toBe(6);
    });

    it('cadastro não envia sem o nome preenchido', () => {
      pedirAutenticacao();
      irParaAbaCadastro();

      preencher('input[type="email"]', 'ana@email.com');
      preencher('input[type="password"]', 'segredo123');
      clicarEnviar();

      httpMock.expectNone(baseUrlUsuarios);
    });
  });
});
