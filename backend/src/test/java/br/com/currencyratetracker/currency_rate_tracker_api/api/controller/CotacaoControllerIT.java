package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.client.CotacaoClient;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.client.CotacaoIndisponivelException;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
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

    @MockitoBean
    private CotacaoClient cotacaoClient;

    /** A AwesomeAPI real não é chamada em teste; {@link CotacaoClient} é trocado por um mock. */
    @Test
    void deveListarCotacoesAtuais() throws Exception {
        Cotacao dolar = new Cotacao(
                "USD",
                "Dólar Americano",
                new BigDecimal("5.42"),
                new BigDecimal("0.23"),
                LocalDateTime.of(2026, 8, 14, 10, 0));
        when(cotacaoClient.buscarCotacoes(any())).thenReturn(List.of(dolar));

        mockMvc.perform(get("/cotacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].codigo").value("USD"))
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
}
