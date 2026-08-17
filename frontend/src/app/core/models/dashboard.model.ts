export interface PontoHistorico {
  valor: number;
  variacaoPercentual: number;
  dataCotacao: string;
}

export interface GraficoCotacao {
  codigoMoeda: string;
  pontos: PontoHistorico[];
}

export interface DashboardCotacoes {
  graficos: GraficoCotacao[];
}
