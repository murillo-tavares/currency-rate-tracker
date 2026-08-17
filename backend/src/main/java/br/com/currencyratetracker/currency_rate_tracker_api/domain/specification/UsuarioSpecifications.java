package br.com.currencyratetracker.currency_rate_tracker_api.domain.specification;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.usuario.Usuario;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications do {@link Usuario}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UsuarioSpecifications {

    /** Filtra pelo email do usuário. */
    public static Specification<Usuario> comEmail(String email) {
        return (root, query, cb) -> cb.equal(root.get("email"), email);
    }
}
