package br.com.currencyratetracker.currency_rate_tracker_api.domain.repository;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Acesso a dados da {@link Cotacao}.
 * {@link JpaSpecificationExecutor} habilita consulta por {@code Specification}
 * (ver {@code CotacaoSpecifications}).
 */
public interface CotacaoRepository extends JpaRepository<Cotacao, UUID>, JpaSpecificationExecutor<Cotacao> {
}
