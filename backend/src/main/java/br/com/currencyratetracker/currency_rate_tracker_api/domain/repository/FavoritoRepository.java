package br.com.currencyratetracker.currency_rate_tracker_api.domain.repository;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.favorito.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Acesso a dados do {@link Favorito}.
 * {@link JpaSpecificationExecutor} habilita consulta por {@code Specification}
 * (ver {@code FavoritoSpecifications}).
 */
public interface FavoritoRepository extends JpaRepository<Favorito, UUID>, JpaSpecificationExecutor<Favorito> {
}
