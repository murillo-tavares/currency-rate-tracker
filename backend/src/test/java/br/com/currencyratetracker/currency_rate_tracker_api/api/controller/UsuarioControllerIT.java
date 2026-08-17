package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.exception.usuario.EmailJaCadastradoException;
import br.com.currencyratetracker.currency_rate_tracker_api.support.suite.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioControllerIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCadastrarUsuario() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "teste@exemplo.com", "nome": "Teste", "senha": "senha123"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("teste@exemplo.com"))
                .andExpect(jsonPath("$.nome").value("Teste"));
    }

    @Test
    void deveRejeitarEmailJaCadastrado() throws Exception {
        String corpo = """
                {"email": "duplicado@exemplo.com", "nome": "Teste", "senha": "senha123"}
                """;

        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value(EmailJaCadastradoException.CODIGO));
    }

    @Test
    void deveRejeitarEmailComFormatoInvalido() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "nao-e-email", "nome": "Teste", "senha": "senha123"}
                                """))
                .andExpect(status().isBadRequest());
    }

    /** Confirma a mensagem customizada — sem isso, o padrão do Bean Validation soa "entre 6 e 2147483647". */
    @Test
    void deveRejeitarSenhaMenorQueOMinimoComMensagemClara() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "senhacurta@exemplo.com", "nome": "Teste", "senha": "123"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("senha deve ter entre 6 e 72 caracteres"));
    }
}
