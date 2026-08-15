package br.com.currencyratetracker.currency_rate_tracker_api.domain.repository;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Acesso a dados da {@link Cotacao}.
 */
public interface CotacaoRepository extends JpaRepository<Cotacao, UUID> {
}
