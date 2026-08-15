package br.com.currencyratetracker.currency_rate_tracker_api.domain.specification;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.util.SpecificationUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Builder fluente para compor {@link Specification} de forma legível.
 * O padrão utilizado é encadear {@code add(...)} e {@code addIfPresent(...)} e, no fim,
 * gerar uma única specification combinada com {@code AND}.
 */
public final class SpecificationBuilder<T> {

    private final List<Specification<T>> specifications = new ArrayList<>();

    /** Adiciona uma specification à composição; valores nulos são ignorados. */
    public SpecificationBuilder<T> add(Specification<T> specification) {
        if (specification != null) {
            specifications.add(specification);
        }
        return this;
    }

    /** Adiciona uma specification apenas quando o valor informado estiver presente. */
    public <V> SpecificationBuilder<T> addIfPresent(V value, Function<V, Specification<T>> mapper) {
        if (value != null && mapper != null) {
            return add(mapper.apply(value));
        }
        return this;
    }

    /** Produz a specification final combinando todas as entradas com {@code AND}. */
    public Specification<T> build() {
        return SpecificationUtils.andAll(specifications);
    }
}
