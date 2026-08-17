import { Pipe, PipeTransform } from '@angular/core';

import { decimaisPara } from '../utils/moeda-decimais.util';

@Pipe({
  name: 'moedaBrl',
})
export class MoedaBrlPipe implements PipeTransform {
  transform(valor: number, codigoMoeda: string): string {
    const decimais = decimaisPara(codigoMoeda);
    return (
      'R$ ' +
      valor.toLocaleString('pt-BR', {
        minimumFractionDigits: decimais,
        maximumFractionDigits: decimais,
      })
    );
  }
}
