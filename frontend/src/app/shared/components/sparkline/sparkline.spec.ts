import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { BaseChartDirective } from 'ng2-charts';

import { Sparkline } from './sparkline';

describe('Sparkline', () => {
  let component: Sparkline;
  let fixture: ComponentFixture<Sparkline>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Sparkline],
    }).compileComponents();

    fixture = TestBed.createComponent(Sparkline);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('valores', [10, 12, 9, 15]);
    fixture.detectChanges();
  });

  function chartDirective(): BaseChartDirective {
    return fixture.debugElement
      .query(By.directive(BaseChartDirective))
      .injector.get(BaseChartDirective);
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('passa os valores recebidos como dados do gráfico', () => {
    expect(chartDirective().data?.datasets[0].data).toEqual([10, 12, 9, 15]);
  });

  it('aplica a cor recebida na linha', () => {
    fixture.componentRef.setInput('cor', 'oklch(0.75 0.16 145)');
    fixture.detectChanges();

    expect(chartDirective().data?.datasets[0].borderColor).toBe('oklch(0.75 0.16 145)');
  });

  it('não exibe eixos, legenda nem tooltip', () => {
    const opcoes = chartDirective().options;
    expect(opcoes?.plugins?.legend?.display).toBe(false);
    expect(opcoes?.plugins?.tooltip?.enabled).toBe(false);
    expect(opcoes?.scales?.['x']?.display).toBe(false);
    expect(opcoes?.scales?.['y']?.display).toBe(false);
  });

  it('o eixo x cobre exatamente o intervalo dos pontos, sem margem extra', () => {
    const opcoes = chartDirective().options;
    expect(opcoes?.scales?.['x']?.min).toBe(0);
    expect(opcoes?.scales?.['x']?.max).toBe(3);
  });

  it('duplica um único valor para sempre existir uma linha visível', () => {
    fixture.componentRef.setInput('valores', [42]);
    fixture.detectChanges();

    expect(chartDirective().data?.datasets[0].data).toEqual([42, 42]);
    expect(chartDirective().options?.scales?.['x']?.max).toBe(1);
  });
});
