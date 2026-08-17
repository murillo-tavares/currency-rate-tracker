import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { environment } from '../../../environments/environment';
import { AuthService } from '../../core/services/auth';
import { CotacoesDashboard, INTERVALO_ATUALIZACAO_MS } from './cotacoes-dashboard';

const CHAVE_SESSAO = 'currency-rate-tracker:sessao';

describe('CotacoesDashboard', () => {
  let fixture: ComponentFixture<CotacoesDashboard>;
  let httpMock: HttpTestingController;
  const baseUrl = `${environment.apiUrl}/cotacoes`;
  const baseUrlFavoritos = `${environment.apiUrl}/favoritos`;

  function elemento(): HTMLElement {
    return fixture.nativeElement as HTMLElement;
  }

  function textoDoCard(codigoMoeda: string): string {
    const cards = Array.from(elemento().querySelectorAll<HTMLElement>('app-currency-card'));
    return cards.find((el) => el.textContent?.includes(codigoMoeda))?.textContent ?? '';
  }

  function botaoFavoritoDoCard(codigoMoeda: string): HTMLButtonElement {
    const cards = Array.from(elemento().querySelectorAll<HTMLElement>('app-currency-card'));
    const card = cards.find((el) => el.textContent?.includes(codigoMoeda))!;
    return card.querySelector<HTMLButtonElement>('.favorito')!;
  }

  async function flushCotacoes(): Promise<void> {
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

  describe('deslogado', () => {
    beforeEach(async () => {
      localStorage.clear();
      vi.useFakeTimers();

      await TestBed.configureTestingModule({
        imports: [CotacoesDashboard],
        providers: [provideHttpClient(), provideHttpClientTesting()],
      }).compileComponents();

      httpMock = TestBed.inject(HttpTestingController);
      fixture = TestBed.createComponent(CotacoesDashboard);
      fixture.detectChanges();
      await vi.advanceTimersByTimeAsync(0);
    });

    afterEach(() => {
      httpMock.verify();
      vi.useRealTimers();
    });

    it('carrega as cotações e monta um card por moeda, sem chamar /favoritos', async () => {
      await flushCotacoes();

      expect(elemento().textContent).not.toContain('Carregando cotações');
      expect(elemento().querySelectorAll('app-currency-card').length).toBe(2);
      httpMock.expectNone(baseUrlFavoritos);
    });

    it('junta o histórico do dashboard com a cotação certa por código', async () => {
      await flushCotacoes();

      const textoUsd = textoDoCard('USD');
      expect(textoUsd).toContain('R$ 5,40');
      expect(textoUsd).toContain('R$ 5,30');

      const textoEur = textoDoCard('EUR');
      expect(textoEur).toContain('R$ 5,90');
    });

    it('não mostra a seção de favoritos antes de o usuário logar', async () => {
      await flushCotacoes();

      expect(elemento().textContent).not.toContain('Favoritas');
    });

    it('clicar em favoritar deslogado solicita autenticação, sem chamar o backend', async () => {
      await flushCotacoes();
      const auth = TestBed.inject(AuthService);
      let notificacoes = 0;
      auth.pedidoAutenticacao$.subscribe(() => notificacoes++);

      botaoFavoritoDoCard('USD').click();
      fixture.detectChanges();

      expect(notificacoes).toBe(1);
      httpMock.expectNone(`${baseUrlFavoritos}/USD`);
    });

    it('mantém os cards visíveis e mostra erro quando uma atualização automática falha', async () => {
      await flushCotacoes();
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
      await flushCotacoes();

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

  describe('logado', () => {
    beforeEach(async () => {
      localStorage.clear();
      localStorage.setItem(
        CHAVE_SESSAO,
        JSON.stringify({ token: 'jwt-abc', email: 'ana@email.com', nome: 'Ana' }),
      );
      vi.useFakeTimers();

      await TestBed.configureTestingModule({
        imports: [CotacoesDashboard],
        providers: [provideHttpClient(), provideHttpClientTesting()],
      }).compileComponents();

      httpMock = TestBed.inject(HttpTestingController);
      fixture = TestBed.createComponent(CotacoesDashboard);
      fixture.detectChanges();
      await vi.advanceTimersByTimeAsync(0);
    });

    afterEach(() => {
      httpMock.verify();
      vi.useRealTimers();
    });

    async function flushFavoritosIniciais(codigos: string[] = []): Promise<void> {
      httpMock
        .expectOne((r) => r.url === baseUrlFavoritos)
        .flush(codigos.map((codigoMoeda) => ({ codigoMoeda })));
      await vi.advanceTimersByTimeAsync(0);
      fixture.detectChanges();
    }

    it('carrega os favoritos do backend ao iniciar logado', async () => {
      await flushFavoritosIniciais(['USD']);
      await flushCotacoes();

      expect(elemento().textContent).toContain('Favoritas');
      expect(textoDoCard('USD')).toContain('★');
    });

    it('favorita uma moeda e atualiza a UI ao ter sucesso', async () => {
      await flushFavoritosIniciais([]);
      await flushCotacoes();

      botaoFavoritoDoCard('USD').click();
      fixture.detectChanges();

      const req = httpMock.expectOne(`${baseUrlFavoritos}/USD`);
      expect(req.request.method).toBe('POST');
      req.flush(null, { status: 204, statusText: 'No Content' });
      fixture.detectChanges();

      expect(elemento().textContent).toContain('Favoritas');
    });

    it('desfavorita uma moeda já favoritada', async () => {
      await flushFavoritosIniciais(['USD']);
      await flushCotacoes();

      botaoFavoritoDoCard('USD').click();
      fixture.detectChanges();

      const req = httpMock.expectOne(`${baseUrlFavoritos}/USD`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null, { status: 204, statusText: 'No Content' });
      fixture.detectChanges();

      expect(elemento().textContent).not.toContain('Favoritas');
    });

    it('sessão expirada ao favoritar (401): desloga e solicita autenticação de novo', async () => {
      await flushFavoritosIniciais([]);
      await flushCotacoes();
      const auth = TestBed.inject(AuthService);
      let notificacoes = 0;
      auth.pedidoAutenticacao$.subscribe(() => notificacoes++);

      botaoFavoritoDoCard('USD').click();
      fixture.detectChanges();

      httpMock
        .expectOne(`${baseUrlFavoritos}/USD`)
        .flush({ message: 'Autenticação necessária' }, { status: 401, statusText: 'Unauthorized' });
      await vi.advanceTimersByTimeAsync(0);
      fixture.detectChanges();

      expect(auth.usuarioLogado()).toBeNull();
      expect(notificacoes).toBe(1);
    });

    it('token inválido/expirado ao carregar os favoritos (401): desloga sem travar o resto do dashboard', async () => {
      const auth = TestBed.inject(AuthService);

      httpMock
        .expectOne((r) => r.url === baseUrlFavoritos)
        .flush({ message: 'Autenticação necessária' }, { status: 401, statusText: 'Unauthorized' });
      await vi.advanceTimersByTimeAsync(0);

      await flushCotacoes();

      expect(auth.usuarioLogado()).toBeNull();
      expect(elemento().querySelectorAll('app-currency-card').length).toBe(2);
    });

    it('erro que não é 401 ao carregar os favoritos (ex: 404): não desloga, só fica sem favoritos', async () => {
      const auth = TestBed.inject(AuthService);

      httpMock
        .expectOne((r) => r.url === baseUrlFavoritos)
        .flush({ message: 'Not Found' }, { status: 404, statusText: 'Not Found' });
      await vi.advanceTimersByTimeAsync(0);

      await flushCotacoes();

      expect(auth.usuarioLogado()).not.toBeNull();
      expect(elemento().textContent).not.toContain('Favoritas');
      expect(elemento().querySelectorAll('app-currency-card').length).toBe(2);
    });
  });
});
