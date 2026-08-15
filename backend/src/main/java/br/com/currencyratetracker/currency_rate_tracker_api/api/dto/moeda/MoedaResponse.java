package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.moeda;

/**
 * Dados de saída de uma moeda do catálogo.
 */
public record MoedaResponse(
        String codigo,
        String nome
) {
}
