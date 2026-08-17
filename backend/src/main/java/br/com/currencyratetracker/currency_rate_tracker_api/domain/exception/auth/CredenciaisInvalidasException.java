package br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.auth;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Email ou senha não conferem com nenhum usuário cadastrado.
 */
public final class CredenciaisInvalidasException extends AbstractThrowableProblem {

    public static final String CODIGO = "CREDENCIAIS_INVALIDAS";

    public CredenciaisInvalidasException() {
        super(null, CODIGO, Status.UNAUTHORIZED, "Email ou senha inválidos");
    }
}
