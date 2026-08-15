package br.com.currencyratetracker.currency_rate_tracker_api.domain.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;

/**
 * Auxilia a composição de {@link Specification} em consultas JPA.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpecificationUtils {

    /** Combina todas as specifications recebidas em uma única specification aplicada com {@code AND}. */
    public static <T> Specification<T> andAll(List<Specification<T>> specifications) {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(
                        Specification.<T>where((root, query, cb) -> cb.conjunction()),
                        Specification::and
                );
    }
}
