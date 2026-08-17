import { TestBed } from '@angular/core/testing';

import { FavoritasService } from './favoritas';

describe('FavoritasService', () => {
  let service: FavoritasService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({});
    service = TestBed.inject(FavoritasService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('lista vazio quando nada foi salvo ainda', () => {
    let resultado: string[] | undefined;
    service.listar().subscribe((codigos) => (resultado = codigos));

    expect(resultado).toEqual([]);
  });

  it('salva e depois lista os códigos persistidos', () => {
    service.salvar(['USD', 'BTC']).subscribe();

    let resultado: string[] | undefined;
    service.listar().subscribe((codigos) => (resultado = codigos));

    expect(resultado).toEqual(['USD', 'BTC']);
  });

  it('adiciona um código que ainda não é favorito', () => {
    service.salvar(['USD']).subscribe();

    let resultado: string[] | undefined;
    service.alternar('BTC').subscribe((codigos) => (resultado = codigos));

    expect(resultado).toEqual(['USD', 'BTC']);
  });

  it('remove um código que já é favorito', () => {
    service.salvar(['USD', 'BTC']).subscribe();

    let resultado: string[] | undefined;
    service.alternar('USD').subscribe((codigos) => (resultado = codigos));

    expect(resultado).toEqual(['BTC']);
  });

  it('persiste o resultado do alternar', () => {
    service.alternar('EUR').subscribe();

    let resultado: string[] | undefined;
    service.listar().subscribe((codigos) => (resultado = codigos));

    expect(resultado).toEqual(['EUR']);
  });
});
