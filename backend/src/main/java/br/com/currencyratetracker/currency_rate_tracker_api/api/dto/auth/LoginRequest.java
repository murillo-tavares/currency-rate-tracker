package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Dados de entrada para login.
 */
public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String senha
) {
}
