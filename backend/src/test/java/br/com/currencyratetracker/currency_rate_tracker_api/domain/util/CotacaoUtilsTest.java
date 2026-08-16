package br.com.currencyratetracker.currency_rate_tracker_api.domain.util;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CotacaoUtilsTest {

    @Test
    void mantemCotacaoComDataDiferenteDaUltimaConhecida() {
        Cotacao ultimaConhecida = cotacao("USD", LocalDateTime.of(2026, 8, 14, 10, 0));
        Cotacao recemConsultada = cotacao("USD", LocalDateTime.of(2026, 8, 15, 10, 0));

        List<Cotacao> alteradas = CotacaoUtils.filtrarAlteradas(List.of(recemConsultada), List.of(ultimaConhecida));

        assertThat(alteradas).containsExactly(recemConsultada);
    }

    @Test
    void descartaCotacaoComMesmaDataDaUltimaConhecida() {
        LocalDateTime mesmaData = LocalDateTime.of(2026, 8, 14, 10, 0);
        Cotacao ultimaConhecida = cotacao("USD", mesmaData);
        Cotacao recemConsultada = cotacao("USD", mesmaData);

        List<Cotacao> alteradas = CotacaoUtils.filtrarAlteradas(List.of(recemConsultada), List.of(ultimaConhecida));

        assertThat(alteradas).isEmpty();
    }

    @Test
    void mantemCotacaoDeMoedaSemHistoricoAnterior() {
        Cotacao primeiraCotacao = cotacao("EUR", LocalDateTime.now());

        List<Cotacao> alteradas = CotacaoUtils.filtrarAlteradas(List.of(primeiraCotacao), List.of());

        assertThat(alteradas).containsExactly(primeiraCotacao);
    }

    private Cotacao cotacao(String codigoMoeda, LocalDateTime dataCotacao) {
        return Cotacao.builder()
                .codigoMoeda(codigoMoeda)
                .nome(codigoMoeda)
                .valor(BigDecimal.ONE)
                .variacaoPercentual(BigDecimal.ZERO)
                .dataCotacao(dataCotacao)
                .build();
    }
}
