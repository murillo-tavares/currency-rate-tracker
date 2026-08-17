package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Lê o Bearer token do header Authorization e autentica a requisição, se válido.
 * Token ausente/inválido segue sem autenticação; quem decide se bloqueia é o {@link SecurityConfig}.
 */
@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIXO_BEARER = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = extrairToken(request);
        if (token != null) {
            autenticarSePossivel(token);
        }
        filterChain.doFilter(request, response);
    }

    private String extrairToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        boolean temBearer = header != null && header.startsWith(PREFIXO_BEARER);
        return temBearer ? header.substring(PREFIXO_BEARER.length()) : null;
    }

    private void autenticarSePossivel(String token) {
        try {
            UUID usuarioId = jwtService.extrairUsuarioId(token);
            var authentication = new UsernamePasswordAuthenticationToken(usuarioId, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RuntimeException tokenInvalido) {
            // Token inválido/expirado: segue sem autenticar; endpoint protegido barra sozinho.
        }
    }
}
