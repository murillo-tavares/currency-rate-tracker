package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.security;

import br.com.currencyratetracker.currency_rate_tracker_api.properties.JwtProperties;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtProperties properties = new JwtProperties("chave-de-teste-com-pelo-menos-32-caracteres-000", 3_600_000);
    private final JwtService jwtService = new JwtService(properties);

    @Test
    void deveGerarTokenERecuperarOMesmoUsuarioId() {
        UUID usuarioId = UUID.randomUUID();

        String token = jwtService.gerarToken(usuarioId);

        assertThat(jwtService.extrairUsuarioId(token)).isEqualTo(usuarioId);
    }
}
