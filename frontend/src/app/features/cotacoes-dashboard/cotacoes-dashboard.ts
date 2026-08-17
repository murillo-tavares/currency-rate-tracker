import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { catchError, combineLatest, forkJoin, of, switchMap, tap, timer } from 'rxjs';

import { Cotacao } from '../../core/models/cotacao.model';
import { DashboardCotacoes } from '../../core/models/dashboard.model';
import { SelecaoPeriodo } from '../../core/models/timeframe.model';
import { CotacaoService } from '../../core/services/cotacao';
import { FavoritasService } from '../../core/services/favoritas';
import { CurrencyCard } from '../../shared/components/currency-card/currency-card';
import { PeriodSelector } from '../../shared/components/period-selector/period-selector';
import { CardVisivel, montarCards } from '../../shared/utils/card-visivel.util';
import { formatarHora } from '../../shared/utils/data-hora.util';

export const INTERVALO_ATUALIZACAO_MS = 20_000;

interface Resultado {
  cotacoes: Cotacao[];
  dashboard: DashboardCotacoes;
}

@Component({
  selector: 'app-cotacoes-dashboard',
  imports: [CurrencyCard, PeriodSelector],
  templateUrl: './cotacoes-dashboard.html',
  styleUrl: './cotacoes-dashboard.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CotacoesDashboard {
  private readonly cotacaoService = inject(CotacaoService);
  private readonly favoritasService = inject(FavoritasService);

  protected readonly selecaoPeriodo = signal<SelecaoPeriodo>({ timeframe: '24h' });
  protected readonly favoritas = signal<Set<string>>(new Set());
  protected readonly ultimaAtualizacao = signal<Date | null>(null);
  protected readonly horaAtualizacao = computed(() => {
    const data = this.ultimaAtualizacao();
    return data ? formatarHora(data) : '--:--:--';
  });

  protected readonly carregando = signal(true);
  protected readonly erro = signal<string | null>(null);
  protected readonly ultimoResultadoValido = signal<Resultado>({
    cotacoes: [],
    dashboard: { graficos: [] },
  });

  protected readonly cardsFavoritos = computed(() => this.cards().filter((card) => card.favorita));
  protected readonly cards = computed<CardVisivel[]>(() =>
    montarCards(
      this.ultimoResultadoValido().cotacoes,
      this.ultimoResultadoValido().dashboard.graficos,
      this.favoritas(),
    ),
  );

  constructor() {
    this.favoritasService.listar().subscribe((codigos) => {
      const favoritas = new Set(codigos);
      this.favoritas.set(favoritas);
    });

    const selecaoPeriodo$ = toObservable(this.selecaoPeriodo);

    combineLatest([timer(0, INTERVALO_ATUALIZACAO_MS), selecaoPeriodo$])
      .pipe(
        switchMap(([, selecao]) =>
          forkJoin({
            cotacoes: this.cotacaoService.listarAtuais(),
            dashboard: this.cotacaoService.buscarDashboardPorPeriodo(selecao),
          }).pipe(
            tap((sucesso) => {
              this.ultimoResultadoValido.set(sucesso);
              this.erro.set(null);
            }),
            catchError(() => {
              this.erro.set('Não foi possível atualizar as cotações agora.');
              return of(null);
            }),
          ),
        ),
        tap(() => {
          this.carregando.set(false);
          this.ultimaAtualizacao.set(new Date());
        }),
        takeUntilDestroyed(),
      )
      .subscribe();
  }

  protected onSelecaoPeriodoAlterada(selecao: SelecaoPeriodo): void {
    this.selecaoPeriodo.set(selecao);
  }

  protected alternarFavorita(codigoMoeda: string): void {
    this.favoritasService
      .alternar(codigoMoeda)
      .subscribe((codigos) => this.favoritas.set(new Set(codigos)));
  }
}
