package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.cotacao.CotacaoResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.cotacao.DashboardResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.mapper.CotacaoMapper;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.filter.FiltroDashboardCotacoes;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Dashboard;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.CotacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /** Lista a cotação atual das moedas do catálogo (ou só as informadas), servida a partir do cache. */
    @GetMapping
    public List<CotacaoResponse> listar(@RequestParam(required = false) List<String> codigosMoeda) {
        List<Cotacao> cotacoes = cotacaoService.obterCotacoesAtuais(codigosMoeda);
        return cotacaoMapper.toResponseList(cotacoes);
    }

    /** Dashboard de cotação (uma ou várias moedas) dentro do filtro informado. */
    @GetMapping("/dashboard")
    public DashboardResponse buscarDashboard(@ModelAttribute FiltroDashboardCotacoes filtro) {
        Dashboard dashboard = cotacaoService.buscarDashboard(filtro);
        return cotacaoMapper.toDashboardResponse(dashboard);
    }
}
