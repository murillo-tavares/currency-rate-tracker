package br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Agrupamento dos gráficos de uma ou várias moedas pedidas de uma vez, pra alimentar um
 * dashboard sem uma requisição por moeda.
 */
public record Dashboard(
        List<Grafico> graficos
) {

    /** Agrupa as cotações por moeda, um {@link Grafico} por código, ordenados por código. */
    public static Dashboard de(List<Cotacao> cotacoes) {
        Map<String, List<Cotacao>> pontosPorMoeda = cotacoes.stream().collect(Collectors.groupingBy(Cotacao::getCodigoMoeda));
        List<Grafico> graficos = pontosPorMoeda.entrySet().stream()
                .map(entrada -> new Grafico(entrada.getKey(), entrada.getValue()))
                .sorted(Comparator.comparing(Grafico::codigoMoeda))
                .toList();
        return new Dashboard(graficos);
    }
}
