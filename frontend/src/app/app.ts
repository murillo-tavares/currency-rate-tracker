import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { AppHeader } from './shared/components/app-header/app-header';
import { AuthModal } from './shared/components/auth-modal/auth-modal';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AppHeader, AuthModal],
  templateUrl: './app.html',
  styleUrl: './app.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class App {}
