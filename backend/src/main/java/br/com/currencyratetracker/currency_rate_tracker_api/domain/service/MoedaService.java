package br.com.currencyratetracker.currency_rate_tracker_api.domain.service;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.moeda.MoedaNaoEncontradaException;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.moeda.Moeda;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.repository.MoedaRepository;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.specification.MoedaSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio do catálogo de moedas. Trabalha só com o domínio ({@link Moeda}) —
 * conversão para/de DTO é responsabilidade da camada de API.
 */
@Service
@RequiredArgsConstructor
public class MoedaService {

    private final MoedaRepository moedaRepository;

    /** Lista todas as moedas disponíveis no catálogo. */
    public List<Moeda> listar() {
        return moedaRepository.findAll();
    }

    /** Busca uma moeda pelo código; lança se não existir no catálogo. */
    public Moeda buscarPorCodigo(String codigo) {
        return moedaRepository.findOne(MoedaSpecifications.comCodigo(codigo))
                .orElseThrow(() -> MoedaNaoEncontradaException.paraCodigo(codigo));
    }
}
