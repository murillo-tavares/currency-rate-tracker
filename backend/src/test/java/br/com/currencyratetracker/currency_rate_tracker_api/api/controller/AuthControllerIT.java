package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.auth.CredenciaisInvalidasException;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.UsuarioService;
import br.com.currencyratetracker.currency_rate_tracker_api.support.suite.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void deveLogarComCredenciaisValidasEDevolverToken() throws Exception {
        usuarioService.cadastrar("login@exemplo.com", "Teste", "senha123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "login@exemplo.com", "senha": "senha123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void deveRejeitarSenhaIncorreta() throws Exception {
        usuarioService.cadastrar("senhaerrada@exemplo.com", "Teste", "senha123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "senhaerrada@exemplo.com", "senha": "outrasenha"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value(CredenciaisInvalidasException.CODIGO));
    }

    @Test
    void deveRejeitarEmailInexistente() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "naoexiste@exemplo.com", "senha": "senha123"}
                                """))
                .andExpect(status().isUnauthorized());
    }
}
