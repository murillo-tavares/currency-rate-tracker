package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.security;

import br.com.currencyratetracker.currency_rate_tracker_api.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Emissão e validação de tokens JWT usados para autenticar requisições.
 */
@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties properties;

    /** Gera um token com o id do usuário como subject, válido por {@code expiracaoMillis}. */
    public String gerarToken(UUID usuarioId) {
        Instant agora = Instant.now();
        return Jwts.builder()
                .subject(usuarioId.toString())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusMillis(properties.expiracaoMillis())))
                .signWith(chave())
                .compact();
    }

    /** Id do usuário contido no token. Lança exceção não verificada se inválido/expirado. */
    public UUID extrairUsuarioId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(chave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return UUID.fromString(claims.getSubject());
    }

    private SecretKey chave() {
        return Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }
}
