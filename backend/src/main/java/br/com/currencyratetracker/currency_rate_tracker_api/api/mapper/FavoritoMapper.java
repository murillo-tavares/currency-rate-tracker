package br.com.currencyratetracker.currency_rate_tracker_api.api.mapper;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.favorito.FavoritoResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.favorito.Favorito;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Conversão entre {@link Favorito} e seus DTOs.
 * Implementação é gerada em tempo de build pelo MapStruct.
 */
@Mapper(componentModel = "spring")
public interface FavoritoMapper {

    FavoritoResponse toResponse(Favorito favorito);

    List<FavoritoResponse> toResponseList(List<Favorito> favoritos);
}
