import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { TooltipItem } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

import { PontoHistorico } from '../../../core/models/dashboard.model';
import { Sparkline } from './sparkline';

function criarPonto(overrides: Partial<PontoHistorico> = {}): PontoHistorico {
  return {
    valor: 10,
    variacaoPercentual: 0,
    dataCotacao: '2026-08-15T10:00:00',
    ...overrides,
  };
}

describe('Sparkline', () => {
  let component: Sparkline;
  let fixture: ComponentFixture<Sparkline>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Sparkline],
    }).compileComponents();

    fixture = TestBed.createComponent(Sparkline);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('pontos', [
      criarPonto({ valor: 10, dataCotacao: '2026-08-15T10:00:00' }),
      criarPonto({ valor: 12, dataCotacao: '2026-08-15T11:00:00' }),
      criarPonto({ valor: 9, dataCotacao: '2026-08-15T12:00:00' }),
      criarPonto({ valor: 15, dataCotacao: '2026-08-15T13:00:00' }),
    ]);
    fixture.componentRef.setInput('codigoMoeda', 'USD');
    fixture.detectChanges();
  });

  function chartDirective(): BaseChartDirective {
    return fixture.debugElement
      .query(By.directive(BaseChartDirective))
      .injector.get(BaseChartDirective);
  }

  function chamarTitle(dataIndex: number): string {
    const callback = chartDirective().options?.plugins?.tooltip?.callbacks?.title as (
      itens: TooltipItem<'line'>[],
    ) => string;
    return callback([{ dataIndex } as TooltipItem<'line'>]);
  }

  function chamarLabel(dataIndex: number): string {
    const callback = chartDirective().options?.plugins?.tooltip?.callbacks?.label as (
      item: TooltipItem<'line'>,
    ) => string;
    return callback({ dataIndex } as TooltipItem<'line'>);
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('extrai os valores dos pontos como dados do gráfico', () => {
    expect(chartDirective().data?.datasets[0].data).toEqual([10, 12, 9, 15]);
  });

  it('aplica a cor recebida na linha', () => {
    fixture.componentRef.setInput('cor', 'oklch(0.75 0.16 145)');
    fixture.detectChanges();

    expect(chartDirective().data?.datasets[0].borderColor).toBe('oklch(0.75 0.16 145)');
  });

  it('não exibe eixos nem legenda', () => {
    const opcoes = chartDirective().options;
    expect(opcoes?.plugins?.legend?.display).toBe(false);
    expect(opcoes?.scales?.['x']?.display).toBe(false);
    expect(opcoes?.scales?.['y']?.display).toBe(false);
  });

  it('ativa o hover por índice, sem precisar acertar exatamente a linha', () => {
    expect(chartDirective().options?.interaction).toEqual({ mode: 'index', intersect: false });
  });

  it('o eixo x cobre exatamente o intervalo dos pontos, sem margem extra', () => {
    const opcoes = chartDirective().options;
    expect(opcoes?.scales?.['x']?.min).toBe(0);
    expect(opcoes?.scales?.['x']?.max).toBe(3);
  });

  it('duplica um único ponto para sempre existir uma linha visível', () => {
    fixture.componentRef.setInput('pontos', [criarPonto({ valor: 42 })]);
    fixture.detectChanges();

    expect(chartDirective().data?.datasets[0].data).toEqual([42, 42]);
    expect(chartDirective().options?.scales?.['x']?.max).toBe(1);
  });

  it('sem nenhum ponto, não quebra (escala vazia em vez de max negativo)', () => {
    fixture.componentRef.setInput('pontos', []);
    fixture.detectChanges();

    expect(chartDirective().data?.datasets[0].data).toEqual([]);
    expect(chartDirective().options?.scales?.['x']?.max).toBe(0);
  });

  describe('tooltip (título e rótulo do Chart.js)', () => {
    it('usa o valor formatado (via moedaBrl + código da moeda) como título', () => {
      expect(chamarTitle(2)).toBe('R$ 9,00');
    });

    it('usa a data formatada do ponto como rótulo', () => {
      expect(chamarLabel(2)).toBe('15/08, 12:00');
    });

    it('usa as casas decimais certas pra moeda de baixo valor (ex: ARS)', () => {
      fixture.componentRef.setInput('pontos', [criarPonto({ valor: 0.0035 })]);
      fixture.componentRef.setInput('codigoMoeda', 'ARS');
      fixture.detectChanges();

      expect(chamarTitle(0)).toBe('R$ 0,0035');
    });
  });

  it('duplica também a data do ponto único, mantendo valor e data juntos', () => {
    fixture.componentRef.setInput('pontos', [
      criarPonto({ valor: 42, dataCotacao: '2026-08-15T10:00:00' }),
    ]);
    fixture.detectChanges();

    expect(chamarLabel(1)).toBe('15/08, 10:00');
  });
});
