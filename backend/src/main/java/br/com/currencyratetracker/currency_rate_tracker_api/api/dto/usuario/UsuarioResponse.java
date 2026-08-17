package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.usuario;

/**
 * Dados de saída de um usuário cadastrado.
 */
public record UsuarioResponse(
        String email,
        String nome
) {
}
