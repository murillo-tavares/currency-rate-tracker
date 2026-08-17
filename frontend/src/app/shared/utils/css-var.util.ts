/**
 * Lê o valor computado de uma CSS custom property. Usado onde uma cor precisa chegar
 * já resolvida (ex.: "#1a2b3c") em vez de uma referência var(), como nas opções do Chart.js.
 */
export function lerVariavelCss(nome: string): string {
  return getComputedStyle(document.documentElement).getPropertyValue(nome).trim();
}
