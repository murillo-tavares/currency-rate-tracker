const DECIMAIS_PADRAO = 2;

const DECIMAIS_POR_MOEDA: Record<string, number> = {
  ARS: 4,
  JPY: 4,
};

export function decimaisPara(codigoMoeda: string): number {
  return DECIMAIS_POR_MOEDA[codigoMoeda] ?? DECIMAIS_PADRAO;
}
