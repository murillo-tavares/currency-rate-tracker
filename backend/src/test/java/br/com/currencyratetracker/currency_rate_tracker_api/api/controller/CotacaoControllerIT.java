package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.client.CotacaoClient;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.client.CotacaoIndisponivelException;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.repository.CotacaoRepository;
import br.com.currencyratetracker.currency_rate_tracker_api.support.suite.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CotacaoControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CotacaoRepository cotacaoRepository;

    @MockitoBean
    private CotacaoClient cotacaoClient;

    /** A AwesomeAPI real não é chamada em teste; {@link CotacaoClient} é trocado por um mock. */
    @Test
    void deveListarCotacoesAtuais() throws Exception {
        Cotacao cotacao = Cotacao.builder()
                .codigoMoeda("USD")
                .nome("Dólar Americano")
                .valor(new BigDecimal("5.42"))
                .variacaoPercentual(new BigDecimal("0.23"))
                .dataCotacao(LocalDateTime.of(2026, 8, 14, 10, 0))
                .build();
        when(cotacaoClient.buscarCotacoes(any())).thenReturn(List.of(cotacao));

        mockMvc.perform(get("/cotacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].codigoMoeda").value("USD"))
                .andExpect(jsonPath("$[0].valor").value(5.42));
    }

    /** Falha na origem vira um erro de negócio (503), não um 500 genérico. */
    @Test
    void deveRetornar503QuandoAwesomeApiFalha() throws Exception {
        when(cotacaoClient.buscarCotacoes(any()))
                .thenThrow(CotacaoIndisponivelException.falhaNaConsulta(new RuntimeException("timeout")));

        mockMvc.perform(get("/cotacoes"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.codigo").value(CotacaoIndisponivelException.CODIGO));
    }

    @Test
    void deveListarDashboardFiltradoPorCodigos() throws Exception {
        salvarCotacao("USD", "5.42", LocalDateTime.now().minusHours(1));
        salvarCotacao("EUR", "6.10", LocalDateTime.now().minusHours(1));
        salvarCotacao("GBP", "7.00", LocalDateTime.now().minusHours(1));

        mockMvc.perform(get("/cotacoes/dashboard").param("codigosMoeda", "USD", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.graficos.length()").value(2))
                .andExpect(jsonPath("$.graficos[0].codigoMoeda").value("EUR"))
                .andExpect(jsonPath("$.graficos[1].codigoMoeda").value("USD"));
    }

    @Test
    void deveListarDashboardComTodasAsMoedasSemFiltroDeCodigo() throws Exception {
        salvarCotacao("USD", "5.42", LocalDateTime.now().minusHours(1));
        salvarCotacao("EUR", "6.10", LocalDateTime.now().minusHours(1));

        mockMvc.perform(get("/cotacoes/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.graficos.length()").value(2));
    }

    @Test
    void deveIgnorarCotacaoForaDaJanelaPadraoDe24Horas() throws Exception {
        salvarCotacao("USD", "5.00", LocalDateTime.now().minusDays(2));
        salvarCotacao("USD", "5.42", LocalDateTime.now().minusHours(1));

        mockMvc.perform(get("/cotacoes/dashboard").param("codigosMoeda", "USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.graficos[0].pontos.length()").value(1))
                .andExpect(jsonPath("$.graficos[0].pontos[0].valor").value(5.42));
    }

    private void salvarCotacao(String codigoMoeda, String valor, LocalDateTime dataCotacao) {
        cotacaoRepository.save(Cotacao.builder()
                .codigoMoeda(codigoMoeda)
                .nome(codigoMoeda)
                .valor(new BigDecimal(valor))
                .variacaoPercentual(BigDecimal.ZERO)
                .dataCotacao(dataCotacao)
                .build());
    }
}
