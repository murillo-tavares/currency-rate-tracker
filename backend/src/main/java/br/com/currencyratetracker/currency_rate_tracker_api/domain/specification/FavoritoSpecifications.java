package br.com.currencyratetracker.currency_rate_tracker_api.domain.specification;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.favorito.Favorito;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

/**
 * Specifications do {@link Favorito}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FavoritoSpecifications {

    /** Filtra pelo id do usuário dono do favorito. */
    public static Specification<Favorito> comUsuarioId(UUID usuarioId) {
        return (root, query, cb) -> cb.equal(root.get("usuarioId"), usuarioId);
    }

    /** Filtra pelo código da moeda favoritada. */
    public static Specification<Favorito> comCodigoMoeda(String codigoMoeda) {
        return (root, query, cb) -> cb.equal(root.get("codigoMoeda"), codigoMoeda);
    }

    /** Identifica o favorito de um usuário para uma moeda específica. */
    public static Specification<Favorito> comUsuarioIdECodigoMoeda(UUID usuarioId, String codigoMoeda) {
        return new SpecificationBuilder<Favorito>()
                .add(comUsuarioId(usuarioId))
                .add(comCodigoMoeda(codigoMoeda))
                .build();
    }
}
