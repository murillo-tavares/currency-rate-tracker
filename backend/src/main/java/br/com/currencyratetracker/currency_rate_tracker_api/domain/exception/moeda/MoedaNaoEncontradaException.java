package br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.moeda;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Não existe moeda no catálogo com o código informado.
 */
public final class MoedaNaoEncontradaException extends AbstractThrowableProblem {

    public static final String CODIGO = "MOEDA_NAO_ENCONTRADA";

    private MoedaNaoEncontradaException(String codigoMoeda) {
        super(null, CODIGO, Status.NOT_FOUND, "Moeda não encontrada: " + codigoMoeda);
    }

    public static MoedaNaoEncontradaException paraCodigo(String codigoMoeda) {
        return new MoedaNaoEncontradaException(codigoMoeda);
    }
}
