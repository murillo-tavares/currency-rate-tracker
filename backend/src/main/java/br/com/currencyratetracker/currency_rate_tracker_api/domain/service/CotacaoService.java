package br.com.currencyratetracker.currency_rate_tracker_api.domain.service;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.client.CotacaoClient;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Moeda;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.repository.MoedaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio de cotação de moedas. Busca as moedas do catálogo e delega a consulta
 * dos valores atuais ao {@link CotacaoClient}.
 */
@Service
@RequiredArgsConstructor
public class CotacaoService {

    private final MoedaRepository moedaRepository;
    private final CotacaoClient cotacaoClient;

    /** Busca a cotação atual de todas as moedas do catálogo. */
    public List<Cotacao> buscarCotacoesAtuais() {
        List<Moeda> moedas = moedaRepository.findAll();
        return cotacaoClient.buscarCotacoes(moedas);
    }
}
