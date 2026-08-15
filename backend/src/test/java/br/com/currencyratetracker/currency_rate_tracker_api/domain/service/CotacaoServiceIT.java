package br.com.currencyratetracker.currency_rate_tracker_api.domain.service;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.client.CotacaoClient;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.support.suite.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Único teste que precisa do cache de verdade ligado — sobrescreve o padrão de {@link IntegrationTest}. */
@TestPropertySource(properties = "spring.cache.type=redis")
class CotacaoServiceIT extends IntegrationTest {

    @Autowired
    private CotacaoService cotacaoService;

    @MockitoBean
    private CotacaoClient cotacaoClient;

    /**
     * O mock devolve valores diferentes a cada chamada, então só passa se as leituras
     * realmente vierem do cache: se caíssem na origem de novo, pegariam o valor mais recente.
     */
    @Test
    void deveServirCotacoesDoCacheAposAtualizar() {
        Cotacao cotacaoEmCache = Cotacao.builder()
                .codigoMoeda("USD")
                .nome("Dólar Americano")
                .valor(new BigDecimal("5.42"))
                .variacaoPercentual(new BigDecimal("0.23"))
                .dataCotacao(LocalDateTime.of(2026, 8, 14, 10, 0))
                .build();
        Cotacao cotacaoMaisRecente = Cotacao.builder()
                .codigoMoeda("USD")
                .nome("Dólar Americano")
                .valor(new BigDecimal("6.00"))
                .variacaoPercentual(new BigDecimal("1.00"))
                .dataCotacao(LocalDateTime.of(2026, 8, 15, 10, 0))
                .build();
        when(cotacaoClient.buscarCotacoes(any()))
                .thenReturn(List.of(cotacaoEmCache))
                .thenReturn(List.of(cotacaoMaisRecente));

        cotacaoService.atualizarCotacoes();
        List<Cotacao> primeiraLeitura = cotacaoService.obterCotacoesAtuais();
        List<Cotacao> segundaLeitura = cotacaoService.obterCotacoesAtuais();

        assertThat(primeiraLeitura).hasSize(1);
        assertThat(primeiraLeitura.getFirst().getValor()).isEqualByComparingTo("5.42");
        assertThat(segundaLeitura).hasSize(1);
        assertThat(segundaLeitura.getFirst().getValor()).isEqualByComparingTo("5.42");
        verify(cotacaoClient, times(1)).buscarCotacoes(any());
    }
}
