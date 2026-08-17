package br.com.currencyratetracker.currency_rate_tracker_api.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuração dos tokens JWT emitidos no login.
 */
@ConfigurationProperties(prefix = "seguranca.jwt")
public record JwtProperties(
        @DefaultValue("chave-secreta-apenas-para-desenvolvimento-trocar-em-producao-000000")
        String secret,
        @DefaultValue("3600000") long expiracaoMillis
) {
}
