package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dados de entrada para login.
 */
public record LoginRequest(
        @NotBlank
        @Email
        @Size(max = 150, message = "email deve ter no máximo 150 caracteres")
        String email,

        @NotBlank
        @Size(max = 72, message = "senha deve ter no máximo 72 caracteres")
        String senha
) {
}
