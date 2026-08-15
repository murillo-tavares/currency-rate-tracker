package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.client.awesomeapi;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.client.CotacaoClient;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Moeda;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementação de {@link CotacaoClient} sobre a AwesomeAPI (https://docs.awesomeapi.com.br).
 */
@Component
@RequiredArgsConstructor
class AwesomeApiCotacaoClient implements CotacaoClient {

    private final AwesomeApiCotacaoApi api;

    @Override
    public List<Cotacao> buscarCotacoes(List<Moeda> moedas) {
        String pares = moedas.stream()
                .map(moeda -> moeda.getCodigo() + "-BRL")
                .collect(Collectors.joining(","));

        try {
            Map<String, AwesomeApiCotacaoResponse> resposta = api.buscarUltimasCotacoes(pares);
            return resposta.values().stream()
                    .map(AwesomeApiCotacaoResponse::paraCotacao)
                    .toList();
        } catch (RestClientException exception) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "Falha ao consultar cotações na AwesomeAPI", exception);
        }
    }
}
