import { DestroyRef, Directive, effect, inject, input, signal } from '@angular/core';

const DURACAO_FLASH_MS = 900;

export type Flash = 'up' | 'down' | null;

@Directive({
  selector: '[appFlash]',
  host: {
    '[class.flash-up]': 'flash() === "up"',
    '[class.flash-down]': 'flash() === "down"',
  },
})
export class FlashDirective {
  readonly appFlash = input.required<number>();

  protected readonly flash = signal<Flash>(null);

  private valorAnterior: number | null = null;
  private timeoutId?: ReturnType<typeof setTimeout>;

  constructor() {
    effect(() => {
      const valorAtual = this.appFlash();

      if (this.valorAnterior !== null && valorAtual !== this.valorAnterior) {
        this.flash.set(valorAtual > this.valorAnterior ? 'up' : 'down');
        clearTimeout(this.timeoutId);
        this.timeoutId = setTimeout(() => this.flash.set(null), DURACAO_FLASH_MS);
      }

      this.valorAnterior = valorAtual;
    });

    inject(DestroyRef).onDestroy(() => clearTimeout(this.timeoutId));
  }
}
