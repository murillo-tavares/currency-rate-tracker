import { HttpClient, HttpParams } from '@angular/common/http';
import { Service, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { filtroParaSelecaoPeriodo } from '../../shared/utils/filtro-periodo.util';
import { Cotacao } from '../models/cotacao.model';
import { DashboardCotacoes } from '../models/dashboard.model';
import { SelecaoPeriodo } from '../models/timeframe.model';

export interface FiltroDashboard {
  codigosMoeda?: string[];
  inicio?: string;
  fim?: string;
}

@Service()
export class CotacaoService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/cotacoes`;

  listarAtuais(codigosMoeda?: string[]): Observable<Cotacao[]> {
    let params = new HttpParams();
    for (const codigo of codigosMoeda ?? []) {
      params = params.append('codigosMoeda', codigo);
    }
    return this.http.get<Cotacao[]>(this.baseUrl, { params });
  }

  buscarDashboard(filtro: FiltroDashboard): Observable<DashboardCotacoes> {
    let params = new HttpParams();
    for (const codigo of filtro.codigosMoeda ?? []) {
      params = params.append('codigosMoeda', codigo);
    }
    if (filtro.inicio) {
      params = params.append('inicio', filtro.inicio);
    }
    if (filtro.fim) {
      params = params.append('fim', filtro.fim);
    }
    return this.http.get<DashboardCotacoes>(`${this.baseUrl}/dashboard`, { params });
  }

  buscarDashboardPorPeriodo(selecao: SelecaoPeriodo): Observable<DashboardCotacoes> {
    return this.buscarDashboard(filtroParaSelecaoPeriodo(selecao));
  }
}
