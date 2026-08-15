package br.com.currencyratetracker.currency_rate_tracker_api.domain.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Agrupamento dos gráficos de uma ou várias moedas pedidas de uma vez, pra alimentar um
 * dashboard sem uma requisição por moeda.
 */
public record DashboardCotacoes(
        List<GraficoCotacao> graficos
) {

    /** Agrupa as cotações por moeda, uma {@link GraficoCotacao} por código, ordenadas por código. */
    public static DashboardCotacoes de(List<Cotacao> cotacoes) {
        Map<String, List<Cotacao>> pontosPorMoeda = cotacoes.stream().collect(Collectors.groupingBy(Cotacao::getCodigo));
        List<GraficoCotacao> graficos = pontosPorMoeda.entrySet().stream()
                .map(entrada -> new GraficoCotacao(entrada.getKey(), entrada.getValue()))
                .sorted(Comparator.comparing(GraficoCotacao::codigoMoeda))
                .toList();
        return new DashboardCotacoes(graficos);
    }
}
