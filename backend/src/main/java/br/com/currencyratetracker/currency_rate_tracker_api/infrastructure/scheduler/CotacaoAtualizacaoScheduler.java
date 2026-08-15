package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.scheduler;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.CotacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Atualiza periodicamente o cache de cotações, para que requisições de usuário nunca
 * cheguem a chamar a AwesomeAPI diretamente.
 */
@Component
@RequiredArgsConstructor
class CotacaoAtualizacaoScheduler {

    private final CotacaoService cotacaoService;

    /** Consulta a AwesomeAPI e atualiza o cache no intervalo configurado. */
    @Scheduled(fixedRateString = "${cotacao.atualizacao.intervalo-millis:60000}")
    void atualizarCotacoes() {
        cotacaoService.atualizarCotacoesEmCache();
    }
}
