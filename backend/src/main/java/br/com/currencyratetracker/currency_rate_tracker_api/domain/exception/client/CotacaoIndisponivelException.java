package br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.client;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * A consulta de cotação não pôde ser concluída (AwesomeAPI indisponível, timeout ou
 * resposta em formato inesperado). 503: a falha é da dependência externa, não de algo
 * que o cliente da API tenha feito de errado.
 */
public final class CotacaoIndisponivelException extends AbstractThrowableProblem {

    public static final String CODIGO = "COTACAO_INDISPONIVEL";

    private CotacaoIndisponivelException(String detail) {
        super(null, CODIGO, Status.SERVICE_UNAVAILABLE, detail);
    }

    public static CotacaoIndisponivelException falhaNaConsulta(Exception causa) {
        return new CotacaoIndisponivelException("Falha ao consultar cotações na AwesomeAPI: " + causa.getMessage());
    }
}
