package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.client.awesomeapi;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Map;

/**
 * Contrato HTTP da AwesomeAPI. A resposta é um objeto cujas chaves são os pares consultados
 * (ex.: "USDBRL"), por isso o retorno é um mapa em vez de uma lista.
 */
interface AwesomeApiCotacaoApi {

    /**
     * Busca a última cotação dos pares informados, ex.: "USD-BRL,EUR-BRL".
     */
    @GetExchange("/last/{pares}")
    Map<String, AwesomeApiCotacaoResponse> buscarUltimasCotacoes(@PathVariable String pares);
}
