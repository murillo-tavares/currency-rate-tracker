package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.config;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.DashboardCotacoes;
import br.com.currencyratetracker.currency_rate_tracker_api.domain.service.CotacaoService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import java.time.Duration;
import java.util.List;

/**
 * Caches de cotação serializados em JSON (em vez do padrão Java nativo), pra ficar legível
 * direto no Redis, e com TTL de segurança caso o job agendado pare de rodar.
 */
@Configuration
class RedisCacheConfig {

    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(ObjectMapper jackson2ObjectMapper) {
        JavaType tipoListaCotacao = jackson2ObjectMapper.getTypeFactory()
                .constructCollectionType(List.class, Cotacao.class);
        JavaType tipoDashboard = jackson2ObjectMapper.getTypeFactory().constructType(DashboardCotacoes.class);

        RedisCacheConfiguration configuracaoCotacoes =
                configuracaoJson(jackson2ObjectMapper, tipoListaCotacao, Duration.ofMinutes(5));
        RedisCacheConfiguration configuracaoDashboard =
                configuracaoJson(jackson2ObjectMapper, tipoDashboard, Duration.ofMinutes(2));

        return builder -> builder
                .withCacheConfiguration(CotacaoService.CACHE_COTACOES, configuracaoCotacoes)
                .withCacheConfiguration(CotacaoService.CACHE_DASHBOARD, configuracaoDashboard);
    }

    private RedisCacheConfiguration configuracaoJson(ObjectMapper objectMapper, JavaType tipo, Duration ttl) {
        JsonRedisSerializer serializador = new JsonRedisSerializer(objectMapper, tipo);
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .serializeValuesWith(SerializationPair.fromSerializer(serializador));
    }
}
