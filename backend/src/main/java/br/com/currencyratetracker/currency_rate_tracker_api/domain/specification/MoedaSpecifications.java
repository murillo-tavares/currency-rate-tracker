package br.com.currencyratetracker.currency_rate_tracker_api.domain.specification;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.moeda.Moeda;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications da {@link Moeda}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MoedaSpecifications {

    /** Filtra pelo código da moeda. */
    public static Specification<Moeda> comCodigo(String codigo) {
        return (root, query, cb) -> cb.equal(root.get("codigo"), codigo);
    }
}
