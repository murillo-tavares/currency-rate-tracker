export function lerVariavelCss(nome: string): string {
  return getComputedStyle(document.documentElement).getPropertyValue(nome).trim();
}
