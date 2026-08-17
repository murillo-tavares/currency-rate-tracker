import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelecaoPeriodo } from '../../../core/models/timeframe.model';
import { PeriodSelector } from './period-selector';

describe('PeriodSelector', () => {
  let fixture: ComponentFixture<PeriodSelector>;
  let component: PeriodSelector;
  let emitidos: SelecaoPeriodo[];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PeriodSelector],
    }).compileComponents();

    fixture = TestBed.createComponent(PeriodSelector);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('timeframe', '24h');
    fixture.detectChanges();

    emitidos = [];
    component.selecaoAlterada.subscribe((selecao) => emitidos.push(selecao));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('marca o botão do timeframe atual como ativo', () => {
    const botoes = (fixture.nativeElement as HTMLElement).querySelectorAll<HTMLButtonElement>(
      '.botao',
    );
    const botao24h = Array.from(botoes).find((b) => b.textContent?.trim() === '24h')!;
    expect(botao24h.classList.contains('ativo')).toBe(true);
  });

  it('emite o timeframe selecionado ao clicar em um botão de preset', () => {
    const botoes = (fixture.nativeElement as HTMLElement).querySelectorAll<HTMLButtonElement>(
      '.botao',
    );
    const botao7d = Array.from(botoes).find((b) => b.textContent?.trim() === '7d')!;
    botao7d.click();

    expect(emitidos).toEqual([{ timeframe: '7d' }]);
  });

  it('não exibe os campos de data fora do modo personalizado', () => {
    expect((fixture.nativeElement as HTMLElement).querySelector('.campo-data')).toBeNull();
  });

  it('exibe campos de data e emite inicio/fim no modo personalizado', () => {
    fixture.componentRef.setInput('timeframe', 'custom');
    fixture.componentRef.setInput('inicioPersonalizado', '2026-08-01');
    fixture.componentRef.setInput('fimPersonalizado', '2026-08-15');
    fixture.detectChanges();

    const campos = (fixture.nativeElement as HTMLElement).querySelectorAll<HTMLInputElement>(
      '.campo-data',
    );
    expect(campos.length).toBe(2);

    campos[0].value = '2026-08-05';
    campos[0].dispatchEvent(new Event('change'));

    expect(emitidos).toEqual([{ timeframe: 'custom', inicio: '2026-08-05', fim: '2026-08-15' }]);
  });
});
