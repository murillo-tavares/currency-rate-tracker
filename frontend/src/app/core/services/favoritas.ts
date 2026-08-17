import { HttpClient } from '@angular/common/http';
import { Service, inject } from '@angular/core';
import { Observable, map } from 'rxjs';

import { environment } from '../../../environments/environment';
import { FavoritoResponse } from '../models/favorito.model';

@Service()
export class FavoritasService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/favoritos`;

  listar(): Observable<string[]> {
    return this.http
      .get<FavoritoResponse[]>(this.baseUrl)
      .pipe(map((favoritos) => favoritos.map((favorito) => favorito.codigoMoeda)));
  }

  adicionar(codigoMoeda: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${codigoMoeda}`, null);
  }

  remover(codigoMoeda: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${codigoMoeda}`);
  }
}
