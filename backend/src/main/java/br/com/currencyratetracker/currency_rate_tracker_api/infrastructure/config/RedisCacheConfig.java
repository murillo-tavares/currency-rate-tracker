package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.config;

import br.com.currencyratetracker.currency_rate_tracker_api.domain.model.Cotacao;
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
 * Cache da lista de cotações serializado em JSON (em vez do padrão Java nativo), pra ficar
 * legível direto no Redis, e com TTL de segurança caso o job agendado pare de rodar.
 */
@Configuration
class RedisCacheConfig {

    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(ObjectMapper jackson2ObjectMapper) {
        JavaType tipoListaCotacao = jackson2ObjectMapper.getTypeFactory()
                .constructCollectionType(List.class, Cotacao.class);
        JsonRedisSerializer serializador = new JsonRedisSerializer(jackson2ObjectMapper, tipoListaCotacao);

        RedisCacheConfiguration configuracaoCotacoes = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .serializeValuesWith(SerializationPair.fromSerializer(serializador));

        return builder -> builder.withCacheConfiguration(CotacaoService.CACHE_COTACOES, configuracaoCotacoes);
    }
}
