package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.usuario.CadastroRequest;
import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.usuario.UsuarioResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.api.mapper.UsuarioMapper;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.usuario.Usuario;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint REST de cadastro de usuário. Converte entre DTO e domínio usando {@link UsuarioMapper}.
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    /** Cadastra um novo usuário. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(@Valid @RequestBody CadastroRequest requisicao) {
        Usuario usuario = usuarioService.cadastrar(requisicao.email(), requisicao.nome(), requisicao.senha());
        return usuarioMapper.toResponse(usuario);
    }
}
