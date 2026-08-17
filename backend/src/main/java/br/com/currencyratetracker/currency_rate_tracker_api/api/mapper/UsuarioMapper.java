package br.com.currencyratetracker.currency_rate_tracker_api.api.mapper;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.usuario.UsuarioResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.usuario.Usuario;
import org.mapstruct.Mapper;

/**
 * Conversão entre {@link Usuario} e seus DTOs.
 * Implementação é gerada em tempo de build pelo MapStruct.
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponse toResponse(Usuario usuario);
}
