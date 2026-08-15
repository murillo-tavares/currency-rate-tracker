package br.com.currencyratetracker.currency_rate_tracker_api.domain.model;

import java.util.List;

/**
 * Série de cotação de uma moeda — os pontos (valor x tempo) que alimentam um gráfico.
 */
public record GraficoCotacao(
        String codigoMoeda,
        List<Cotacao> pontos
) {
}
