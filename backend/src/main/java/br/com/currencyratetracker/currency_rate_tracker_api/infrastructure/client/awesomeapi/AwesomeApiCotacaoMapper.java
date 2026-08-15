package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.client.awesomeapi;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.moeda.Moeda;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Monta a string de pares da consulta à AwesomeAPI e converte cada item da resposta pra
 * {@link Cotacao}, resolvendo a moeda correspondente.
 */
class AwesomeApiCotacaoMapper {

    /** Moedas a consultar, indexadas por código pra resolver rápido cada item da resposta. */
    private final Map<String, Moeda> moedas;

    AwesomeApiCotacaoMapper(List<Moeda> moedas) {
        this.moedas = moedas.stream().collect(Collectors.toMap(Moeda::getCodigo, Function.identity()));
    }

    /** Pares no formato esperado pela AwesomeAPI, ex.: "USD-BRL,EUR-BRL". */
    String paresConsulta() {
        return moedas.values().stream()
                .map(moeda -> moeda.getCodigo() + "-BRL")
                .collect(Collectors.joining(","));
    }

    /** Converte um item da resposta bruta pra domínio, resolvendo a moeda pelo código. */
    Cotacao paraCotacao(AwesomeApiCotacaoResponse item) {
        Moeda moeda = moedas.get(item.code());
        return item.paraCotacao(moeda);
    }
}
