package br.com.currencyratetracker.currency_rate_tracker_api.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Auxilia extração de dados de {@link WebRequest}, reutilizável por qualquer handler/filtro.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WebRequestUtils {

    /** Caminho da requisição atual (ex.: {@code /moedas}). */
    public static String caminho(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }
        return request.getDescription(false);
    }
}
