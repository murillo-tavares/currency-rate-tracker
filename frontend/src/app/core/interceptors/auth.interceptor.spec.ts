import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;

  afterEach(() => {
    httpMock.verify();
  });

  function configurar(): void {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
      ],
    });
    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  }

  it('não anexa Authorization quando não há sessão', () => {
    localStorage.clear();
    configurar();

    http.get('/api/v1/moedas').subscribe();

    const req = httpMock.expectOne('/api/v1/moedas');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush([]);
  });

  it('anexa Authorization: Bearer <token> quando há sessão', () => {
    localStorage.setItem(
      'currency-rate-tracker:sessao',
      JSON.stringify({ token: 'jwt-abc', email: 'ana@email.com' }),
    );
    configurar();

    http.get('/api/v1/cotacoes').subscribe();

    const req = httpMock.expectOne('/api/v1/cotacoes');
    expect(req.request.headers.get('Authorization')).toBe('Bearer jwt-abc');
    req.flush([]);
  });
});
