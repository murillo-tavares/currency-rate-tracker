import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { CotacaoService } from './cotacao';

describe('CotacaoService', () => {
  let service: CotacaoService;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiUrl}/cotacoes`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(CotacaoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('lista cotações atuais sem filtro de código', () => {
    service.listarAtuais().subscribe();

    const req = httpMock.expectOne((r) => r.url === baseUrl);
    expect(req.request.method).toBe('GET');
    expect(req.request.params.keys().length).toBe(0);
    req.flush([]);
  });

  it('lista cotações atuais filtrando por código', () => {
    service.listarAtuais(['USD', 'EUR']).subscribe();

    const req = httpMock.expectOne((r) => r.url === baseUrl);
    expect(req.request.params.getAll('codigosMoeda')).toEqual(['USD', 'EUR']);
    req.flush([]);
  });

  it('busca dashboard com filtro completo', () => {
    service
      .buscarDashboard({
        codigosMoeda: ['USD'],
        inicio: '2026-08-14T00:00:00',
        fim: '2026-08-15T00:00:00',
      })
      .subscribe();

    const req = httpMock.expectOne((r) => r.url === `${baseUrl}/dashboard`);
    expect(req.request.params.getAll('codigosMoeda')).toEqual(['USD']);
    expect(req.request.params.get('inicio')).toBe('2026-08-14T00:00:00');
    expect(req.request.params.get('fim')).toBe('2026-08-15T00:00:00');
    req.flush({ graficos: [] });
  });

  it('busca dashboard sem filtro', () => {
    service.buscarDashboard({}).subscribe();

    const req = httpMock.expectOne((r) => r.url === `${baseUrl}/dashboard`);
    expect(req.request.params.keys().length).toBe(0);
    req.flush({ graficos: [] });
  });

  it('busca dashboard por período convertendo a seleção em filtro', () => {
    service.buscarDashboardPorPeriodo({ timeframe: '24h' }).subscribe();

    const req = httpMock.expectOne((r) => r.url === `${baseUrl}/dashboard`);
    expect(req.request.params.get('inicio')).toBeTruthy();
    req.flush({ graficos: [] });
  });
});
