package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.moeda.MoedaResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.mapper.MoedaMapper;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.moeda.Moeda;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.MoedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints REST do catálogo de moedas. Converte entre DTO e domínio usando {@link MoedaMapper}.
 */
@RestController
@RequestMapping("/moedas")
@RequiredArgsConstructor
public class MoedaController {

    private final MoedaService moedaService;
    private final MoedaMapper moedaMapper;

    /** Lista todas as moedas disponíveis para cotação. */
    @GetMapping
    public List<MoedaResponse> listar() {
        List<Moeda> moedas = moedaService.listar();
        return moedaMapper.toResponseList(moedas);
    }
}
