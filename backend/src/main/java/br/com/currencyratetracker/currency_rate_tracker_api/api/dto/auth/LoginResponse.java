package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.auth;

/**
 * Dados de saída do login: token JWT a ser enviado em {@code Authorization: Bearer <token>}.
 */
public record LoginResponse(
        String token
) {
}
