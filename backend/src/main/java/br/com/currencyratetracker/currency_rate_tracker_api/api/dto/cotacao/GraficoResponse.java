package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.cotacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Gráfico de cotação (valor x tempo) de uma moeda.
 */
public record GraficoResponse(
        String codigoMoeda,
        List<Ponto> pontos
) {

    /** Um ponto (valor x tempo) do gráfico. */
    public record Ponto(
            BigDecimal valor,
            BigDecimal variacaoPercentual,
            LocalDateTime dataCotacao
    ) {
    }
}
