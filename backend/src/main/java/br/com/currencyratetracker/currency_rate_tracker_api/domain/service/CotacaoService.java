package br.com.currencyratetracker.currency_rate_tracker_api.domain.service;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.client.CotacaoClient;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.filter.FiltroDashboardCotacoes;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Dashboard;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.moeda.Moeda;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.repository.CotacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio de cotação de moedas. Busca as moedas do catálogo e delega a consulta
 * dos valores atuais ao {@link CotacaoClient}, guardando o resultado em cache.
 */
@Service
@RequiredArgsConstructor
public class CotacaoService {

    public static final String CACHE_COTACOES = "cotacoes";
    public static final String CACHE_DASHBOARD = "cotacoes-dashboard";
    private static final String CHAVE_CACHE_ATUAL = "'atual'";

    private final MoedaService moedaService;
    private final CotacaoRepository cotacaoRepository;
    private final CotacaoClient cotacaoClient;

    /** Lê a cotação atual do cache; se ainda não populado, consulta a AwesomeAPI na hora. */
    @Cacheable(cacheNames = CACHE_COTACOES, key = CHAVE_CACHE_ATUAL)
    public List<Cotacao> obterCotacoesAtuais() {
        return buscarCotacoesNaOrigem();
    }

    /** Consulta a AwesomeAPI, atualiza o cache e persiste a cotação. Chamado pelo job agendado. */
    @CachePut(cacheNames = CACHE_COTACOES, key = CHAVE_CACHE_ATUAL)
    public List<Cotacao> atualizarCotacoes() {
        List<Cotacao> cotacoes = buscarCotacoesNaOrigem();
        cotacaoRepository.saveAll(cotacoes);
        return cotacoes;
    }

    /** Busca o dashboard de cotações (uma ou várias moedas) dentro do filtro informado. */
    @Cacheable(cacheNames = CACHE_DASHBOARD)
    public Dashboard buscarDashboard(FiltroDashboardCotacoes filtro) {
        Specification<Cotacao> especificacao = filtro.toSpecification();
        Sort ordenacaoPorData = Sort.by("dataCotacao").ascending();

        List<Cotacao> cotacoes = cotacaoRepository.findAll(especificacao, ordenacaoPorData);
        return Dashboard.de(cotacoes);
    }

    private List<Cotacao> buscarCotacoesNaOrigem() {
        List<Moeda> moedas = moedaService.listar();
        return cotacaoClient.buscarCotacoes(moedas);
    }
}
