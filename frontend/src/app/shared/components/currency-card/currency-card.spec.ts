import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Cotacao } from '../../../core/models/cotacao.model';
import { CurrencyCard } from './currency-card';

function criarCotacao(overrides: Partial<Cotacao> = {}): Cotacao {
  return {
    codigoMoeda: 'USD',
    nome: 'Dólar Americano',
    valor: 5.42,
    variacaoPercentual: 1.23,
    dataCotacao: '2026-08-15T12:00:00',
    ...overrides,
  };
}

describe('CurrencyCard', () => {
  let fixture: ComponentFixture<CurrencyCard>;
  let component: CurrencyCard;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurrencyCard],
    }).compileComponents();

    fixture = TestBed.createComponent(CurrencyCard);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.componentRef.setInput('cotacao', criarCotacao());
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('exibe código, nome e valor formatado', () => {
    fixture.componentRef.setInput(
      'cotacao',
      criarCotacao({ codigoMoeda: 'EUR', nome: 'Euro', valor: 5.86 }),
    );
    fixture.detectChanges();

    const texto = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(texto).toContain('EUR');
    expect(texto).toContain('Euro');
    expect(texto).toContain('R$ 5,86');
  });

  it('mostra seta e sinal de acordo com a variação', () => {
    fixture.componentRef.setInput('cotacao', criarCotacao({ variacaoPercentual: -0.38 }));
    fixture.detectChanges();

    const texto = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(texto).toContain('▼ -0.38%');
  });

  it('calcula máximo e mínimo a partir do histórico e do valor atual', () => {
    fixture.componentRef.setInput('cotacao', criarCotacao({ valor: 10 }));
    fixture.componentRef.setInput('pontos', [
      { valor: 8, variacaoPercentual: 0, dataCotacao: '2026-08-15T10:00:00' },
      { valor: 12, variacaoPercentual: 0, dataCotacao: '2026-08-15T11:00:00' },
    ]);
    fixture.detectChanges();

    const texto = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(texto).toContain('R$ 12,00');
    expect(texto).toContain('R$ 8,00');
  });

  it('emite o código da moeda ao clicar no botão de favorito', () => {
    fixture.componentRef.setInput('cotacao', criarCotacao({ codigoMoeda: 'BTC' }));
    fixture.detectChanges();

    let emitido: string | undefined;
    component.favoritaAlternada.subscribe((codigo) => (emitido = codigo));

    (fixture.nativeElement as HTMLElement).querySelector<HTMLButtonElement>('.favorito')!.click();

    expect(emitido).toBe('BTC');
  });
});
