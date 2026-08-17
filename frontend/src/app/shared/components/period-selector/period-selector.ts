import { ChangeDetectionStrategy, Component, input, output } from '@angular/core';

import { SelecaoPeriodo, Timeframe } from '../../../core/models/timeframe.model';

@Component({
  selector: 'app-period-selector',
  imports: [],
  templateUrl: './period-selector.html',
  styleUrl: './period-selector.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PeriodSelector {
  readonly timeframe = input.required<Timeframe>();
  readonly inicioPersonalizado = input<string>('');
  readonly fimPersonalizado = input<string>('');

  readonly selecaoAlterada = output<SelecaoPeriodo>();

  protected selecionar(timeframe: Timeframe): void {
    this.selecaoAlterada.emit(timeframe === 'custom' ? this.selecaoPersonalizada() : { timeframe });
  }

  protected onInicioAlterado(valor: string): void {
    this.selecaoAlterada.emit({ ...this.selecaoPersonalizada(), inicio: valor });
  }

  protected onFimAlterado(valor: string): void {
    this.selecaoAlterada.emit({ ...this.selecaoPersonalizada(), fim: valor });
  }

  private selecaoPersonalizada(): SelecaoPeriodo {
    return {
      timeframe: 'custom',
      inicio: this.inicioPersonalizado(),
      fim: this.fimPersonalizado(),
    };
  }
}
