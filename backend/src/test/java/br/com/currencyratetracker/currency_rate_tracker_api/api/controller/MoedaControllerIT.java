package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.support.suite.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MoedaControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /** O catálogo já vem populado pela migration de seed, sem precisar de dados de teste. */
    @Test
    void deveListarMoedasDoCatalogo() throws Exception {
        mockMvc.perform(get("/moedas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(12))
                .andExpect(jsonPath("$[*].codigo", hasItem("USD")))
                .andExpect(jsonPath("$[*].nome", hasItem("Dólar Americano")));
    }
}
