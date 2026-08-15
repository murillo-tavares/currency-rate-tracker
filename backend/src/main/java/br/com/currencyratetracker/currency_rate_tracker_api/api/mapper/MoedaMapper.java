package br.com.currencyratetracker.currency_rate_tracker_api.api.mapper;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.response.MoedaResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Moeda;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Conversão entre {@link Moeda} e seus DTOs.
 * Implementação é gerada em tempo de build pelo MapStruct.
 */
@Mapper(componentModel = "spring")
public interface MoedaMapper {

    MoedaResponse toResponse(Moeda moeda);

    List<MoedaResponse> toResponseList(List<Moeda> moedas);
}
