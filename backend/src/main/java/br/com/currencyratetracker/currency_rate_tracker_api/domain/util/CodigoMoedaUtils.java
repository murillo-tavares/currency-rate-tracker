package br.com.currencyratetracker.currency_rate_tracker_api.domain.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Tratamento de listas de código de moeda reutilizável por qualquer filtro/consulta.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CodigoMoedaUtils {

    /** Ordena e remove duplicados — mesma entrada sempre produz a mesma saída. {@code null}/vazio retorna {@code null}. */
    public static List<String> ordenarSemDuplicados(List<String> codigosMoeda) {
        return CollectionUtils.isEmpty(codigosMoeda)
                ? null
                : codigosMoeda.stream().distinct().sorted().toList();
    }
}
