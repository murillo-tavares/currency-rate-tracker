package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dados de entrada para cadastro de um novo usuário.
 */
public record CadastroRequest(
        @NotBlank
        @Email
        @Size(max = 150, message = "email deve ter no máximo 150 caracteres")
        String email,

        @NotBlank
        @Size(max = 100, message = "nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank
        @Size(min = 6, max = 72, message = "senha deve ter entre 6 e 72 caracteres")
        String senha
) {
}
