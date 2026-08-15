package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.config;

import com.fasterxml.jackson.databind.JavaType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import java.time.Duration;

/**
 * Monta {@link RedisCacheConfiguration} serializadas em JSON, usando o {@link JacksonConfig#OBJECT_MAPPER} compartilhado.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RedisCacheConfigUtils {

    static RedisCacheConfiguration json(JavaType tipo, Duration ttl) {
        JsonRedisSerializer serializador = new JsonRedisSerializer(JacksonConfig.OBJECT_MAPPER, tipo);
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .serializeValuesWith(SerializationPair.fromSerializer(serializador));
    }
}
