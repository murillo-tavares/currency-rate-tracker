package br.com.currencyratetracker.currency_rate_tracker_api.domain.repository;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.favorito.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Acesso a dados do {@link Favorito}.
 */
public interface FavoritoRepository extends JpaRepository<Favorito, UUID> {

    List<Favorito> findByUsuarioId(UUID usuarioId);

    boolean existsByUsuarioIdAndCodigoMoeda(UUID usuarioId, String codigoMoeda);

    void deleteByUsuarioIdAndCodigoMoeda(UUID usuarioId, String codigoMoeda);
}
