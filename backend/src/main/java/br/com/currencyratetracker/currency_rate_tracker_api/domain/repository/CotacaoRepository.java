package br.com.currencyratetracker.currency_rate_tracker_api.domain.repository;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Acesso a dados da {@link Cotacao}.
 * {@link JpaSpecificationExecutor} habilita consulta por {@code Specification}
 * (ver {@code CotacaoSpecifications}).
 */
public interface CotacaoRepository extends JpaRepository<Cotacao, UUID>, JpaSpecificationExecutor<Cotacao> {

    /**
     * Última cotação registrada de cada moeda. Desempata por {@code data_criacao} — a AwesomeAPI
     * pode repetir a mesma {@code data_cotacao} entre ciclos do scheduler quando a moeda não é
     * atualizada na origem, e nesse caso o registro mais recente deve prevalecer.
     */
    @Query(value = """
            SELECT DISTINCT ON (codigo_moeda) * FROM cotacao
            ORDER BY codigo_moeda, data_cotacao DESC, data_criacao DESC
            """, nativeQuery = true)
    List<Cotacao> buscarUltimaCotacao();

    /** Mesmo critério de {@link #buscarUltimaCotacao()}, restrito às moedas informadas. */
    @Query(value = """
            SELECT DISTINCT ON (codigo_moeda) * FROM cotacao
            WHERE codigo_moeda IN (:codigosMoeda)
            ORDER BY codigo_moeda, data_cotacao DESC, data_criacao DESC
            """, nativeQuery = true)
    List<Cotacao> buscarUltimaCotacaoPorMoeda(@Param("codigosMoeda") List<String> codigosMoeda);
}
