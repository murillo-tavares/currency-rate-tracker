package br.com.currencyratetracker.currency_rate_tracker_api.domain.service;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.favorito.Favorito;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.repository.FavoritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Regras de negócio de moedas favoritas de um usuário.
 */
@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final MoedaService moedaService;

    /** Adiciona a moeda aos favoritos do usuário; idempotente se já for favorita. */
    public void adicionar(UUID usuarioId, String codigoMoeda) {
        moedaService.buscarPorCodigo(codigoMoeda);

        boolean jaFavoritada = favoritoRepository.existsByUsuarioIdAndCodigoMoeda(usuarioId, codigoMoeda);
        if (jaFavoritada) {
            return;
        }
        Favorito favorito = Favorito.builder()
                .usuarioId(usuarioId)
                .codigoMoeda(codigoMoeda)
                .build();
        favoritoRepository.save(favorito);
    }

    /** Remove a moeda dos favoritos do usuário; idempotente se não for favorita. */
    @Transactional
    public void remover(UUID usuarioId, String codigoMoeda) {
        favoritoRepository.deleteByUsuarioIdAndCodigoMoeda(usuarioId, codigoMoeda);
    }

    /** Lista os favoritos do usuário. */
    public List<Favorito> listar(UUID usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId);
    }
}
