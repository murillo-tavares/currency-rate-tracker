package br.com.currencyratetracker.currency_rate_tracker_api.domain.repository;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.moeda.Moeda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Acesso a dados da {@link Moeda}.
 * {@link JpaSpecificationExecutor} habilita consulta por {@code Specification}
 * (ver {@code MoedaSpecifications}).
 */
public interface MoedaRepository extends JpaRepository<Moeda, UUID>, JpaSpecificationExecutor<Moeda> {
}
