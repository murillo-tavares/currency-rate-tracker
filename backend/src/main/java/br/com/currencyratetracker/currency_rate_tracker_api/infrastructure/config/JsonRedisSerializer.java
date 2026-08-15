package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

/**
 * Serializa/desserializa um valor de cache em JSON, pra um tipo Java já conhecido de antemão.
 * Substitui o {@code Jackson2JsonRedisSerializer} (depreciado desde o Spring Data Redis 4.0).
 */
class JsonRedisSerializer implements RedisSerializer<Object> {

    private final ObjectMapper objectMapper;
    private final JavaType tipo;

    JsonRedisSerializer(ObjectMapper objectMapper, JavaType tipo) {
        this.objectMapper = objectMapper;
        this.tipo = tipo;
    }

    @Override
    public byte[] serialize(Object valor) throws SerializationException {
        if (valor == null) {
            return new byte[0];
        }
        try {
            return objectMapper.writeValueAsBytes(valor);
        } catch (JsonProcessingException exception) {
            throw new SerializationException("Não foi possível serializar o valor para JSON", exception);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, tipo);
        } catch (IOException exception) {
            throw new SerializationException("Não foi possível desserializar o valor do JSON", exception);
        }
    }
}
