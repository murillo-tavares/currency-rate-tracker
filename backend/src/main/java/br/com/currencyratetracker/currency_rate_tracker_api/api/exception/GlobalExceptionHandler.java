package br.com.currencyratetracker.currency_rate_tracker_api.api.exception;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.response.ErroResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.util.ValidacaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.zalando.problem.ThrowableProblem;

import java.util.List;

/**
 * Converte toda exceção em {@link ErroResponse}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ThrowableProblem.class)
    public ResponseEntity<ErroResponse> handleProblem(ThrowableProblem ex, WebRequest request) {
        log.warn("Erro de negócio: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
        ErroResponse erro = ErroResponse.builder()
                .problem(ex)
                .request(request)
                .build();
        return ResponseEntity.status(erro.getStatus()).body(erro);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroResponse> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        ErroResponse erro = ErroResponse.builder()
                .status(ex.getStatusCode())
                .message(ex.getReason())
                .request(request)
                .build();
        return ResponseEntity.status(ex.getStatusCode()).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleFalhaInesperada(Exception ex, WebRequest request) {
        log.error("Erro não tratado", ex);
        ErroResponse erro = ErroResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Erro interno inesperado")
                .request(request)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {

        List<ErroResponse.CampoInvalido> campos = ValidacaoUtils.extrairCampos(ex);
        String mensagem = campos != null ? "Erro de validação" : ex.getMessage();

        ErroResponse erro = ErroResponse.builder()
                .status(statusCode)
                .message(mensagem)
                .errors(campos)
                .request(request)
                .build();
        return ResponseEntity.status(statusCode).headers(headers).body(erro);
    }
}
