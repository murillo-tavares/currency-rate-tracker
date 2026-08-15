package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.cotacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Dados de saída da cotação atual de uma moeda.
 */
public record CotacaoResponse(
        String codigoMoeda,
        String nome,
        BigDecimal valor,
        BigDecimal variacaoPercentual,
        LocalDateTime dataCotacao
) {
}
