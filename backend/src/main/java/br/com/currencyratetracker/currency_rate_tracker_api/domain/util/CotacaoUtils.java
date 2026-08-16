package br.com.currencyratetracker.currency_rate_tracker_api.domain.util;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Operações auxiliares sobre {@link Cotacao} reutilizáveis por serviços e filtros.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CotacaoUtils {

    /** Mantém só as cotações cuja data difere da última conhecida daquela moeda. */
    public static List<Cotacao> filtrarAlteradas(List<Cotacao> cotacoes, List<Cotacao> ultimasCotacoes) {
        Map<String, LocalDateTime> ultimaDataPorMoeda = ultimasCotacoes.stream()
                .collect(Collectors.toMap(Cotacao::getCodigoMoeda, Cotacao::getDataCotacao));

        return cotacoes.stream()
                .filter(cotacao -> {
                    LocalDateTime ultimaData = ultimaDataPorMoeda.get(cotacao.getCodigoMoeda());
                    return !cotacao.getDataCotacao().equals(ultimaData);
                })
                .toList();
    }
}
