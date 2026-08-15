package br.com.currencyratetracker.currency_rate_tracker_api.api.mapper;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.response.CotacaoResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.response.DashboardCotacoesResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.response.GraficoCotacaoResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.DashboardCotacoes;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.GraficoCotacao;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Conversão entre {@link Cotacao}/{@link GraficoCotacao}/{@link DashboardCotacoes} e seus DTOs.
 * Implementação é gerada em tempo de build pelo MapStruct.
 */
@Mapper(componentModel = "spring")
public interface CotacaoMapper {

    CotacaoResponse toResponse(Cotacao cotacao);

    List<CotacaoResponse> toResponseList(List<Cotacao> cotacoes);

    GraficoCotacaoResponse.Ponto toPonto(Cotacao cotacao);

    List<GraficoCotacaoResponse.Ponto> toPontos(List<Cotacao> cotacoes);

    GraficoCotacaoResponse toGraficoResponse(GraficoCotacao grafico);

    DashboardCotacoesResponse toDashboardResponse(DashboardCotacoes dashboard);
}
