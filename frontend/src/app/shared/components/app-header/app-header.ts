import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';

import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './app-header.html',
  styleUrl: './app-header.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppHeader {
  protected readonly auth = inject(AuthService);

  protected readonly inicial = computed(() => {
    const usuario = this.auth.usuarioLogado();
    return (usuario?.nome ?? usuario?.email ?? 'U').trim().charAt(0).toUpperCase();
  });
}
