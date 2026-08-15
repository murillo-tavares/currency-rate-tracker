package br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao;

import java.util.List;

/**
 * Série de cotação de uma moeda — os pontos (valor x tempo) que alimentam um gráfico.
 */
public record Grafico(
        String codigoMoeda,
        List<Cotacao> pontos
) {
}
