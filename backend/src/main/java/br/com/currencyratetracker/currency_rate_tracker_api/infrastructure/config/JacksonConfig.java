package br.com.currencyratetracker.currency_rate_tracker_api.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JacksonConfig {

    /** {@code ObjectMapper} do Jackson 2, com suporte a tipos de data/hora do Java 8+. */
    @Bean
    ObjectMapper jackson2ObjectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
}
