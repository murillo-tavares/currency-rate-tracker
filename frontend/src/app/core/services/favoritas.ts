import { Service } from '@angular/core';
import { Observable, of } from 'rxjs';

import { carregarFavoritas, salvarFavoritas } from '../../shared/utils/favoritas-storage.util';

@Service()
export class FavoritasService {
  listar(): Observable<string[]> {
    return of(carregarFavoritas());
  }

  salvar(codigosMoeda: string[]): Observable<void> {
    salvarFavoritas(codigosMoeda);
    return of(undefined);
  }

  alternar(codigoMoeda: string): Observable<string[]> {
    const atuais = carregarFavoritas();
    const novos = atuais.includes(codigoMoeda)
      ? atuais.filter((codigo) => codigo !== codigoMoeda)
      : [...atuais, codigoMoeda];

    salvarFavoritas(novos);
    return of(novos);
  }
}
