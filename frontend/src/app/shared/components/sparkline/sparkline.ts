import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import {
  Chart,
  ChartConfiguration,
  LinearScale,
  LineController,
  LineElement,
  PointElement,
} from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

Chart.register(LineController, LineElement, PointElement, LinearScale);

@Component({
  selector: 'app-sparkline',
  imports: [BaseChartDirective],
  templateUrl: './sparkline.html',
  styleUrl: './sparkline.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Sparkline {
  readonly valores = input.required<number[]>();
  readonly cor = input<string>('currentColor');

  // Chart.js precisa de pelo menos 2 pontos pra desenhar uma linha
  // com só 1 valor, ele imprime um ponto isolado em vez de uma linha.
  protected readonly pontos = computed(() => {
    const valores = this.valores();
    return valores.length === 1 ? [valores[0], valores[0]] : valores;
  });

  protected readonly dados = computed<ChartConfiguration<'line'>['data']>(() => ({
    labels: this.pontos().map((_, indice) => indice),
    datasets: [
      {
        data: this.pontos(),
        borderColor: this.cor(),
        borderWidth: 1.6,
        borderCapStyle: 'round',
        borderJoinStyle: 'round',
        pointRadius: 0,
        tension: 0,
        fill: false,
      },
    ],
  }));

  protected readonly opcoes = computed<ChartConfiguration<'line'>['options']>(() => ({
    responsive: true,
    maintainAspectRatio: false,
    animation: false,
    scales: {
      x: { type: 'linear', display: false, min: 0, max: this.pontos().length - 1 },
      y: { display: false },
    },
    plugins: {
      legend: { display: false },
      tooltip: { enabled: false },
    },
  }));
}
