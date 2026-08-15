package br.com.currencyratetracker.currency_rate_tracker_api.domain.repository;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Moeda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Acesso a dados da {@link Moeda}.
 */
public interface MoedaRepository extends JpaRepository<Moeda, UUID> {
}
