package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.client.awesomeapi;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Formato bruto de uma cotação retornada pela AwesomeAPI.
 */
record AwesomeApiCotacaoResponse(
        String code,
        String name,
        BigDecimal bid,
        BigDecimal pctChange,
        @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createDate
) {

    /** Converte a resposta bruta da AwesomeAPI para o domínio. */
    Cotacao paraCotacao() {
        return new Cotacao(code, name, bid, pctChange, createDate);
    }
}
