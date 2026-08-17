import { HttpClient } from '@angular/common/http';
import { Service, inject, signal } from '@angular/core';
import { Observable, Subject, map, switchMap } from 'rxjs';

import { environment } from '../../../environments/environment';
import {
  SessaoUsuario,
  carregarSessao,
  limparSessao,
  salvarSessao,
} from '../../shared/utils/auth-storage.util';
import { LoginResponse, UsuarioResponse } from '../models/auth.model';

@Service()
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly baseUrlAuth = `${environment.apiUrl}/auth`;
  private readonly baseUrlUsuarios = `${environment.apiUrl}/usuarios`;

  private readonly sessao = signal<SessaoUsuario | null>(carregarSessao());
  readonly usuarioLogado = this.sessao.asReadonly();

  /** Emite a cada pedido de autenticação. Quem exibe a UI de login (AuthModal) se inscreve nisso. */
  private readonly pedido$ = new Subject<void>();
  readonly pedidoAutenticacao$ = this.pedido$.asObservable();

  get token(): string | null {
    return this.sessao()?.token ?? null;
  }

  /** Sinaliza que alguma parte da aplicação precisa que o usuário se autentique agora. */
  solicitarAutenticacao(): void {
    this.pedido$.next();
  }

  /** O login não devolve o nome, só o cadastro devolve. */
  login(email: string, senha: string): Observable<void> {
    return this.http
      .post<LoginResponse>(`${this.baseUrlAuth}/login`, { email, senha })
      .pipe(map(({ token }) => this.definirSessao({ token, email })));
  }

  /** Cadastra e, na sequência, autentica com as mesmas credenciais, pois o cadastro sozinho não devolve token. */
  cadastrar(email: string, nome: string, senha: string): Observable<void> {
    return this.http.post<UsuarioResponse>(this.baseUrlUsuarios, { email, nome, senha }).pipe(
      switchMap(() => this.http.post<LoginResponse>(`${this.baseUrlAuth}/login`, { email, senha })),
      map(({ token }) => this.definirSessao({ token, email, nome })),
    );
  }

  logout(): void {
    this.sessao.set(null);
    limparSessao();
  }

  private definirSessao(sessao: SessaoUsuario): void {
    this.sessao.set(sessao);
    salvarSessao(sessao);
  }
}
