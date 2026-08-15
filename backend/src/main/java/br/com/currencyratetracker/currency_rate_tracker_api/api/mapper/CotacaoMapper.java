package br.com.currencyratetracker.currency_rate_tracker_api.api.mapper;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.response.CotacaoResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Conversão entre {@link Cotacao} e seus DTOs.
 * Implementação é gerada em tempo de build pelo MapStruct.
 */
@Mapper(componentModel = "spring")
public interface CotacaoMapper {

    CotacaoResponse toResponse(Cotacao cotacao);

    List<CotacaoResponse> toResponseList(List<Cotacao> cotacoes);
}
