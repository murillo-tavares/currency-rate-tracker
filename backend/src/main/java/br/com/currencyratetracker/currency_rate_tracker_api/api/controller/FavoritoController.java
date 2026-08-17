package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.favorito.FavoritoResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.mapper.FavoritoMapper;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.favorito.Favorito;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Endpoints REST de moedas favoritas do usuário autenticado. Converte entre DTO e domínio
 * usando {@link FavoritoMapper}.
 */
@RestController
@RequestMapping("/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;
    private final FavoritoMapper favoritoMapper;

    /** Lista os favoritos do usuário autenticado. */
    @GetMapping
    public List<FavoritoResponse> listar(@AuthenticationPrincipal UUID usuarioId) {
        List<Favorito> favoritos = favoritoService.listar(usuarioId);
        return favoritoMapper.toResponseList(favoritos);
    }

    /** Adiciona uma moeda aos favoritos do usuário autenticado. */
    @PostMapping("/{codigoMoeda}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adicionar(@AuthenticationPrincipal UUID usuarioId, @PathVariable String codigoMoeda) {
        favoritoService.adicionar(usuarioId, codigoMoeda);
    }

    /** Remove uma moeda dos favoritos do usuário autenticado. */
    @DeleteMapping("/{codigoMoeda}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@AuthenticationPrincipal UUID usuarioId, @PathVariable String codigoMoeda) {
        favoritoService.remover(usuarioId, codigoMoeda);
    }
}
