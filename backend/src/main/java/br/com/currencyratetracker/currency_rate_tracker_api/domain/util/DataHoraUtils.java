package br.com.currencyratetracker.currency_rate_tracker_api.domain.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Tratamento de datas reutilizável por qualquer filtro/consulta.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataHoraUtils {

    /** Trunca segundos/nanos — datas próximas caem no mesmo valor. {@code null} retorna {@code null}. */
    public static LocalDateTime arredondarParaMinuto(LocalDateTime data) {
        return data != null
                ? data.truncatedTo(ChronoUnit.MINUTES)
                : null;
    }
}
