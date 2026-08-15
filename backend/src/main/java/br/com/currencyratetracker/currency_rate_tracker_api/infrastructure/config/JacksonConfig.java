package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JacksonConfig {

    /** Singleton com suporte a tipos de data/hora do Java 8+, acessível fora do contexto do Spring. */
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Bean
    ObjectMapper jackson2ObjectMapper() {
        return OBJECT_MAPPER;
    }
}
