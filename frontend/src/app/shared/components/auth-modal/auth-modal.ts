import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';

import { AuthService } from '../../../core/services/auth';

type ModoAuth = 'login' | 'cadastro';

@Component({
  selector: 'app-auth-modal',
  imports: [FormsModule],
  templateUrl: './auth-modal.html',
  styleUrl: './auth-modal.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AuthModal {
  protected readonly auth = inject(AuthService);

  protected readonly aberto = signal(false);
  protected readonly modoCadastro = signal(false);

  protected readonly nome = signal('');
  protected readonly email = signal('');
  protected readonly senha = signal('');
  protected readonly carregando = signal(false);
  protected readonly erro = signal<string | null>(null);

  protected readonly rotuloBotao = computed(() => (this.modoCadastro() ? 'Criar conta' : 'Entrar'));

  constructor() {
    this.auth.pedidoAutenticacao$.pipe(takeUntilDestroyed()).subscribe(() => {
      if (!this.aberto()) {
        this.modoCadastro.set(false);
        this.aberto.set(true);
      }
    });
  }

  protected selecionarModo(modo: ModoAuth): void {
    this.erro.set(null);
    this.modoCadastro.set(modo === 'cadastro');
  }

  protected enviar(): void {
    this.erro.set(null);
    this.carregando.set(true);

    const acao = this.modoCadastro()
      ? this.auth.cadastrar(this.email(), this.nome(), this.senha())
      : this.auth.login(this.email(), this.senha());

    acao.subscribe({
      next: () => {
        this.carregando.set(false);
        this.fechar();
      },
      error: (erro: HttpErrorResponse) => {
        this.carregando.set(false);
        this.erro.set(
          erro.error?.message ?? 'Não foi possível completar a operação. Tente novamente.',
        );
      },
    });
  }

  protected fechar(): void {
    this.nome.set('');
    this.email.set('');
    this.senha.set('');
    this.erro.set(null);
    this.aberto.set(false);
  }
}
