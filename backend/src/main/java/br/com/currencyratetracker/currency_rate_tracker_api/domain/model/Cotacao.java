package br.com.currencyratetracker.currency_rate_tracker_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Cotação atual de uma moeda em relação ao Real (BRL), consultada na AwesomeAPI.
 */
public record Cotacao(
        String codigo,
        String nome,
        BigDecimal valor,
        BigDecimal variacaoPercentual,
        LocalDateTime dataAtualizacao
) {
}
