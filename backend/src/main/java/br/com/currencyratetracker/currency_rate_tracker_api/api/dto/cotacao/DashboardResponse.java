package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.cotacao;

import java.util.List;

/**
 * Agrupamento dos gráficos de uma ou várias moedas pedidas de uma vez.
 */
public record DashboardResponse(
        List<GraficoResponse> graficos
) {
}
