export type Timeframe = '24h' | '7d' | '30d' | 'custom';

export interface SelecaoPeriodo {
  timeframe: Timeframe;
  inicio?: string;
  fim?: string;
}
