import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { FavoritasService } from './favoritas';

describe('FavoritasService', () => {
  let service: FavoritasService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiUrl}/favoritos`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(FavoritasService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('lista os códigos das moedas favoritas', () => {
    let resultado: string[] | undefined;
    service.listar().subscribe((codigos) => (resultado = codigos));

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush([{ codigoMoeda: 'USD' }, { codigoMoeda: 'BTC' }]);

    expect(resultado).toEqual(['USD', 'BTC']);
  });

  it('adiciona uma moeda aos favoritos', () => {
    let concluiu = false;
    service.adicionar('USD').subscribe(() => (concluiu = true));

    const req = httpMock.expectOne(`${baseUrl}/USD`);
    expect(req.request.method).toBe('POST');
    req.flush(null, { status: 204, statusText: 'No Content' });

    expect(concluiu).toBe(true);
  });

  it('remove uma moeda dos favoritos', () => {
    let concluiu = false;
    service.remover('USD').subscribe(() => (concluiu = true));

    const req = httpMock.expectOne(`${baseUrl}/USD`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null, { status: 204, statusText: 'No Content' });

    expect(concluiu).toBe(true);
  });
});
