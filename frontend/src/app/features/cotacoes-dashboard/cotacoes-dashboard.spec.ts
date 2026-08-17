import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { CotacoesDashboard, INTERVALO_ATUALIZACAO_MS } from './cotacoes-dashboard';

const CHAVE_FAVORITAS = 'currency-rate-tracker:favoritas';

describe('CotacoesDashboard', () => {
  let fixture: ComponentFixture<CotacoesDashboard>;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiUrl}/cotacoes`;

  beforeEach(async () => {
    localStorage.clear();
    vi.useFakeTimers();

    await TestBed.configureTestingModule({
      imports: [CotacoesDashboard],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(CotacoesDashboard);
  });

  afterEach(() => {
    httpMock.verify();
    vi.useRealTimers();
  });

  function elemento(): HTMLElement {
    return fixture.nativeElement as HTMLElement;
  }

  function textoDoCard(codigoMoeda: string): string {
    const cards = Array.from(elemento().querySelectorAll<HTMLElement>('app-currency-card'));
    return cards.find((el) => el.textContent?.includes(codigoMoeda))?.textContent ?? '';
  }

  async function flushBuscaInicial(): Promise<void> {
    fixture.detectChanges();
    await vi.advanceTimersByTimeAsync(0);

    httpMock
      .expectOne((r) => r.url === baseUrl)
      .flush([
        {
          codigoMoeda: 'USD',
          nome: 'Dólar Americano',
          valor: 5.4,
          variacaoPercentual: 1.2,
          dataCotacao: '2026-08-15T12:00:00',
        },
        {
          codigoMoeda: 'EUR',
          nome: 'Euro',
          valor: 5.9,
          variacaoPercentual: -0.5,
          dataCotacao: '2026-08-15T12:00:00',
        },
      ]);

    httpMock
      .expectOne((r) => r.url === `${baseUrl}/dashboard`)
      .flush({
        graficos: [
          {
            codigoMoeda: 'USD',
            pontos: [{ valor: 5.3, variacaoPercentual: 1, dataCotacao: '2026-08-15T11:00:00' }],
          },
        ],
      });

    await vi.advanceTimersByTimeAsync(0);
    fixture.detectChanges();
  }

  it('carrega as cotações e monta um card por moeda', async () => {
    await flushBuscaInicial();

    expect(elemento().textContent).not.toContain('Carregando cotações');
    expect(elemento().querySelectorAll('app-currency-card').length).toBe(2);
  });

  it('junta o histórico do dashboard com a cotação certa por código', async () => {
    await flushBuscaInicial();

    const textoUsd = textoDoCard('USD');
    expect(textoUsd).toContain('R$ 5,40');
    expect(textoUsd).toContain('R$ 5,30');

    const textoEur = textoDoCard('EUR');
    expect(textoEur).toContain('R$ 5,90');
  });

  it('não mostra a seção de favoritos antes de o usuário escolher algum', async () => {
    await flushBuscaInicial();

    expect(elemento().textContent).not.toContain('Favoritas');
  });

  it('alterna favorito ao clicar na estrela e persiste no localStorage', async () => {
    await flushBuscaInicial();

    const cards = Array.from(elemento().querySelectorAll<HTMLElement>('app-currency-card'));
    const cardUsd = cards.find((el) => el.textContent?.includes('USD'))!;
    cardUsd.querySelector<HTMLButtonElement>('.favorito')!.click();
    fixture.detectChanges();
    await vi.advanceTimersByTimeAsync(0);

    expect(elemento().textContent).toContain('Favoritas');
    expect(JSON.parse(localStorage.getItem(CHAVE_FAVORITAS)!)).toEqual(['USD']);
  });

  it('mantém os cards visíveis e mostra erro quando uma atualização automática falha', async () => {
    await flushBuscaInicial();
    expect(elemento().querySelectorAll('app-currency-card').length).toBe(2);

    await vi.advanceTimersByTimeAsync(INTERVALO_ATUALIZACAO_MS);
    const reqCotacoes = httpMock.expectOne((r) => r.url === baseUrl);
    const reqDashboard = httpMock.expectOne((r) => r.url === `${baseUrl}/dashboard`);
    reqCotacoes.error(new ProgressEvent('erro de rede'));
    await vi.advanceTimersByTimeAsync(0);
    // forkJoin cancela a outra request assim que uma das duas falha.
    expect(reqDashboard.cancelled).toBe(true);
    fixture.detectChanges();

    expect(elemento().querySelectorAll('app-currency-card').length).toBe(2);
    expect(textoDoCard('USD')).toContain('R$ 5,40');
    expect(elemento().textContent).toContain('Não foi possível atualizar as cotações agora.');
  });

  it('refaz a busca com o novo filtro quando o período selecionado muda', async () => {
    await flushBuscaInicial();

    const botoes = Array.from(
      elemento().querySelectorAll<HTMLButtonElement>('app-period-selector .botao'),
    );
    botoes.find((b) => b.textContent?.trim() === '7d')!.click();
    fixture.detectChanges();
    await vi.advanceTimersByTimeAsync(0);

    httpMock.expectOne((r) => r.url === baseUrl).flush([]);
    const reqDashboard = httpMock.expectOne((r) => r.url === `${baseUrl}/dashboard`);
    expect(reqDashboard.request.params.get('inicio')).toBeTruthy();
    reqDashboard.flush({ graficos: [] });

    await vi.advanceTimersByTimeAsync(0);
  });
});
