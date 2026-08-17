package br.com.currencyratetracker.currency_rate_tracker_api.api.controller;

import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.auth.LoginRequest;
import br.com.currencyratetracker.currency_rate_tracker_api.api.dto.auth.LoginResponse;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.usuario.Usuario;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.UsuarioService;
import br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint REST de autenticação.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    /** Autentica por email/senha e devolve um token JWT. */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest requisicao) {
        Usuario usuario = usuarioService.autenticar(requisicao.email(), requisicao.senha());
        String token = jwtService.gerarToken(usuario.getId());
        return new LoginResponse(token);
    }
}
