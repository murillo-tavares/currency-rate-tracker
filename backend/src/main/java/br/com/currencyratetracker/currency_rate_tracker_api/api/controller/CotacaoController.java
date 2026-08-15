package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.response.CotacaoResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.mapper.CotacaoMapper;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.CotacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints REST de cotação de moedas. Converte entre DTO e domínio usando {@link CotacaoMapper}.
 */
@RestController
@RequestMapping("/cotacoes")
@RequiredArgsConstructor
public class CotacaoController {

    private final CotacaoService cotacaoService;
    private final CotacaoMapper cotacaoMapper;

    /** Lista a cotação atual de todas as moedas do catálogo. */
    @GetMapping
    public List<CotacaoResponse> listar() {
        List<Cotacao> cotacoes = cotacaoService.buscarCotacoesAtuais();
        return cotacaoMapper.toResponseList(cotacoes);
    }
}
