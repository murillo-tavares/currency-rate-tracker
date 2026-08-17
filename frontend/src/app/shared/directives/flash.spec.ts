import { Component, signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlashDirective } from './flash';

@Component({
  imports: [FlashDirective],
  template: `<div [appFlash]="valor()"></div>`,
})
class HostDeTeste {
  readonly valor = signal(10);
}

describe('FlashDirective', () => {
  let fixture: ComponentFixture<HostDeTeste>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HostDeTeste],
    }).compileComponents();

    fixture = TestBed.createComponent(HostDeTeste);
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  function div(): HTMLElement {
    return fixture.nativeElement.querySelector('div');
  }

  it('não pisca no primeiro valor recebido', () => {
    fixture.detectChanges();

    expect(div().classList.contains('flash-up')).toBe(false);
    expect(div().classList.contains('flash-down')).toBe(false);
  });

  it('aplica flash-up quando o valor sobe e remove após a duração', () => {
    vi.useFakeTimers();
    fixture.detectChanges();

    fixture.componentInstance.valor.set(15);
    fixture.detectChanges();

    expect(div().classList.contains('flash-up')).toBe(true);

    vi.advanceTimersByTime(900);
    fixture.detectChanges();

    expect(div().classList.contains('flash-up')).toBe(false);
  });

  it('aplica flash-down quando o valor cai', () => {
    vi.useFakeTimers();
    fixture.detectChanges();

    fixture.componentInstance.valor.set(5);
    fixture.detectChanges();

    expect(div().classList.contains('flash-down')).toBe(true);
  });
});
