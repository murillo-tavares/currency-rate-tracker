package br.com.currencyratetracker.currency_rate_tracker_api.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuração do cliente HTTP para a AwesomeAPI, usada para consultar cotações de moedas.
 */
@ConfigurationProperties(prefix = "integracao.awesome-api")
public record AwesomeApiProperties(
        @DefaultValue("https://economia.awesomeapi.com.br")
        String baseUrl,
        @DefaultValue("3000") int connectTimeoutMillis,
        @DefaultValue("3000") int readTimeoutMillis
) {
}
