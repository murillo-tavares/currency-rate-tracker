package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.cotacao.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.repository.CotacaoRepository;
import br.com.currencyratetracker.currency_rate_tracker_api.support.suite.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CotacaoControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CotacaoRepository cotacaoRepository;

    /** Só a mais recente de cada moeda aparece, mesmo havendo histórico anterior. */
    @Test
    void deveListarApenasAUltimaCotacaoDeCadaMoeda() throws Exception {
        salvarCotacao("USD", "5.00", LocalDateTime.now().minusHours(2));
        salvarCotacao("USD", "5.42", LocalDateTime.now().minusHours(1));
        salvarCotacao("EUR", "6.10", LocalDateTime.now().minusHours(1));

        mockMvc.perform(get("/cotacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].codigoMoeda").value("EUR"))
                .andExpect(jsonPath("$[1].codigoMoeda").value("USD"))
                .andExpect(jsonPath("$[1].valor").value(5.42));
    }

    /** Se a AwesomeAPI repete a mesma data_cotacao entre ciclos do scheduler, prevalece o registro mais recém-inserido. */
    @Test
    void deveDesempatarPorDataCriacaoQuandoDataCotacaoEmpata() throws Exception {
        LocalDateTime mesmaDataCotacao = LocalDateTime.now().minusHours(1);
        salvarCotacao("USD", "5.00", mesmaDataCotacao);
        Thread.sleep(50);
        salvarCotacao("USD", "5.42", mesmaDataCotacao);

        mockMvc.perform(get("/cotacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].valor").value(5.42));
    }

    /** Filtra as cotações atuais pelas moedas informadas. */
    @Test
    void deveFiltrarCotacoesAtuaisPorCodigo() throws Exception {
        salvarCotacao("USD", "5.42", LocalDateTime.now().minusHours(1));
        salvarCotacao("EUR", "6.10", LocalDateTime.now().minusHours(1));
        salvarCotacao("GBP", "7.00", LocalDateTime.now().minusHours(1));

        mockMvc.perform(get("/cotacoes").param("codigosMoeda", "USD", "EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].codigoMoeda").value("EUR"))
                .andExpect(jsonPath("$[1].codigoMoeda").value("USD"));
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
