import { ChangeDetectionStrategy, Component, computed, input, output } from '@angular/core';

import { Cotacao } from '../../../core/models/cotacao.model';
import { PontoHistorico } from '../../../core/models/dashboard.model';
import { FlashDirective } from '../../directives/flash';
import { MoedaBrlPipe } from '../../pipes/moeda-brl-pipe';
import { lerVariavelCss } from '../../utils/css-var.util';
import { Sparkline } from '../sparkline/sparkline';

@Component({
  selector: 'app-currency-card',
  imports: [Sparkline, MoedaBrlPipe, FlashDirective],
  templateUrl: './currency-card.html',
  styleUrl: './currency-card.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurrencyCard {
  readonly cotacao = input.required<Cotacao>();
  readonly pontos = input<PontoHistorico[]>([]);
  readonly favorita = input<boolean>(false);

  readonly favoritaAlternada = output<string>();

  protected readonly valores = computed(() => this.pontos().map((p) => p.valor));

  protected readonly maximo = computed(() =>
    this.valores().length > 0 ? Math.max(...this.valores()) : this.cotacao().valor,
  );
  protected readonly minimo = computed(() =>
    this.valores().length > 0 ? Math.min(...this.valores()) : this.cotacao().valor,
  );

  protected readonly emAlta = computed(() => this.cotacao().variacaoPercentual >= 0);
  protected readonly corTendencia = computed(() =>
    lerVariavelCss(this.emAlta() ? '--color-up' : '--color-down'),
  );
  protected readonly variacaoFormatada = computed(() =>
    this.cotacao().variacaoPercentual.toFixed(2),
  );

  protected onToggleFavorita(): void {
    this.favoritaAlternada.emit(this.cotacao().codigoMoeda);
  }
}
