package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dados de entrada para cadastro de um novo usuário.
 */
public record CadastroRequest(
        @NotBlank @Email String email,
        @NotBlank String nome,
        @NotBlank @Size(min = 6) String senha
) {
}
