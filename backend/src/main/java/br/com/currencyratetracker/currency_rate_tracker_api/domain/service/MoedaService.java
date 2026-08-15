package br.com.currencyratetracker.currency_rate_tracker_api.domain.service;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Moeda;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.repository.MoedaRepository;
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
}
