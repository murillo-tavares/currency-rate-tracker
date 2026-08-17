import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import {
  Chart,
  ChartConfiguration,
  LinearScale,
  LineController,
  LineElement,
  PointElement,
  Tooltip,
  TooltipItem,
} from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

import { PontoHistorico } from '../../../core/models/dashboard.model';
import { MoedaBrlPipe } from '../../pipes/moeda-brl-pipe';
import { lerVariavelCss } from '../../utils/css-var.util';
import { formatarDataHora } from '../../utils/data-hora.util';

Chart.register(LineController, LineElement, PointElement, LinearScale, Tooltip);

@Component({
  selector: 'app-sparkline',
  imports: [BaseChartDirective],
  templateUrl: './sparkline.html',
  styleUrl: './sparkline.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Sparkline {
  readonly pontos = input.required<PontoHistorico[]>();
  readonly codigoMoeda = input.required<string>();
  readonly cor = input<string>('currentColor');

  private readonly moedaBrlPipe = new MoedaBrlPipe();

  protected readonly pontosAjustados = computed(() => {
    const pontos = this.pontos();
    return pontos.length === 1 ? [pontos[0], pontos[0]] : pontos;
  });

  protected readonly dados = computed<ChartConfiguration<'line'>['data']>(() => ({
    labels: this.pontosAjustados().map((_, indice) => indice),
    datasets: [
      {
        data: this.pontosAjustados().map((p) => p.valor),
        borderColor: this.cor(),
        borderWidth: 1.6,
        borderCapStyle: 'round',
        borderJoinStyle: 'round',
        pointRadius: 0,
        pointHoverRadius: 3,
        pointHoverBackgroundColor: this.cor(),
        pointHoverBorderWidth: 0,
        tension: 0,
        fill: false,
      },
    ],
  }));

  protected readonly opcoes = computed<ChartConfiguration<'line'>['options']>(() => ({
    responsive: true,
    maintainAspectRatio: false,
    animation: false,
    interaction: { mode: 'index', intersect: false },
    scales: {
      x: {
        type: 'linear',
        display: false,
        min: 0,
        max: Math.max(this.pontosAjustados().length - 1, 0),
      },
      y: { display: false },
    },
    plugins: {
      legend: { display: false },
      tooltip: {
        displayColors: false,
        caretSize: 0,
        cornerRadius: 6,
        padding: { x: 8, y: 4 },
        backgroundColor: lerVariavelCss('--color-surface-alt'),
        borderColor: lerVariavelCss('--color-border-strong'),
        borderWidth: 1,
        titleColor: lerVariavelCss('--color-text'),
        titleFont: { family: lerVariavelCss('--font-mono'), size: 11, weight: 'bold' },
        bodyColor: lerVariavelCss('--color-text-secondary'),
        bodyFont: { size: 10 },
        callbacks: {
          title: ([item]: TooltipItem<'line'>[]) =>
            this.moedaBrlPipe.transform(
              this.pontosAjustados()[item.dataIndex].valor,
              this.codigoMoeda(),
            ),
          label: (item: TooltipItem<'line'>) =>
            formatarDataHora(this.pontosAjustados()[item.dataIndex].dataCotacao),
        },
      },
    },
  }));
}
