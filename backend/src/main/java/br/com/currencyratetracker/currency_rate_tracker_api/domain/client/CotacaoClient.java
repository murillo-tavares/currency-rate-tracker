package br.com.currencyratetracker.currency_rate_tracker_api.domain.client;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Moeda;

import java.util.List;

/**
 * Contrato para consulta externa de cotação de moedas.
 * <p>
 * Isola o domínio do provedor concreto usado por trás (hoje a AwesomeAPI). Trocar de
 * fornecedor no futuro demanda apenas uma nova implementação desta interface, sem alterar
 * quem a consome.
 */
public interface CotacaoClient {

    /**
     * Consulta a cotação atual das moedas informadas.
     */
    List<Cotacao> buscarCotacoes(List<Moeda> moedas);
}
