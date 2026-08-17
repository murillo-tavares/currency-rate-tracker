package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.moeda.MoedaNaoEncontradaException;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.usuario.Usuario;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.UsuarioService;
import br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.security.JwtService;
import br.com.currencyratetracker.currency_rate_tracker_api.support.suite.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavoritoControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Test
    void deveRejeitarRequisicaoSemToken() throws Exception {
        mockMvc.perform(get("/favoritos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveAdicionarEListarFavorito() throws Exception {
        String token = tokenParaNovoUsuario("favoritos@exemplo.com");

        mockMvc.perform(post("/favoritos/USD").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/favoritos").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].codigoMoeda").value("USD"));
    }

    @Test
    void naoDeveDuplicarAoFavoritarAMesmaMoedaDuasVezes() throws Exception {
        String token = tokenParaNovoUsuario("duplofavorito@exemplo.com");

        mockMvc.perform(post("/favoritos/USD").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
        mockMvc.perform(post("/favoritos/USD").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/favoritos").header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveRejeitarMoedaInexistenteNoCatalogo() throws Exception {
        String token = tokenParaNovoUsuario("moedainvalida@exemplo.com");

        mockMvc.perform(post("/favoritos/ZZZ").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value(MoedaNaoEncontradaException.CODIGO));
    }

    @Test
    void deveRemoverFavorito() throws Exception {
        String token = tokenParaNovoUsuario("removerfavorito@exemplo.com");

        mockMvc.perform(post("/favoritos/USD").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/favoritos/USD").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/favoritos").header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.length()").value(0));
    }

    /** Remover uma moeda que não é favorita não deve dar erro. */
    @Test
    void deveSerIdempotenteAoRemoverFavoritoInexistente() throws Exception {
        String token = tokenParaNovoUsuario("removerinexistente@exemplo.com");

        mockMvc.perform(delete("/favoritos/USD").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    /** Um usuário nunca deve ver favoritos de outro. */
    @Test
    void naoDeveListarFavoritoDeOutroUsuario() throws Exception {
        String tokenUsuarioA = tokenParaNovoUsuario("usuarioA@exemplo.com");
        String tokenUsuarioB = tokenParaNovoUsuario("usuarioB@exemplo.com");

        mockMvc.perform(post("/favoritos/USD").header("Authorization", "Bearer " + tokenUsuarioA))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/favoritos").header("Authorization", "Bearer " + tokenUsuarioB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    private String tokenParaNovoUsuario(String email) {
        Usuario usuario = usuarioService.cadastrar(email, "Teste", "senha123");
        return jwtService.gerarToken(usuario.getId());
    }
}
