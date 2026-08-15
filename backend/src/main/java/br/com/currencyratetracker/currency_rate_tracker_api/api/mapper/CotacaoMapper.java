package br.com.currencyratetracker.currency_rate_tracker_api.api.mapper;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.cotacao.CotacaoResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.cotacao.DashboardResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.cotacao.GraficoResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Dashboard;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Grafico;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Conversão entre {@link Cotacao}/{@link Grafico}/{@link Dashboard} e seus DTOs.
 * Implementação é gerada em tempo de build pelo MapStruct.
 */
@Mapper(componentModel = "spring")
public interface CotacaoMapper {

    CotacaoResponse toResponse(Cotacao cotacao);

    List<CotacaoResponse> toResponseList(List<Cotacao> cotacoes);

    GraficoResponse.Ponto toPonto(Cotacao cotacao);

    List<GraficoResponse.Ponto> toPontos(List<Cotacao> cotacoes);

    GraficoResponse toGraficoResponse(Grafico grafico);

    DashboardResponse toDashboardResponse(Dashboard dashboard);
}
