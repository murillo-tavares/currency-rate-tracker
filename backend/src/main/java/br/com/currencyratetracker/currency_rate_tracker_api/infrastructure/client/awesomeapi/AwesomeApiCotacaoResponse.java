package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.client.awesomeapi;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.moeda.Moeda;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Formato bruto de uma cotação retornada pela AwesomeAPI.
 */
record AwesomeApiCotacaoResponse(
        String code,
        BigDecimal bid,
        BigDecimal pctChange,
        @JsonProperty("create_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createDate
) {

    /** Converte a resposta bruta da AwesomeAPI para o domínio, usando código/nome do catálogo. */
    Cotacao paraCotacao(Moeda moeda) {
        return Cotacao.builder()
                .codigoMoeda(moeda.getCodigo())
                .nome(moeda.getNome())
                .valor(bid)
                .variacaoPercentual(pctChange)
                .dataCotacao(createDate)
                .build();
    }
}
