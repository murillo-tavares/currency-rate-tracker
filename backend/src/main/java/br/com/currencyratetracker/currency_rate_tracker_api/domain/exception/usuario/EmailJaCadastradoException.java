package br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.usuario;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Já existe um usuário cadastrado com o email informado.
 */
public final class EmailJaCadastradoException extends AbstractThrowableProblem {

    public static final String CODIGO = "EMAIL_JA_CADASTRADO";

    private EmailJaCadastradoException(String email) {
        super(null, CODIGO, Status.CONFLICT, "Já existe um usuário cadastrado com o email " + email);
    }

    public static EmailJaCadastradoException paraEmail(String email) {
        return new EmailJaCadastradoException(email);
    }
}
