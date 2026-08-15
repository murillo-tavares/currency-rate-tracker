package br.com.currencyratetracker.currency_rate_tracker_api.api.dto.response;

import br.com.currencyratetracker.currency_rate_tracker_api.api.util.WebRequestUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.context.request.WebRequest;
import org.zalando.problem.ThrowableProblem;

import java.util.List;

/**
 * Formato padrão de erro devolvido pela API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class ErroResponse {

    private final int status;
    private final String error;
    private final String codigo;
    private final String message;
    private final String path;

    /** Só aparece em erros de validação. */
    private final List<CampoInvalido> errors;

    public record CampoInvalido(String field, String message) {
    }

    /** Lombok injeta os setters dos campos aqui dentro; os métodos abaixo são os que derivam valor. */
    public static class ErroResponseBuilder {

        /** Preenche status, código e mensagem a partir de um erro de negócio. */
        public ErroResponseBuilder problem(ThrowableProblem ex) {
            return status(HttpStatusCode.valueOf(ex.getStatus().getStatusCode()))
                    .codigo(ex.getTitle())
                    .message(ex.getDetail());
        }

        /** Deriva {@code error} (reason phrase) a partir do status HTTP. */
        public ErroResponseBuilder status(HttpStatusCode status) {
            this.status = status.value();
            this.error = HttpStatus.valueOf(status.value()).getReasonPhrase();
            return this;
        }

        /** Deriva {@code path} a partir da requisição atual. */
        public ErroResponseBuilder request(WebRequest request) {
            return path(WebRequestUtils.caminho(request));
        }
    }
}
