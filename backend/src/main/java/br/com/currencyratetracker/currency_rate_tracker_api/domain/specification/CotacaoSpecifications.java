package br.com.currencyratetracker.currency_rate_tracker_api.domain.specification;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Specifications da {@link Cotacao}, uma por campo.
 * Mantidas separadas para permitir compor filtros (and/or) sem precisar de um método por combinação.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CotacaoSpecifications {

    /** Filtra pelo código da moeda, dentre os informados. */
    public static Specification<Cotacao> comCodigoMoedaEm(List<String> codigosMoeda) {
        return (root, query, cb) -> root.get("codigoMoeda").in(codigosMoeda);
    }

    /** Filtra pela data de cotação maior ou igual à informada. */
    public static Specification<Cotacao> comDataCotacaoAPartirDe(LocalDateTime inicio) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataCotacao"), inicio);
    }

    /** Filtra pela data de cotação menor ou igual à informada. */
    public static Specification<Cotacao> comDataCotacaoAte(LocalDateTime fim) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataCotacao"), fim);
    }
}
